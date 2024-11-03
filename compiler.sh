#!/bin/bash

# code.txtから入力を読み込み、コンパイルを実行
mvn -q -e exec:java -Dexec.mainClass="net.nanakusa.compiler.Compiler" -Dexec.args="code.txt" >output.bin
cat output.bin
