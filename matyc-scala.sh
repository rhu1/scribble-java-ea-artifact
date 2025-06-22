#!/bin/sh

#./scribblec-ea.sh c:/Users/Raymond/winroot/home/eey335/code/scala/intellij/git/github.com/rhu1/scribble-scala-ea-artifact/src/main/scala/ea/example/shoprestock/Shop.scr Proto1 -d test/scrib/out


JAVA_BASE="/home/paper177/artifact/scribble-java-ea-artfact"
SCALA_BASE="/home/paper177/artifact/scribble-scala-ea-artfact"

FILE="$1"
PROTO="$2"
shift
shift

java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath JAVA_BASE/out/production/scribble-java-ea-artifact:JAVA_BASE/lib/antlr.jar:JAVA_BASE/lib/commons-io.jar:JAVA_BASE/lib/scribble-ast.jar:JAVA_BASE/lib/scribble-cli.jar:JAVA_BASE/lib/antlr-runtime.jar:JAVA_BASE/lib/scribble-core.jar:JAVA_BASE/lib/scribble-main.jar:JAVA_BASE/lib/stringtemplate.jar:JAVA_BASE/lib/scribble-parser.jar:JAVA_BASE/lib/scribble-codegen.jar:JAVA_BASE/lib/scribble-runtime.jar ea.cli.EACommandLine -fair -ea-api $PROTO -v $FILE $@







