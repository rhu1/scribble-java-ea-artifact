package ea.codegen.scala;

import java.util.List;

class GParam {
    final List<String> mods;
    final String type;
    final String name;

    public GParam(List<String> mods, String type, String name) {
        this.mods = List.copyOf(mods);
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return (this.mods.isEmpty() ? "" : String.join(" ", this.mods) + " ")
                + this.name + ": " + this.type;
    }
}
