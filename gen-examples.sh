#!/bin/sh

$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/hello/Hello.scr Proto1 -d $SCALA_BASE/src/main/scala

$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/id/Id.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/lockid/LockId.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/shoprestock/Shop.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/shoprestock/Shop.scr Proto2 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/robot/Robot.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/chat/Chat.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/chat/Chat.scr Proto2 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/chat/Chat.scr Proto3 -d $SCALA_BASE/src/main/scala

$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/pingself/PingSelf.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/pingself/PingSelf.scr ProtoC -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/ping/Ping.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/fib/Fib.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/fib/Fib.scr Proto2 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/diningself/DiningSelf.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/diningself/DiningSelf.scr Proto2 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/diningself/DiningSelf.scr Proto3 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/dining/Dining.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/dining/Dining.scr Proto2 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/sieve/Sieve.scr Proto1 -d $SCALA_BASE/src/main/scala
$JAVA_BASE/matyc-scala.sh $SCALA_EXAMPLE/savina/sieve/Sieve.scr Proto2 -d $SCALA_BASE/src/main/scala



