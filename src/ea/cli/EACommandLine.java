package ea.cli;

import ea.codegen.EAApiGen;
import org.scribble.cli.CLFlags;
import org.scribble.cli.CommandLine;
import org.scribble.cli.CommandLineException;
import org.scribble.core.job.Core;
import org.scribble.core.job.CoreArgs;
import org.scribble.core.job.CoreContext;
import org.scribble.core.lang.global.GProtocol;
import org.scribble.core.model.endpoint.EGraph;
import org.scribble.core.type.name.GProtoName;
import org.scribble.core.type.name.Role;
import org.scribble.job.Job;
import org.scribble.job.JobContext;
import org.scribble.util.AntlrSourceException;
import org.scribble.util.Pair;
import org.scribble.util.ScribException;

public class EACommandLine extends CommandLine {

    public EACommandLine(String... args) {
        super(args);
    }

    public static void main(String[] args)
            throws CommandLineException, AntlrSourceException {

        ////CommandLine.main(args);
        new EACommandLine(args).run();
        //eamain();  // !!! base CommandLine fully bypassed -- No main module used

        ////testParser();
    }

    @Override
    protected CLFlags newCLFlags() {
        return new EACLFlags();
    }

    // A Scribble extension should override as appropriate
    // TODO: rename, barrier misleading (sounds like a sync)
    @Override
    protected void tryBarrierTask(Job job, Pair<String, String[]> task)
            throws ScribException, CommandLineException {
        switch (task.left) {
            case EACLFlags.EA_API_GEN_FLAG: {
                JobContext jobc = job.getContext();
                GProtoName fullname = checkGlobalProtocolArg(jobc, task.right[0]);
                //Map<String, String> out = jgen.generateSessionApi(fullname);* /
                //System.out.println("aaaaaaa: " + task.left + ",, " + fullname);
                Core core = job.getCore();
                CoreContext corec = core.getContext();
                GProtocol inlined = corec.getInlined(fullname);
                EAApiGen gen = new EAApiGen();
                System.out.println("\n" + gen.generateProtoAPI(inlined));
                for (Role r : inlined.roles) {
                    EGraph efsm = job.config.args.get(CoreArgs.MIN_EFSM)
                                  ? corec.getMinimisedEGraph(fullname, r)
                                  : corec.getEGraph(fullname, r);
                    foo(gen, inlined, r, efsm);
                }
                break;
            }
            default:
                super.tryBarrierTask(job, task);
        }
    }

    protected void foo(EAApiGen gen, GProtocol inlined, Role r, EGraph efsm) {
        System.out.println("\n" + gen.generateRoleAPI(inlined, r, efsm));
    }
}
