package ea.codegen.scala;

import java.util.List;

class GImport implements GIndentable {
    public final String pref;  // no trailing "."
    public final List<String> names;  // non-empty

    public GImport(String pref, List<String> names) {
        this.pref = pref;
        this.names = List.copyOf(names);
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String pref) {
        return pref + "import " + this.pref + "."
                + (this.names.size() == 1 ? this.names.getFirst() : "{" + String.join(", ", this.names) + "}");
    }
}
