package ea.codegen.scala;

class GPackage implements GIndentable {
    public final String name;

    public GPackage(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String pref) {
        return pref + "package " + this.name;
    }
}
