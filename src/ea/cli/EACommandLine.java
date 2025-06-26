package ea.cli;

import ea.codegen.scala.EAApiGen;
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

import java.util.Map;

public class EACommandLine extends CommandLine {

    public EACommandLine(String... args) {
        super(args);
    }

    public static void main(String[] args)
            throws CommandLineException, AntlrSourceException {
        new EACommandLine(args).run();
    }

    @Override
    protected CLFlags newCLFlags() {
        return new EACLFlags();
    }

    // A Scribble extension should override as appropriate
    @Override
    protected void tryBarrierTask(Job job, Pair<String, String[]> task)
            throws ScribException, CommandLineException {
        switch (task.left) {
            case EACLFlags.EA_API_GEN_FLAG: {
                JobContext jobc = job.getContext();
                GProtoName fullname = checkGlobalProtocolArg(jobc, task.right[0]);
                Core core = job.getCore();
                CoreContext corec = core.getContext();
                GProtocol inlined = corec.getInlined(fullname);
                EAApiGen gen = new EAApiGen();

                Map<String, String> proto = gen.generateProtoAPI(inlined);
                outputClasses(proto);

                for (Role r : inlined.roles) {
                    EGraph efsm = job.config.args.get(CoreArgs.MIN_EFSM)
                                  ? corec.getMinimisedEGraph(fullname, r)
                                  : corec.getEGraph(fullname, r);

                    Map<String, String> roles = gen.generateRoleAPI(inlined, r, efsm);
                    outputClasses(roles);
                }
                break;
            }
            default:
                super.tryBarrierTask(job, task);
        }
    }
}
