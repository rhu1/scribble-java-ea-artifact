package ea.codegen.scala;

import java.util.List;

class GClass extends GClassOrCompanion {
    public GClass(List<String> mods, String name, List<GParam> params,
                  List<GField> fields, List<GMethod> methods, List<String> supers) {
        super("class", mods, name, params, fields, methods, supers);
    }
}
