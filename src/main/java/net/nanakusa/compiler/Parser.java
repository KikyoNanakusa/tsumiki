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

  public static List<Node> parse(List<Token> token) {
    Parser.token = token;
    return program();
  }

  // function*
  private static List<Node> program() {

    while (!Tokenizer.atEof(token)) {
      code.add(function());
    }

    return code;
  }

  // "fn" ident "(" ")" "{" stmt* "}"
  private static Node function() {
    Node node = new Node(ND_TYPE.ND_FUNC);
    if (Tokenizer.consumeToken(token, "fn")) {
      String fn_name = Tokenizer.consumeIdent(token);
      Tokenizer.expectToken(token, "(");      

      List<LVar> args = new ArrayList<>();
      if (Tokenizer.isIdent(token)) {
        LVar arg = new LVar(Tokenizer.consumeIdent(token), args.size());
        args.add(arg);
        while (Tokenizer.consumeToken(token, ",")) {
          arg = new LVar(Tokenizer.consumeIdent(token), args.size());
          args.add(arg);
        }

        node.setArgs(args);
      }

      Tokenizer.expectToken(token, ")");
      Tokenizer.expectToken(token, "{");
      List<Node> stmts = new ArrayList<>();
      while (!Tokenizer.consumeToken(token, "}")) {
        stmts.add(stmt());
      }

      node.setName(fn_name);
      node.setStmts(stmts);
      node.setLocals(locals);
      locals = new ArrayList<>();

      return node;
    } else {
      throw new Error("Expected function declaration");
    }
  }

  // expr ";" | return expr ";" | if "(" expr "){" stmt* "}"
  // | if "(" expr "){" stmt* "}" else "{" stmt* "}"
  // | while "(" expr "){" stmt* "}"
  // | for "(" expr ";" expr ";" expr "){" stmt* "}"
  // | return expr ";"
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
      Tokenizer.expectToken(token, ";");
      return node;
    } 

    node = expr();
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
    if (Tokenizer.consumeToken(token, "[")) {
      int size = Tokenizer.expectNumber(token);
      Node assignNode = new Node(ND_TYPE.ND_ASSIGN);
      assignNode.setLhs(node);
    }

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

  // \(expr\)? num | ident "(" ")"
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
      Token ident_tok = token.get(0);
      token.remove(0);

      // function call
      if (Tokenizer.consumeToken(token, "(")) {
        // check if the function is defined
        for (Node func: code) {
          if (func.getName().equals(ident_tok.getStr())) {
            Node node = new Node(ND_TYPE.ND_CALL);
            node.setName(ident_tok.getStr());

            // parse arguments
            if (func.getArgs() != null) {
              List<Node> argInput = new ArrayList<>();
              argInput.add(expr());
              
              for (int i = 1; i < func.getArgs().size(); i++) {
                Tokenizer.consumeToken(token, ",");
                argInput.add(expr());
              }

              node.setArgExpr(argInput);
            }

            Tokenizer.expectToken(token, ")");

            return node;
          }
        }

        throw new Error("Function not found: " + ident_tok.getStr());
      }

      Node node = new Node(ND_TYPE.ND_LVAR);
      node.setName(ident_tok.getStr());

      LVar lvar = findLVar(ident_tok.getStr());
      if (lvar != null) {
        node.setOffset(lvar.getOffset());
      } else {
        lvar = new LVar(ident_tok.getStr(), locals.size());
        locals.add(lvar);
        node.setOffset(lvar.getOffset());
      }

      return node;
    }

    throw new Error("Unexpected token: " + token.get(0).getStr());
  }

}
