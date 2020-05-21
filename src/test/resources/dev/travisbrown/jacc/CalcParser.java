// Output created by jacc


  abstract class Expr {
    abstract int eval();
  }

  class IntExpr extends Expr {
    private int value;
    IntExpr(int value) { this.value = value; }
    int eval() { return value; }
  }

  abstract class BinExpr extends Expr {
    protected Expr left, right;
    BinExpr(Expr left, Expr right) {
      this.left = left;  this.right = right;
    }
  }

  class AddExpr extends BinExpr {
    AddExpr(Expr left, Expr right) { super(left, right); }
    int eval() { return left.eval() + right.eval(); }
  }

  class SubExpr extends BinExpr {
    SubExpr(Expr left, Expr right) { super(left, right); }
    int eval() { return left.eval() - right.eval(); }
  }

  class MulExpr extends BinExpr {
    MulExpr(Expr left, Expr right) { super(left, right); }
    int eval() { return left.eval() * right.eval(); }
  }

  class DivExpr extends BinExpr {
    DivExpr(Expr left, Expr right) { super(left, right); }
    int eval() { return left.eval() / right.eval(); }
  }

  class CalcLexer implements CalcTokens {
    private int c = ' ';

    /** Read a single input character from standard input.
     */
    private void nextChar() {
      if (c>=0) {
        try {
          c = System.in.read();
        } catch (Exception e) {
          c = (-1);
        }
      }
    }

    private int     token;
    private IntExpr yylval;

    /** Read the next token and return the
     *  corresponding integer code.
     */
    int nextToken() {
      for (;;) {
        // Skip whitespace
        while (c==' ' || c=='\n' || c=='\t' || c=='\r') {
          nextChar();
        }
        if (c<0) {
          return (token=ENDINPUT);
        }
        switch (c) {
          case '+' : nextChar();
                     return token='+';
          case '-' : nextChar();
                     return token='-';
          case '*' : nextChar();
                     return token='*';
          case '/' : nextChar();
                     return token='/';
          case '(' : nextChar();
                     return token='(';
          case ')' : nextChar();
                     return token=')';
          case ';' : nextChar();
                     return token=';';
          default  : if (Character.isDigit((char)c)) {
                       int n = 0;
                         do {
                           n = 10*n + (c - '0');
                           nextChar();
                         } while (Character.isDigit((char)c));
                         yylval = new IntExpr(n);
                         return token=INTEGER;
                       } else {
                         Main.error("Illegal character "+c);
                         nextChar();
                       }
        }
      }
    }

    /** Return the token code for the current lexeme.
     */
    int getToken() {
      return token;
    }

    /** Return the semantic value for the current lexeme.
     */
    IntExpr getSemantic() {
      return yylval;
    }
  }

  class Main {
    public static void main(String[] args) {
      CalcLexer  lexer  = new CalcLexer();
      lexer.nextToken();
      CalcParser parser = new CalcParser(lexer);
      parser.parse();
    }

    static void error(String msg) {
      System.out.println("ERROR: " + msg);
      System.exit(1);
    }
  }

class CalcParser implements CalcTokens {
    private int yyss = 100;
    private int yytok;
    private int yysp = 0;
    private int[] yyst;
    protected int yyerrno = (-1);
    private Expr[] yysv;
    private Expr yyrv;

    public boolean parse() {
        int yyn = 0;
        yysp = 0;
        yyst = new int[yyss];
        yyerrno = (-1);
        yysv = new Expr[yyss];
        yytok = (lexer.getToken()
                 );
    loop:
        for (;;) {
            switch (yyn) {
                case 0:
                    yyst[yysp] = 0;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 17:
                    yyn = yys0();
                    continue;

                case 1:
                    yyst[yysp] = 1;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 18:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = 34;
                            continue;
                        case ';':
                            yyn = 5;
                            continue;
                    }
                    yyn = 37;
                    continue;

                case 2:
                    yyst[yysp] = 2;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 19:
                    yyn = yys2();
                    continue;

                case 3:
                    yyst[yysp] = 3;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 20:
                    switch (yytok) {
                        case '(':
                        case error:
                        case INTEGER:
                            yyn = 37;
                            continue;
                    }
                    yyn = yyr8();
                    continue;

                case 4:
                    yyst[yysp] = 4;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 21:
                    switch (yytok) {
                        case ')':
                            yyn = yyerr(5, 37);
                            continue;
                        case INTEGER:
                            yyn = 3;
                            continue;
                        case '(':
                            yyn = 4;
                            continue;
                    }
                    yyn = 37;
                    continue;

                case 5:
                    yyst[yysp] = 5;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 22:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = yyerr(4, 37);
                            continue;
                        case INTEGER:
                            yyn = 3;
                            continue;
                        case '(':
                            yyn = 4;
                            continue;
                    }
                    yyn = 37;
                    continue;

                case 6:
                    yyst[yysp] = 6;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 23:
                    switch (yytok) {
                        case ENDINPUT:
                        case ')':
                            yyn = yyerr(3, 37);
                            continue;
                        case INTEGER:
                            yyn = 3;
                            continue;
                        case '(':
                            yyn = 4;
                            continue;
                    }
                    yyn = 37;
                    continue;

                case 7:
                    yyst[yysp] = 7;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 24:
                    switch (yytok) {
                        case '+':
                        case ENDINPUT:
                        case ')':
                            yyn = yyerr(3, 37);
                            continue;
                        case INTEGER:
                            yyn = 3;
                            continue;
                        case '(':
                            yyn = 4;
                            continue;
                    }
                    yyn = 37;
                    continue;

                case 8:
                    yyst[yysp] = 8;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 25:
                    switch (yytok) {
                        case ENDINPUT:
                        case ')':
                            yyn = yyerr(3, 37);
                            continue;
                        case INTEGER:
                            yyn = 3;
                            continue;
                        case '(':
                            yyn = 4;
                            continue;
                    }
                    yyn = 37;
                    continue;

                case 9:
                    yyst[yysp] = 9;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 26:
                    switch (yytok) {
                        case ENDINPUT:
                        case ')':
                            yyn = yyerr(3, 37);
                            continue;
                        case INTEGER:
                            yyn = 3;
                            continue;
                        case '(':
                            yyn = 4;
                            continue;
                    }
                    yyn = 37;
                    continue;

                case 10:
                    yyst[yysp] = 10;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 27:
                    switch (yytok) {
                        case '*':
                            yyn = 6;
                            continue;
                        case '+':
                            yyn = 7;
                            continue;
                        case '-':
                            yyn = 8;
                            continue;
                        case '/':
                            yyn = 9;
                            continue;
                        case ')':
                            yyn = 16;
                            continue;
                    }
                    yyn = 37;
                    continue;

                case 11:
                    yyst[yysp] = 11;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 28:
                    yyn = yys11();
                    continue;

                case 12:
                    yyst[yysp] = 12;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 29:
                    switch (yytok) {
                        case '(':
                        case error:
                        case INTEGER:
                            yyn = 37;
                            continue;
                    }
                    yyn = yyr5();
                    continue;

                case 13:
                    yyst[yysp] = 13;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 30:
                    switch (yytok) {
                        case '(':
                        case INTEGER:
                        case error:
                            yyn = 37;
                            continue;
                        case '*':
                            yyn = 6;
                            continue;
                        case '/':
                            yyn = 9;
                            continue;
                    }
                    yyn = yyr3();
                    continue;

                case 14:
                    yyst[yysp] = 14;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 31:
                    switch (yytok) {
                        case '(':
                        case INTEGER:
                        case error:
                            yyn = 37;
                            continue;
                        case '*':
                            yyn = 6;
                            continue;
                        case '/':
                            yyn = 9;
                            continue;
                    }
                    yyn = yyr4();
                    continue;

                case 15:
                    yyst[yysp] = 15;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 32:
                    switch (yytok) {
                        case '(':
                        case error:
                        case INTEGER:
                            yyn = 37;
                            continue;
                    }
                    yyn = yyr6();
                    continue;

                case 16:
                    yyst[yysp] = 16;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 33:
                    switch (yytok) {
                        case '(':
                        case error:
                        case INTEGER:
                            yyn = 37;
                            continue;
                    }
                    yyn = yyr7();
                    continue;

                case 34:
                    return true;
                case 35:
                    yyerror("stack overflow");
                case 36:
                    return false;
                case 37:
                    yyerror("syntax error");
                    return false;
            }
        }
    }

    private void yyexpand() {
        int[] newyyst = new int[2*yyst.length];
        Expr[] newyysv = new Expr[2*yyst.length];
        System.arraycopy(yyst, 0, newyyst, 0, yyst.length);
        System.arraycopy(yysv, 0, newyysv, 0, yyst.length);
        yyst = newyyst;
        yysv = newyysv;
    }

    private int yys0() {
        switch (yytok) {
            case ENDINPUT:
            case error:
            case ')':
                return 37;
            case ';':
                return yyerr(6, 37);
            case INTEGER:
                return 3;
            case '(':
                return 4;
        }
        return yyerr(0, 37);
    }

    private int yys2() {
        switch (yytok) {
            case ')':
                return yyerr(1, 37);
            case '(':
                return yyerr(2, 37);
            case '*':
                return 6;
            case '+':
                return 7;
            case '-':
                return 8;
            case '/':
                return 9;
            case ENDINPUT:
            case ';':
                return yyr2();
        }
        return 37;
    }

    private int yys11() {
        switch (yytok) {
            case '*':
                return 6;
            case '+':
                return 7;
            case '-':
                return 8;
            case '/':
                return 9;
            case ENDINPUT:
            case ';':
                return yyr1();
        }
        return 37;
    }

    private int yyr1() { // prog : prog ';' expr
        { System.out.println(yysv[yysp-1].eval()); }
        yysv[yysp-=3] = yyrv;
        return 1;
    }

    private int yyr2() { // prog : expr
        { System.out.println(yysv[yysp-1].eval()); }
        yysv[yysp-=1] = yyrv;
        return 1;
    }

    private int yyr3() { // expr : expr '+' expr
        { yyrv = new AddExpr(yysv[yysp-3], yysv[yysp-1]); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr4() { // expr : expr '-' expr
        { yyrv = new SubExpr(yysv[yysp-3], yysv[yysp-1]); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr5() { // expr : expr '*' expr
        { yyrv = new MulExpr(yysv[yysp-3], yysv[yysp-1]); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr6() { // expr : expr '/' expr
        { yyrv = new DivExpr(yysv[yysp-3], yysv[yysp-1]); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr7() { // expr : '(' expr ')'
        { yyrv = yysv[yysp-2]; }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr8() { // expr : INTEGER
        { yyrv = yysv[yysp-1]; }
        yysv[yysp-=1] = yyrv;
        return yypexpr();
    }

    private int yypexpr() {
        switch (yyst[yysp-1]) {
            case 8: return 14;
            case 7: return 13;
            case 6: return 12;
            case 5: return 11;
            case 4: return 10;
            case 0: return 2;
            default: return 15;
        }
    }

    private int yyerr(int e, int n) {
        yyerrno = e;
        return n;
    }
    protected String[] yyerrmsgs = {
        "left operand is missing",
        "unexpected closing parenthesis",
        "unexpected opening parenthesis",
        "right operand is missing",
        "unnecessary semicolon after last expression (or missing expression)",
        "empty parentheses",
        "missing expression"
    };

  private CalcLexer lexer;

  CalcParser(CalcLexer lexer) { this.lexer = lexer; }

  private void yyerror(String msg) {
    Main.error(yyerrno<0 ? msg : yyerrmsgs[yyerrno]);
  }


}
