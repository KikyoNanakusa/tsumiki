package net.nanakusa.compiler;

import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompilerIntegrationTest {

  @Test
  void testCompilationAndExecution() throws Exception {
    // `src/test/resources/test_cases.txt`ファイルを読み込む
    try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/test_cases.txt"))) {
      String line;
      int testCaseNumber = 1;

      // 各テストケースを一行ずつ読み込む
      while ((line = br.readLine()) != null) {
        System.out.println("Running test case #" + testCaseNumber);
        System.out.println("Test case data: " + line);

        String[] parts = line.split(",");
        if (parts.length != 2) {
          System.err.println("Invalid test case format: " + line);
          testCaseNumber++;
          continue; // 無効なフォーマットの場合スキップ
        }

        String code = parts[0].trim();
        String expectedOutput = parts[1].trim();

        // 一時ファイルにコードを書き込み
        File tempFile = File.createTempFile("tempCode", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
          writer.write(code);
        }
        System.out.println("Code written to temporary file: " + tempFile.getAbsolutePath());

        // コンパイラを呼び出し、一時ファイルのパスを引数として渡す
        File outputFile = new File("output.bin");
        if (outputFile.exists()) {
          outputFile.delete();
        }

        ProcessBuilder compilerProcessBuilder = new ProcessBuilder(
            "java", "-cp", "target/classes", "net.nanakusa.compiler.Compiler", tempFile.getAbsolutePath());

        // コンパイラの出力をoutput.binにリダイレクト
        compilerProcessBuilder.redirectOutput(outputFile);
        Process compilerProcess = compilerProcessBuilder.start();
        compilerProcess.waitFor();
        System.out.println("Compilation completed. Output written to output.bin");

        // output.binが生成されたことを確認
        if (!outputFile.exists()) {
          throw new FileNotFoundException("output.bin was not created by the compiler.");
        }

        // 仮想マシンを呼び出し、output.binを入力として渡す
        ProcessBuilder vmProcessBuilder = new ProcessBuilder(
            "java", "-cp", "target/classes", "net.nanakusa.virtualMachine.Main");
        Process vmProcess = vmProcessBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(vmProcess.getInputStream()));
        String lastLine = null;
        String outputLine;
        while ((outputLine = reader.readLine()) != null) {
          lastLine = outputLine; // 最後の行を保持
        }
        vmProcess.waitFor();
        System.out.println("Virtual Machine execution completed. Last output line: " + lastLine);

        assertEquals(expectedOutput, lastLine != null ? lastLine.trim() : "", "Code: " + code);
        System.out.println("Test case #" + testCaseNumber + " passed.\n");

        // 一時ファイルとoutput.binを削除
        tempFile.delete();
        outputFile.delete();

        testCaseNumber++;
      }
    }
  }
}
