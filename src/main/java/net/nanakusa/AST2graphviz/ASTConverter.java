package net.nanakusa.AST2graphviz;

import net.nanakusa.compiler.Node;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ASTConverter {
  private static int nodeIdCounter; // ノードIDの一意性を管理
  private static Map<Node, Integer> nodeIds; // ノードとIDのマッピング

  public static void saveAsDotFile(Node root, String filename) {
    try (FileWriter writer = new FileWriter(filename)) {
      nodeIdCounter = 0; // 新しいASTごとにIDカウンターをリセット
      nodeIds = new HashMap<>(); // 新しいASTごとにマップをリセット
      writer.write(toDotFormat(root));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String toDotFormat(Node node) {
    StringBuilder sb = new StringBuilder();
    sb.append("digraph AST {\n");
    buildDot(sb, node);
    sb.append("}\n");
    return sb.toString();
  }

  private static void buildDot(StringBuilder sb, Node node) {
    if (node == null)
      return;

    // ノードに一度だけIDを割り当て、既存のIDがあれば再利用
    int currentId = nodeIds.computeIfAbsent(node, k -> nodeIdCounter++);
    String label = node.getType().toString().replace("ND_", "");
    sb.append("  node").append(currentId).append(" [label=\"").append(label).append("\"];\n");

    // 子ノードを再帰的に処理し、同じノードには同じIDを使用
    if (node.getLhs() != null) {
      int leftId = nodeIds.computeIfAbsent(node.getLhs(), k -> nodeIdCounter++);
      sb.append("  node").append(currentId).append(" -> node").append(leftId).append(";\n");
      buildDot(sb, node.getLhs());
    }

    if (node.getRhs() != null) {
      int rightId = nodeIds.computeIfAbsent(node.getRhs(), k -> nodeIdCounter++);
      sb.append("  node").append(currentId).append(" -> node").append(rightId).append(";\n");
      buildDot(sb, node.getRhs());
    }

    if (node.getCond() != null) {
      int condId = nodeIds.computeIfAbsent(node.getCond(), k -> nodeIdCounter++);
      sb.append("  node").append(currentId).append(" -> node").append(condId).append(" [label=\"cond\"];\n");
      buildDot(sb, node.getCond());
    }

    if (node.getEls() != null) {
      int elseId = nodeIds.computeIfAbsent(node.getEls(), k -> nodeIdCounter++);
      sb.append("  node").append(currentId).append(" -> node").append(elseId).append(" [label=\"else\"];\n");
      buildDot(sb, node.getEls());
    }

    if (node.getThen() != null) {
      for (Node thenNode : node.getThen()) {
        int thenId = nodeIds.computeIfAbsent(thenNode, k -> nodeIdCounter++);
        sb.append("  node").append(currentId).append(" -> node").append(thenId).append(" [label=\"then\"];\n");
        buildDot(sb, thenNode);
      }
    }

    if (node.getInit() != null) {
      int initId = nodeIds.computeIfAbsent(node.getInit(), k -> nodeIdCounter++);
      sb.append("  node").append(currentId).append(" -> node").append(initId).append(" [label=\"init\"];\n");
      buildDot(sb, node.getInit());
    }

    if (node.getInc() != null) {
      int incId = nodeIds.computeIfAbsent(node.getInc(), k -> nodeIdCounter++);
      sb.append("  node").append(currentId).append(" -> node").append(incId).append(" [label=\"inc\"];\n");
      buildDot(sb, node.getInc());
    }
  }
}
