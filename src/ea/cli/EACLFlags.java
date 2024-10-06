package ea.cli;

import org.scribble.cli.CLFlag;
import org.scribble.cli.CLFlags;

import java.util.Map;


// Flag Strings must start with "-", e.g., "-project"
// A Scribble extension should override getFlags
public class EACLFlags extends CLFlags {

    // Non-unique flags
    public static final String EA_API_GEN_FLAG = "-ea-api";

    public EACLFlags() {
        super();
    }

    // Return a map of flag Strings to flag objects
    // A Scribble extension should override getFlags -- e.g., call super, then put any additional
    protected Map<String, CLFlag> getFlags() {
        Map<String, CLFlag> flags = super.getFlags();
        flags.put(EA_API_GEN_FLAG,
                new CLFlag(EA_API_GEN_FLAG, 1, false, true, true,
                        "Missing protocol argument: "));
        return flags;
    }
}
