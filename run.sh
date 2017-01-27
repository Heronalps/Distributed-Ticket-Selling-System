#!/usr/bin/env bash
#cp Scenario1 ConfigOriginal;
#cp ConfigOriginal Config;
find . -name "*.java" > source.txt;
javac @source.txt;
java -cp ./src com.ucsb.michaelzhang.Main;
