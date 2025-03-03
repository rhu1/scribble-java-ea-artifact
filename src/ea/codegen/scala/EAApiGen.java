package ea.codegen.scala;

import org.scribble.core.lang.global.GProtocol;
import org.scribble.core.model.endpoint.EGraph;
import org.scribble.core.model.endpoint.EState;
import org.scribble.core.model.endpoint.EStateKind;
import org.scribble.core.model.endpoint.actions.EAction;
import org.scribble.core.type.kind.PayElemKind;
import org.scribble.core.type.name.*;
import org.scribble.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EAApiGen {

    public static final String PID_TYPE = "Net.Pid";
    public static final String PID_PARAM_NAME = "pid";
    public static final String SID_TYPE = "Session.Sid";
    public static final String SID_PARAM_NAME = "sid";
    public static final String ROLE_TYPE = "Session.Role";
    public static final String ROLE_PARAM_NAME = "role";
    public static final String ACTOR_PARAM_NAME = "actor";
    public static final String OSTATE_TYPE = "Session.OState";
    public static final String ISTATE_TYPE = "Session.IState";
    public static final String SUSPEND_TYPE = "Session.SuspendState";
    public static final String END_TYPE = "Session.End";
    public static final String SEND_PAY_PARAM_NAME = "x";
    public static final String SUSPEND_CB_PARAM_NAME = "f";
    public static final String ACTOR_SPAWNANDREGISTER_METHOD = "spawnAndRegister";  // deprecate
    public static final String ACTOR_ENQUEUEREGISTER_METHOD = "enqueueRegisterForPeers";
    public static final String ACTOR_SENDMESSAGE_METHOD = "sendMessage";
    public static final String ACTOR_SUSPEND_METHOD = "suspend";
    public static final String ACTOR_FINISH_METHOD = "finish";
    public static final String ACTOR_END_METHOD = "end";
    public static final String DONE_TYPE = "Done.type";
    public static final String SEALED_KW = "sealed";
    //public static final String IS_USED_FIELD_NAME = "isUsed";
    public static final String CHECK_NOT_USED_METHOD = "checkNotUsed";
    public static final String CHECK_USED_METHOD = "checkUsed";

    public static final String TAB = "    ";

    //protected static final GField IS_USED_FIELD = new GField(List.of("var"), "Boolean", IS_USED_FIELD_NAME, "false");


    /* ... */

    public Map<String, String> generateProtoAPI(GProtocol inlined) {
        System.out.println("\n[EAAPIGen] Generating Proto API for: " + inlined.fullname);

        GProtoName proto = inlined.fullname.getSimpleName();
        List<GIndentable> membs = new LinkedList<>();

        //membs.add(new GPackage("tmp.scratch.scratch07." + getProtoPackageName(proto)));
        membs.add(new GPackage(fullnameToPackage(inlined.fullname)));
        membs.add(new GImport("ea.runtime", List.of("AP", "Session")));

        List<Role> all = //inlined.roles.stream().sorted((o1, o2) -> Comparator.<String>naturalOrder().compare(o1.toString(), o2.toString())).toList();
                inlined.roles;
        membs.add(generateAPCompanion(proto, all));
        membs.add(generateAPClass(proto));

        String file = fullnameToProtoFilename(inlined.fullname);
        return Map.of(file, membs.stream().map(GIndentable::toString)
                                 .collect(Collectors.joining("\n\n")));
    }

    protected String fullnameToPackage(GProtoName p) {
        String[] es = p.getElements();
        return Arrays.stream(es).limit(es.length - 1)
                     .collect(Collectors.joining("."))
                + "." + getProtoPackageName(p);  // refactor -- combine
    }

    protected String fullnameToProtoFilename(GProtoName p) {
         return fullnameToPackage(p).replaceAll("\\.", "/")
                + "/" + getProtoPackageName(p) + ".scala";
    }

    protected String getAPClassName(GProtoName proto) {
        return proto.getSimpleName().toString();
    }

    protected GObject generateAPCompanion(GProtoName proto, List<Role> all) {
        String name = getAPClassName(proto);
        List<GField> fields = List.of(
                new GField(List.of("val"), "String", "name", "\"" + name + "\""),
                new GField(List.of("val"), "Seq[Session.Role]", "roles", "Seq(" + all.stream().map(y -> "\"" + y + "\"").collect(Collectors.joining(", ")) + ")")
        );
        return new GObject(List.of(), name, List.of(), fields, List.of(), List.of());
    }

    protected GClass generateAPClass(GProtoName proto) {
        String name = getAPClassName(proto);
        List<String> supers = List.of("AP(" + name + ".name, " + name + ".roles.toSet)");
        return new GClass(List.of(), name, List.of(), List.of(), List.of(), supers);
    }


    /* ... */

    public Map<String, String> generateRoleAPI(GProtocol inlined, Role r, EGraph efsm) {

        System.out.println("\n[EAAPIGen] Generating Role API for: " + inlined.fullname + "@" + r);

        Pair<List<EState>, Map<Integer, String>> pair = getStatesAndNames(r, efsm);
        List<EState> ss = pair.left;
        Map<Integer, String> names = pair.right;

        GProtoName proto = inlined.fullname.getSimpleName();
        List<GIndentable> membs = new LinkedList<>();

        //membs.add(new GPackage("tmp.scratch.scratch07." + getProtoPackageName(proto)));
        membs.add(new GPackage(fullnameToPackage(inlined.fullname)));
        membs.add(new GImport("ea.runtime", List.of("Actor", "Done", "Session")));
        if (ss.stream().anyMatch(x -> x.getStateKind() == EStateKind.OUTPUT)) {
            membs.add(new GImport("java.io", List.of("IOException")));
        }

        List<Role> peers = //inlined.roles.stream().filter(x -> !x.equals(r)).sorted((o1, o2) -> Comparator.<String>naturalOrder().compare(o1.toString(), o2.toString())).toList();
                inlined.roles.stream().filter(x -> !x.equals(r)).toList();
        membs.add(generateActorTrait(names, proto, r, peers, ss.get(0)));

        for (EState s : ss) {
            EStateKind kind = s.getStateKind();
            switch (kind) {
                case OUTPUT -> membs.add(generateOutputState(names, proto, r, s));
                case UNARY_RECEIVE -> membs.addAll(generateInputState(names, proto, r, s));
                case POLY_RECIEVE -> membs.addAll(generateInputState(names, proto, r, s));
                case TERMINAL -> membs.add(generateTerminalState(names, proto, r, s));
                default -> throw new RuntimeException("Unexpected state kind: " + kind);
            }
        }

        //return generateTop(proto, r) + "\n" + res;
        String file = fullnameToRoleFilename(inlined.fullname, r);
        return Map.of(file, membs.stream().map(GIndentable::toString)
                                 .collect(Collectors.joining("\n\n")));
    }

    protected String fullnameToRoleFilename(GProtoName p, Role r) {
        return fullnameToPackage(p).replaceAll("\\.", "/")
                + "/" + getProtoPackageName(p) + "_" + r + ".scala";
    }

    protected Pair<List<EState>, Map<Integer, String>> getStatesAndNames(Role r, EGraph efsm) {
        EState init = efsm.init;
        List<EState> ss = new LinkedList<>();
        ss.add(init);
        Set<EState> reachable = new HashSet<>(init.getReachableStates());
        reachable.remove(init);
        ss.addAll(reachable.stream().sorted(Comparator.comparingInt(o -> o.id)).toList());

        int[] i = {1};
        Supplier<String> nextName = () -> r.toString() + i[0]++;
        Map<Integer, String> names = ss.stream().collect(Collectors.toMap(
                x -> x.id,
                x -> x.isTerminal() ? "End" + r : nextName.get()
        ));

        return new Pair<>(ss, names);
    }

    protected GTrait generateActorTrait(Map<Integer, String> names, GProtoName proto, Role r, List<Role> peers, EState init) {

        String initName = getInitName(names, init);

        String name = getActorClassName(r);
        //String actorType = getActorType(proto, r);
        List<GParam> params = List.of(new GParam(List.of(), PID_TYPE, PID_PARAM_NAME));
        //List<String> supers = List.of("Actor(" + PID_PARAM_NAME + ")");
        List<String> supers = List.of("Actor");
        List<GMethod> methods = List.of(generateRegister(proto, r, peers, initName));

        //return new GClass(List.of(), name, params, List.of(), methods, supers);
        return new GTrait(List.of(), name, supers, methods);  // !!! multi roles, e.g., trait SS extends ActorS with ActorSS
    }

    protected String getProtoPackageName(GProtoName proto) {
        //return proto.getSimpleName().toString().toLowerCase(Locale.ROOT);
        return proto.getSimpleName().toString();
    }

    protected String getActorClassName(Role r) {
        return "Actor" + r;
    }

    protected String getInitName(Map<Integer, String> names, EState init) {
        return getSuccTypeName(names, init);
    }

    protected GMethod generateRegister(GProtoName proto, Role r, List<Role> peers, String initName) {
        String name = "register" + r;
        List<GTParam> tParams = List.of(new GTParam("D", "Session.Data"));
        List<GParam> params = List.of(
                new GParam(List.of(), "Int", "port"),
                new GParam(List.of(), "String", "apHost"),
                new GParam(List.of(), "Int", "apPort"),
                new GParam(List.of(), "D", "d"),
                new GParam(List.of(), "(D, " + initName + ") => " + DONE_TYPE, "f"));
        String ret = "Unit";
        String body =
                "val g = (" + SID_PARAM_NAME + ": " + SID_TYPE + ") => " + initName + "(" + SID_PARAM_NAME + ", \"" + r + "\", this)"
                        + "\n" + ACTOR_ENQUEUEREGISTER_METHOD + "(apHost, apPort, \"" + proto + "\", \"" + r + "\", port, d, f, g, Set(" + peers.stream().map(y -> "\"" + y + "\"").collect(Collectors.joining(", ")) + "))";
        return new GMethod(List.of(), List.of(), name, tParams, params, ret, body);
    }

    // !!! Deprecate
    protected GMethod generateSpawnAndRegister(GProtoName proto, Role r, List<Role> peers, String initName) {
        String name = "spawnAndRegister";
        List<GTParam> tParams = List.of(new GTParam("D", "Session.Data"));
        List<GParam> params = List.of(
                new GParam(List.of(), "Int", "port"),
                new GParam(List.of(), "String", "apHost"),
                new GParam(List.of(), "Int", "apPort"),
                new GParam(List.of(), "D", "d"),
                new GParam(List.of(), "(D, " + initName + ") => " + DONE_TYPE, "f"));
        String ret = "Unit";
        String body =
                "val g = (" + SID_PARAM_NAME + ": " + SID_TYPE + ") => " + initName + "(" + SID_PARAM_NAME + ", this)"
                        + "\n" + ACTOR_SPAWNANDREGISTER_METHOD + "(apHost, apPort, \"" + proto + "\", \"" + r + "\", port, d, f, g, Set(" + peers.stream().map(y -> "\"" + y + "\"").collect(Collectors.joining(", ")) + "))";
        return new GMethod(List.of(), List.of(), name, tParams, params, ret, body);
    }

    /* ... */

    protected List<GIndentable> generateInputState(Map<Integer, String> names, GProtoName proto, Role r, EState s) {
        String susName = getSuspendTypeName(names, s);
        List<String> susMods = List.of("case");
        String actorType = getActorType(proto, r);
        List<GParam> susParams = List.of(
                new GParam(List.of(), SID_TYPE, SID_PARAM_NAME),
                new GParam(List.of(), ROLE_TYPE, ROLE_PARAM_NAME),
                new GParam(List.of(), actorType, ACTOR_PARAM_NAME));
        List<String> susSupers = List.of(SUSPEND_TYPE + "[" + actorType + "]");
        List<GField> susFields = List.of();
        List<GMethod> susMethods = List.of(generateSuspend(names, r, s));
        GClass sus = new GClass(susMods, susName, susParams, susFields, susMethods, susSupers);

        String name = getStateTypeName(names, s);
        GTrait state = new GTrait(List.of(SEALED_KW), name, List.of(ISTATE_TYPE), List.of());

        List<String> mods = susMods;
        List<GParam> params = List.of(
                new GParam(List.of(), SID_TYPE, SID_PARAM_NAME),
                new GParam(List.of(), ROLE_TYPE, ROLE_PARAM_NAME));
        //new GParam(List.of(), "String", SEND_PAY_PARAM_NAME));
        //new GParam(List.of(), getSuccTypeName(r, s, x), "s"));
        List<GClass> cases = s.getDetActions().stream()
                              .map(x -> new GClass(mods, getInputCaseType(r, (Op) x.mid),
                                      makeCaseParams(params, names, s, x),
                                      List.of(), List.of(), List.of(name)))
                              .toList();
        //new GClass(susMods, susName, susParams, susMethods, susSupers);

        return Stream.concat(Stream.of(sus, state), cases.stream()).toList();
    }

    protected List<GParam> makeCaseParams(
            List<GParam> params, Map<Integer, String> names, EState s, EAction x) {
        int[] c = { 1 };
        return Stream.concat(
                params.stream(),
                Stream.concat(
                    //new GParam(List.of(), getPayloadType(x).toString(), SEND_PAY_PARAM_NAME),
                    getPayloadTypes(x).stream().map(y ->
                            new GParam(List.of(), y.toString(), SEND_PAY_PARAM_NAME + c[0]++)),
                    Stream.of(new GParam(List.of(), getSuccTypeName(names, s, x), "s")))
        ).toList();
    }

    protected String getInputCaseType(Role r, Op op) {
        return op.toString() + r;
    }

    protected GMethod generateSuspend(Map<Integer, String> names, Role r, EState s) {
        String state = getStateTypeName(names, s);
        List<GTParam> tParams = List.of(new GTParam("D", "Session.Data"));
        List<GParam> params = List.of(
                new GParam(List.of(), "D", "d"),
                new GParam(List.of(), "(D, " + state + ") => " + DONE_TYPE, "f"));
        int[] c = { 0 };
        Function<List<DataName>, String> g = (x) ->
                //x.stream().map(y -> ", pay(" + c[0]++ + ").asInstanceOf[" + y + "]")
                x.stream().map(y -> {
                     if (y.toString().equals("String")) {
                         return ", actor.deserializeString(split(" + c[0]++ + "))";
                     } else if (y.toString().equals("Int")) {
                         return ", actor.deserializeInt(split(" + c[0]++ +"))";
                     } else if (y.toString().equals("Boolean")) {
                         return ", actor.deserializeBoolean(split(" + c[0]++ +"))";
                     } else {
                         throw new RuntimeException("TODO: " + y);
                     }
                 })
                 .collect(Collectors.joining());
        Function<EAction, String> f = (x) -> {
            c[0] = 0;
            return TAB + "if (op == \"" + x.mid + "\") {"
                    + "\n" + TAB + TAB + "val s = " + getSuccTypeName(names, s, x) + "(" + SID_PARAM_NAME + ", " + ROLE_PARAM_NAME + ", " + ACTOR_PARAM_NAME + ")"
                    + "\n" + TAB + TAB + "succ = Some(s)"
                    + "\n" + TAB + TAB + "val split = pay.split(\"::::\")"
                    //+ "\n" + TAB + TAB + getInputCaseType(r, (Op) x.mid) + "(" + SID_PARAM_NAME + ", pay.asInstanceOf[" + getPayloadType(x) + "], s)"
                    + "\n" + TAB + TAB + getInputCaseType(r, (Op) x.mid) + "(" + SID_PARAM_NAME + ", " + ROLE_PARAM_NAME + g.apply(getPayloadTypes(x)) + ", s)"
                    + "\n" + TAB + "} else ";
        };
        List<EAction> as = s.getDetActions();
        Role peer = as.get(0).peer;
        //c[0] = 1;
        String body = CHECK_NOT_USED_METHOD + "()"
                + "\nval g = (op: String, pay: String) => {"  // !!! Object -- cf. generic lambda not directly supported?
                //+ "\nval g = (op: String, pay: List[Object]) => {"  // !!! Object -- cf. generic lambda not directly supported?
                + "\n" + TAB + "var succ: Option[Session.ActorState[Actor]] = None"
                + "\n" + TAB + "val msg: " + state + " ="
                + "\n" + as.stream().map(f).collect(Collectors.joining())
                + "{"
                + "\n" + TAB + TAB + "throw new RuntimeException(s\"[ERROR] Unexpected op: ${op}(${pay})\")"
                + "\n" + TAB + "}"
                + "\n" + TAB + "val done = f.apply(d, msg)"
                + "\n" + TAB + "succ.get.checkUsed()"
                + "\n" + TAB + "done"
                + "\n}"
                + "\nactor.setHandler(" + SID_PARAM_NAME + ", \"" + r + "\", \"" + peer + "\", g)"
                + "\nDone";
        return new GMethod(List.of(), List.of(), ACTOR_SUSPEND_METHOD, tParams, params, DONE_TYPE, body);
    }

    protected GIndentable generateOutputState(Map<Integer, String> names, GProtoName proto, Role r, EState s) {
        String actorType = getActorType(proto, r);
        List<String> mods = List.of("case");
        String name = getStateTypeName(names, s);
        List<GParam> params = List.of(
                new GParam(List.of(), SID_TYPE, SID_PARAM_NAME),
                new GParam(List.of(), ROLE_TYPE, ROLE_PARAM_NAME),
                new GParam(List.of(), actorType, ACTOR_PARAM_NAME)
        );
        List<String> supers = List.of(OSTATE_TYPE + "[" + actorType + "]");

        List<GMethod> methods = Stream.concat(
                s.getDetActions().stream()
                 .map(x -> generateSend(r, x.peer, (Op) x.mid,
                         getPayloadTypes(x), getSuccTypeName(names, s, x))),
                Stream.of() //generateWeaken(name))
        ).toList();

        return new GClass(mods, name, params, List.of(), methods, supers);
    }

    /*protected GMethod generateWeaken(String stateType) {
        String name = "weaken";
        String ret = "(" + stateType + ", " + DONE_TYPE + ")";
        String body =
                "this.isUsed = true"
                        + "\n(" + stateType + "(" + SID_PARAM_NAME + ", " + ACTOR_PARAM_NAME + "), Done)";
        return new GMethod(List.of(), List.of(), name, List.of(), List.of(), ret, body);
    }*/

    protected GMethod generateSend(Role src, Role dst, Op op, List<DataName> pays, String ret) {
        String name = "send" + op;
        int[] c = { 1 };
        List<GParam> params = //List.of(new GParam(List.of(), pay.toString(), SEND_PAY_PARAM_NAME));
                pays.stream().map(x -> new GParam(List.of(), x.toString(), SEND_PAY_PARAM_NAME + c[0]++)).toList();
        c[0] = 1;
        String body = CHECK_NOT_USED_METHOD + "()"
                + "\nval pay = " +
                    (pays.isEmpty() ? "\"\"" :
                    pays.stream().map(x -> {
                    if (x.toString().equals("String")) {
                        return "actor.serializeString(" + SEND_PAY_PARAM_NAME + c[0]++ + ")";
                    } else if (x.toString().equals("Int")) {
                        return "actor.serializeInt(" + SEND_PAY_PARAM_NAME + c[0]++ + ")";
                    } else if (x.toString().equals("Boolean")) {
                        return "actor.serializeBoolean(" + SEND_PAY_PARAM_NAME + c[0]++ + ")";
                    } else {
                        throw new RuntimeException("TODO: " + x);
                    }
                 }).collect(Collectors.joining(" + \"::::\" + ")))
                //+ "\n" + ACTOR_PARAM_NAME + "." + ACTOR_SENDMESSAGE_METHOD + "(" + SID_PARAM_NAME + ", \"" + src + "\", \"" + dst + "\", \"" + op + "\"" + pays.stream().map(x -> ", " + SEND_PAY_PARAM_NAME + c[0]++).collect(Collectors.joining()) + ")"
                + "\n" + ACTOR_PARAM_NAME + "." + ACTOR_SENDMESSAGE_METHOD + "(" + SID_PARAM_NAME + ", \"" + src + "\", \"" + dst + "\", \"" + op + "\", pay)"
                + "\n" + ret + "(" + SID_PARAM_NAME + ", \"" + src + "\", " + ACTOR_PARAM_NAME + ")";
        return new GMethod(List.of("IOException"), List.of(), name, List.of(), params, ret, body);
    }

    protected GIndentable generateTerminalState(Map<Integer, String> names, GProtoName proto, Role r, EState s) {
        List<String> mods = List.of("case");
        String name = getStateTypeName(names, s);
        List<GParam> params = List.of(
                new GParam(List.of(), SID_TYPE, SID_PARAM_NAME),
                new GParam(List.of(), ROLE_TYPE, ROLE_PARAM_NAME),
                new GParam(List.of(), getActorType(proto, r), ACTOR_PARAM_NAME)
        );
        List<String> supers = List.of(END_TYPE + "[" + getActorType(proto, r) + "]");
        List<GMethod> methods = List.of(generateFinish(r));
        return new GClass(mods, name, params, List.of(), methods, supers);
    }

    protected GMethod generateFinish(Role r) {
        String name = ACTOR_FINISH_METHOD;
        String body = CHECK_NOT_USED_METHOD + "()"
                + "\nval done = super." + ACTOR_FINISH_METHOD + "()"
                + "\n" + ACTOR_PARAM_NAME + "." + ACTOR_END_METHOD + "(" + SID_PARAM_NAME + ", \"" + r + "\")"
                + "\ndone";
        //ACTOR_FINISH_METHOD + "(" + SID_PARAM_NAME + ")";
        return new GMethod(List.of(), List.of("override"), name, List.of(), List.of(), DONE_TYPE, body);
    }


    /* ... */

    protected List<DataName> getPayloadTypes(EAction a) {
        List<PayElemType<? extends PayElemKind>> elems = a.payload.elems;
        /*if (elems.size() != 1) {
            throw new RuntimeException("TODO: " + elems);
        }
        PayElemType<? extends PayElemKind> fst = elems.get(0);
        if (!fst.isDataName()) {
            throw new RuntimeException("TODO: " + fst);
        }
        return (DataName) fst;*/
        if (elems.stream().anyMatch(x -> !x.isDataName())) {
            throw new RuntimeException("TODO: " + elems);
        }
        return elems.stream().map(x -> (DataName) x).toList();
    }

    protected String getSuccTypeName(Map<Integer, String> names, EState s, EAction a) {
        EState succ = s.getDetSuccessor(a);
        return getSuccTypeName(names, succ);
    }

    protected String getSuccTypeName(Map<Integer, String> names, EState succ) {
        EStateKind kind = succ.getStateKind();
        return kind == EStateKind.UNARY_RECEIVE || kind == EStateKind.POLY_RECIEVE
               ? getSuspendTypeName(names, succ)
               : getStateTypeName(names, succ);
    }

    protected String getSuspendTypeName(Map<Integer, String> names, EState s) {
        return getStateTypeName(names, s) + "Suspend";
    }

    /*protected String getStateTypeName(Role r, EState s) {
        return s.getStateKind() == EStateKind.TERMINAL
                ? "End" + r
                : r.toString() + s.id;
    }*/
    // names already includes End
    protected String getStateTypeName(Map<Integer, String> names, EState s) {
        return names.get(s.id);
    }

    protected String getActorType(GProtoName proto, Role r) {
        //return proto + "Actor" + r;
        return "Actor";  // !!!
    }
}
