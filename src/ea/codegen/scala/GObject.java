package ea.codegen.scala;

import java.util.List;

class GObject extends GClassOrCompanion {
    public GObject(List<String> mods, String name, List<GParam> params,
                   List<GField> fields, List<GMethod> methods, List<String> supers) {
        super("object", mods, name, params, fields, methods, supers);
    }
}
