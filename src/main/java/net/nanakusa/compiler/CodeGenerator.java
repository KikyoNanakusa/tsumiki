package net.nanakusa.compiler;

public class CodeGenerator {
  private static int labelSeq = 0;

  public static void genLVar(Node node) {
    if (node.getType() != ND_TYPE.ND_LVAR) {
      throw new Error("not an lvar: " + node.getType());
    }

    // optimize the case where the variable is at the top of the stack
    if (node.getOffset() == 0) {
      System.out.printf("push_bp\n");
      return;
    }

    // consider the stack frame layout
    System.out.printf("push_bp\n");
    System.out.printf("push %d\n", node.getOffset());
    System.out.printf("add\n");

    // System.out.printf("push %d\n", node.getOffset());
  }

  public static void codegen(Node node) {
    if (node.getType() == ND_TYPE.ND_FUNC) {
      System.out.printf("%s:\n", node.getName());
      System.out.printf("push_sp\n");
      System.out.printf("set_bp\n");

      for (int i = 0; i < node.getLocals().size(); i++) {
        System.out.printf("push 0\n");
      }

      for (Node stmt : node.getStmts()) {
        codegen(stmt);
      }

      return;
    }

    switch (node.getType()) {
      case ND_ASSIGN:
        genLVar(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("st\n");
        return;
      case ND_LVAR:
        genLVar(node);
        System.out.printf("ld\n");
        return;
      case ND_NUM:
        System.out.printf("push %d\n", node.getVal());
        return;
      case ND_RETURN:
        codegen(node.getLhs());
        System.out.printf("ret\n");
        return;
      case ND_IF:
        int elseLabel = labelSeq++;
        int ifEndLabel = labelSeq++;

        codegen(node.getCond());
        System.out.printf("jz L%d\n", elseLabel);
        for (Node stmt : node.getThen()) {
          codegen(stmt);
        }

        System.out.printf("jmp L%d\n", ifEndLabel);
        System.out.printf("L%d:\n", elseLabel);
        if (node.getEls() != null) {
          for (Node stmt : node.getEls().getThen()) {
            codegen(stmt);
          }
        }

        System.out.printf("L%d:\n", ifEndLabel);

        return;
      case ND_WHILE:
        int beginLabel = labelSeq++; 
        int whileEndLabel = labelSeq++; 
        System.out.printf("L%d:\n", beginLabel);
        codegen(node.getCond());
        System.out.printf("jz L%d\n", whileEndLabel);
        for (Node stmt : node.getThen()) {
          codegen(stmt);
        }
        System.out.printf("jmp L%d\n", beginLabel);
        System.out.printf("L%d:\n", whileEndLabel);
        return;

      case ND_FOR:
        int fLabel = labelSeq;
        labelSeq++;
        codegen(node.getInit());
        System.out.printf("L%d:\n", fLabel);
        codegen(node.getCond());
        System.out.printf("jz L%dend\n", fLabel);
        for (Node stmt : node.getThen()) {
          codegen(stmt);
        }
        codegen(node.getInc());
        System.out.printf("jmp L%d\n", fLabel);
        System.out.printf("L%dend:\n", fLabel);

        return;
      case ND_CALL:
        System.out.printf("call %s\n", node.getName());
        return;
    }

    codegen(node.getLhs());
    codegen(node.getRhs());

    switch (node.getType()) {
      case ND_CALL:
        System.out.printf("call %s\n", node.getName());
        return;
      case ND_MUL:
        System.out.printf("mul\n");
        return;
      case ND_DIV:
        System.out.printf("div\n");
        return;
      case ND_ADD:
        System.out.printf("add\n");
        return;
      case ND_SUB:
        System.out.printf("sub\n");
        return;
      case ND_LESS:
        System.out.printf("lt\n");
        return;
      case ND_LEQ:
        System.out.printf("leq\n");
        return;
      case ND_EQ:
        System.out.printf("eq\n");
        return;
      case ND_NEQ:
        System.out.printf("neq\n");
        return;
    }
  }
}
