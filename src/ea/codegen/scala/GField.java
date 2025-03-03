package ea.codegen.scala;

import java.util.List;

class GField extends GParam implements GIndentable {

    public final String init;

    public GField(List<String> mods, String type, String name, String init) {
        super(mods, type, name);
        this.init = init;
    }

    @Override
    public String toString(String pref) {
        return pref + super.toString() + " = " + this.init;
    }
}
