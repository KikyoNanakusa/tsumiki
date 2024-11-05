package net.nanakusa.compiler;

public class CodeGenerator {
  private static int labelSeq = 0;

  public static void genLVar(Node node) {
    if (node.getType() != ND_TYPE.ND_LVAR) {
      throw new Error("not an lvar: " + node.getType());
    }

    System.out.printf("push %d\n", node.getOffset());
  }

  public static void codegen(Node node) {
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
        System.out.printf("end\n");
        return;
      case ND_IF:
        codegen(node.getCond());
        System.out.printf("jz L%dend\n", labelSeq);
        for (Node stmt : node.getThen()) {
          codegen(stmt);
        }

        System.out.printf("L%dend:\n", labelSeq);
        if (node.getEls() != null) {
          for (Node stmt : node.getEls().getThen()) {
            codegen(stmt);
          }
        }

        labelSeq++;
        return;
    }

    codegen(node.getLhs());
    codegen(node.getRhs());

    switch (node.getType()) {
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
