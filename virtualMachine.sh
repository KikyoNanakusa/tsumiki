#!/bin/bash

# mvn -q -e exec:java -Dexec.mainClass="net.nanakusa.compiler.Compiler" -Dexec.args="code.txt" >output.bin
java -cp target/classes net.nanakusa.virtualMachine.Main
