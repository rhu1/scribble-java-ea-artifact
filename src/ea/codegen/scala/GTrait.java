package ea.codegen.scala;

import java.util.List;
import java.util.stream.Collectors;

class GTrait implements GIndentable {
    public final List<String> mods;
    public final String name;
    public final List<GMethod> methods;
    public final List<String> supers;

    public GTrait(List<String> mods, String name, List<String> supers, List<GMethod> methods) {
        this.mods = List.copyOf(mods);
        this.name = name;
        this.supers = List.copyOf(supers);
        this.methods = List.copyOf(methods);
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String pref) {
        return pref + (this.mods.isEmpty() ? "" : String.join(" ", this.mods) + " ") + "trait " + this.name + (this.supers.isEmpty() ? "" : " extends " + String.join(", ", supers))
                + (this.methods.isEmpty()
                   ? ""
                   : " {\n\n" + pref + this.methods.stream().map(x -> x.toString(pref + EAApiGen.TAB)).collect(Collectors.joining("\n\n")) + "\n}");
    }
}
