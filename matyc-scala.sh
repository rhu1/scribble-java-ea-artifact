#!/bin/sh


#./matyc-scala.sh $SCALA_BASE/src/main/scala/ea/example/hello/Hello.scr Proto1 -d out/scala
#./matyc-scala.sh $SCALA_EXAMPLE/hello/Hello.scr Proto1 -d out/scala


# Need at least two args or shift error
FILE="$1"
PROTO="$2"
shift
shift

java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath $JAVA_BASE/out/production/scribble-java-ea-artifact:$JAVA_BASE/lib/antlr.jar:$JAVA_BASE/lib/commons-io.jar:$JAVA_BASE/lib/scribble-ast.jar:$JAVA_BASE/lib/scribble-cli.jar:$JAVA_BASE/lib/antlr-runtime.jar:$JAVA_BASE/lib/scribble-core.jar:$JAVA_BASE/lib/scribble-main.jar:$JAVA_BASE/lib/stringtemplate.jar:$JAVA_BASE/lib/scribble-parser.jar:$JAVA_BASE/lib/scribble-codegen.jar:$JAVA_BASE/lib/scribble-runtime.jar ea.cli.EACommandLine -fair -ea-api $PROTO -v $FILE $@



