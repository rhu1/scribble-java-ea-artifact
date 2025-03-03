package ea.codegen.scala;

import java.util.List;
import java.util.stream.Collectors;

abstract class GClassOrCompanion implements GIndentable {
    public final String kind;
    public final List<String> mods;
    public final String name;
    public final List<GParam> params;
    public final List<GField> fields;
    public final List<GMethod> methods;
    public final List<String> supers;

    public GClassOrCompanion(String kind, List<String> mods, String name, List<GParam> params,
                             List<GField> fields, List<GMethod> methods, List<String> supers) {
        this.kind = kind;
        this.mods = List.copyOf(mods);
        this.name = name;
        this.params = List.copyOf(params);
        this.fields = List.copyOf(fields);
        this.methods = List.copyOf(methods);
        this.supers = List.copyOf(supers);
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String pref) {
        return pref + (this.mods.isEmpty() ? "" : String.join(" ", this.mods) + " ") + this.kind + " " + this.name + (this.params.isEmpty() ? "" : "(" + this.params.stream().map(GParam::toString).collect(Collectors.joining(", ")) + ")")
                + (this.supers.isEmpty() ? "" : " extends " + String.join(", ", supers))
                + (this.fields.isEmpty()
                   ? ""
                   : " {\n" + pref + this.fields.stream().map(x -> x.toString(pref + EAApiGen.TAB)).collect(Collectors.joining("\n")) + "\n}")
                + (this.methods.isEmpty()
                   ? ""
                   : " {\n\n" + pref + this.methods.stream().map(x -> x.toString(pref + EAApiGen.TAB)).collect(Collectors.joining("\n\n")) + "\n}");
    }
}
