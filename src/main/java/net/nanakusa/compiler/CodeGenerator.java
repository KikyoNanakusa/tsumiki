public class CodeGenerator {
  public static void codegen(Node node) {
    switch (node.getType()) {
      case ND_TYPE.ND_MUL:
        codegen(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("mul\n");
        return;
      case ND_TYPE.ND_DIV:
        codegen(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("div\n");
        return;
      case ND_TYPE.ND_ADD:
        codegen(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("add\n");
        return;
      case ND_TYPE.ND_SUB:
        codegen(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("sub\n");
        return;
      case ND_TYPE.ND_NUM:
        System.out.printf("push %d\n", node.getVal());
        return;
      case ND_TYPE.ND_LESS:
        codegen(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("lt\n");
        return;
      case ND_TYPE.ND_LEQ:
        codegen(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("leq\n");
        return;
      case ND_TYPE.ND_EQ:
        codegen(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("eq\n");
        return;
      case ND_TYPE.ND_NEQ:
        codegen(node.getLhs());
        codegen(node.getRhs());
        System.out.printf("neq\n");
        return;
    }
  }
}
