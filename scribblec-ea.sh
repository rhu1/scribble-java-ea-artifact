#!/bin/sh

FILE="$1"
PROTO="$2"
shift
shift

java.exe -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\out\production\scribble-java-ea-artifact;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\antlr.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\commons-io.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-ast.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-cli.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\antlr-runtime.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-core.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-main.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\stringtemplate.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-parser.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-codegen.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-runtime.jar" ea.cli.EACommandLine -fair -ea-api $PROTO -v $FILE $@











# -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\out\production\scribble-java-ea-artifact;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\antlr.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\commons-io.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-ast.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-cli.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\antlr-runtime.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-core.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-main.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\stringtemplate.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-parser.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-codegen.jar;C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\lib\scribble-runtime.jar" ea.cli.EACommandLine -fair -ea-api Proto1 -v "C:\Users\Raymond\winroot\home\eey335\code\java\intellij\git\github.com\rhu1\scribble-java-ea-artifact\test\scrib\src\tmp\EATmp.scr"
