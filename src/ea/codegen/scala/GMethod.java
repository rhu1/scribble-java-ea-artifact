package ea.codegen.scala;

import java.util.List;
import java.util.stream.Collectors;

class GMethod implements GIndentable {
    public final List<String> thrws;
    public final List<String> mods;
    public final String name;
    public final List<GTParam> tParams;
    public final List<GParam> params;
    public final String ret;
    public final String body;

    public GMethod(List<String> thrws, List<String> mods, String name, List<GTParam> tParams, List<GParam> params, String ret, String body) {
        this.thrws = List.copyOf(thrws);
        this.mods = List.copyOf(mods);
        this.name = name;
        this.tParams = List.copyOf(tParams);
        this.params = List.copyOf(params);
        this.ret = ret;
        this.body = body;
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String pref) {
        return this.thrws.stream().map(x -> pref + "@throws[" + x + "]\n").collect(Collectors.joining())
                + pref + (this.mods.isEmpty() ? "" : String.join(" ", this.mods) + " ") + "def " + this.name + (this.tParams.isEmpty() ? "" : "[" + this.tParams.stream().map(GTParam::toString).collect(Collectors.joining(", ")) + "]") + "(" + this.params.stream().map(GParam::toString).collect(Collectors.joining(", ")) + "): " + this.ret + " = {"
                + "\n" + pref + EAApiGen.TAB + this.body.replaceAll("\\n", "\n" + pref + EAApiGen.TAB)
                + "\n" + pref + "}";
    }
}
