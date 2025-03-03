package ea.codegen.scala;

class GTParam {
    final String name;
    final String upper;

    public GTParam(String name, String upper) {
        this.name = name;
        this.upper = upper;
    }

    @Override
    public String toString() {
        return this.name + " <: " + this.upper;
    }
}
