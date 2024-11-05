package net.nanakusa.compiler;

import java.util.ArrayList;
import java.util.List;

class Parser {
  static List<Token> token;
  static List<LVar> locals = new ArrayList<>();
  static List<Node> code = new ArrayList<>();

  public static LVar findLVar(String name) {
    for (LVar lvar : locals) {
      if (lvar.getName().equals(name)) {
        return lvar;
      }
    }
    return null;
  }

  public static List<Node> parse() {
    return program();
  }

  // stmt*
  private static List<Node> program() {
    while (!Tokenizer.atEof(token)) {
      Node node = stmt();
      code.add(node);
    }

    return code;
  }

  // expr ";" | return expr ";" | if "(" expr "){" stmt* "}"
  // | if "(" expr "){" stmt* "}" else "{" stmt* "}"
  private static Node stmt() {
    Node node;

    if (Tokenizer.consumeToken(token, "if")) {
      node = new Node(ND_TYPE.ND_IF);
      Tokenizer.expectToken(token, "(");
      Node cond = expr();
      node.setCond(cond);
      Tokenizer.expectToken(token, ")");
      Tokenizer.expectToken(token, "{");
      List<Node> then = new ArrayList<>();
      while (!Tokenizer.consumeToken(token, "}")) {
        then.add(stmt());
      }
      node.setThen(then);

      then = new ArrayList<>();
      if (Tokenizer.consumeToken(token, "else")) {
        Tokenizer.expectToken(token, "{");
        Node els = new Node(ND_TYPE.ND_ELSE);
        while (!Tokenizer.consumeToken(token, "}")) {
          then.add(stmt());
        }
        els.setThen(then);
        node.setEls(els);
      }

      return node;
    }

    if (Tokenizer.consumeToken(token, "while")) {
      node = new Node(ND_TYPE.ND_WHILE);
      Tokenizer.expectToken(token, "(");
      Node cond = expr();
      node.setCond(cond);
      Tokenizer.expectToken(token, ")");
      Tokenizer.expectToken(token, "{");
      List<Node> then = new ArrayList<>();
      while (!Tokenizer.consumeToken(token, "}")) {
        then.add(stmt());
      }
      node.setThen(then);
      return node;
    }

    if (Tokenizer.consumeToken(token, "for")) {
      node = new Node(ND_TYPE.ND_FOR);
      Tokenizer.expectToken(token, "(");
      Node init = expr();
      Tokenizer.expectToken(token, ";");
      Node cond = expr();
      Tokenizer.expectToken(token, ";");
      Node inc = expr();
      Tokenizer.expectToken(token, ")");
      Tokenizer.expectToken(token, "{");
      List<Node> then = new ArrayList<>();
      while (!Tokenizer.consumeToken(token, "}")) {
        then.add(stmt());
      }
      node.setInit(init);
      node.setCond(cond);
      node.setInc(inc);
      node.setThen(then);
      return node;
    }

    if (Tokenizer.consumeToken(token, "return")) {
      node = new Node(ND_TYPE.ND_RETURN);
      node.setLhs(expr());
    } else {
      node = expr();
    }

    Tokenizer.expectToken(token, ";");

    return node;
  }

  // expr = equality
  private static Node expr() {
    Node node = assign();
    return node;
  }

  private static Node assign() {
    Node node = equality();
    if (Tokenizer.consumeToken(token, "=")) {
      Node newNode = assign();
      Node assignNode = new Node(ND_TYPE.ND_ASSIGN);
      assignNode.setLhs(node);
      assignNode.setRhs(newNode);
      return assignNode;
    }

    return node;
  }

  // equality (== | != equality)*
  private static Node equality() {
    Node node = relational();
    if (Tokenizer.consumeToken(token, "==")) {
      Node newNode = relational();
      Node eqNode = new Node(ND_TYPE.ND_EQ);
      eqNode.setLhs(node);
      eqNode.setRhs(newNode);
      return eqNode;
    } else if (Tokenizer.consumeToken(token, "!=")) {
      Node newNode = relational();
      Node eqNode = new Node(ND_TYPE.ND_NEQ);
      eqNode.setLhs(node);
      eqNode.setRhs(newNode);
      return eqNode;
    } else {
      return node;
    }
  }

  // add (< add | <= add | > add | >= add)*
  private static Node relational() {
    Node node = add();
    if (Tokenizer.consumeToken(token, "<")) {
      Node rhs = add();
      Node lessNode = new Node(ND_TYPE.ND_LESS);
      lessNode.setLhs(node);
      lessNode.setRhs(rhs);
      return lessNode;
    } else if (Tokenizer.consumeToken(token, "<=")) {
      Node rhs = add();
      Node leqNode = new Node(ND_TYPE.ND_LEQ);
      leqNode.setLhs(node);
      leqNode.setRhs(rhs);
      return leqNode;
    } else if (Tokenizer.consumeToken(token, ">")) {
      Node lhs = add();
      Node lessNode = new Node(ND_TYPE.ND_LESS);
      lessNode.setLhs(lhs);
      lessNode.setRhs(node);
      return lessNode;
    } else if (Tokenizer.consumeToken(token, ">=")) {
      Node lhs = add();
      Node leqNode = new Node(ND_TYPE.ND_LEQ);
      leqNode.setLhs(lhs);
      leqNode.setRhs(node);
      return leqNode;
    } else {
      return node;
    }
  }

  // mul (+ mul | - mul )*
  private static Node add() {
    Node node = mul();

    while (true) {
      if (token.size() == 0) {
        return node;
      }

      if (token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals("+")) {
        token.remove(0);
        Node newNode = new Node(ND_TYPE.ND_ADD);
        newNode.setLhs(node);
        newNode.setRhs(mul());
        node = newNode;
      } else if (token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals("-")) {
        token.remove(0);
        Node newNode = new Node(ND_TYPE.ND_SUB);
        newNode.setLhs(node);
        newNode.setRhs(mul());
        node = newNode;
      } else {
        return node;
      }
    }
  }

  // unary (\* unary | / unary)*
  private static Node mul() {
    Node node = unary();

    while (true) {
      if (token.size() == 0) {
        return node;
      }

      if (token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals("*")) {
        token.remove(0);
        Node newNode = new Node(ND_TYPE.ND_MUL);
        newNode.setLhs(node);
        newNode.setRhs(unary());
        node = newNode;
      } else if (token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals("/")) {
        token.remove(0);
        Node newNode = new Node(ND_TYPE.ND_DIV);
        newNode.setLhs(node);
        newNode.setRhs(unary());
        node = newNode;
      } else {
        return node;
      }
    }
  }

  // (+ | -)? primary
  private static Node unary() {
    if (token.size() == 0) {
      throw new Error("Unexpected end of input");
    }

    if (token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals("+")) {
      token.remove(0);
      return primary();
    } else if (token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals("-")) {
      token.remove(0);

      Node node = new Node(ND_TYPE.ND_SUB);
      node.setLhs(new Node(ND_TYPE.ND_NUM, 0));
      node.setRhs(primary());

      return node;
    }

    return primary();
  }

  // \(expr\)? num
  private static Node primary() {
    if (token.size() == 0) {
      throw new Error("Unexpected end of input");
    }

    if (token.get(0).getType() == TK_TYPE.TK_RESERVED && token.get(0).getStr().equals("(")) {
      token.remove(0);
      Node node = expr();

      if (!(token.get(0).getType() == TK_TYPE.TK_RESERVED) || !(token.get(0).getStr().equals(")"))) {
        throw new Error(") not found");
      }
      token.remove(0);
      return node;
    }

    if (token.get(0).getType() == TK_TYPE.TK_NUM) {
      Token tok = token.get(0);
      token.remove(0);

      Node node = new Node(ND_TYPE.ND_NUM, Integer.parseInt(tok.getStr()));
      return node;
    }

    if (token.get(0).getType() == TK_TYPE.TK_IDENT) {
      Token tok = token.get(0);
      token.remove(0);
      Node node = new Node(ND_TYPE.ND_LVAR);
      node.setName(tok.getStr());

      LVar lvar = findLVar(tok.getStr());
      if (lvar != null) {
        node.setOffset(lvar.getOffset());
      } else {
        lvar = new LVar(tok.getStr(), locals.size());
        locals.add(lvar);
        node.setOffset(lvar.getOffset());
      }

      return node;
    }

    throw new Error("Unexpected token: " + token.get(0).getStr());
  }

}
