// Output created by jacc 2.1.0

public class Calc implements CalcTokens {
    private int yyss = 100;
    private int yytok;
    private int yysp = 0;
    private int[] yyst;
    protected int yyerrno = (-1);
    private int[] yysv;
    private int yyrv;

    public boolean parse() {
        int yyn = 0;
        yysp = 0;
        yyst = new int[yyss];
        yysv = new int[yyss];
        yytok = (token
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
                    switch (yytok) {
                        case INTEGER:
                            yyn = 3;
                            continue;
                        case '(':
                            yyn = 4;
                            continue;
                    }
                    yyn = 37;
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
                    yysv[yysp] = (yylval
                                 );
                    yytok = (yylex()
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
                    yysv[yysp] = (yylval
                                 );
                    yytok = (yylex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 21:
                    switch (yytok) {
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
                    yysv[yysp] = (yylval
                                 );
                    yytok = (yylex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 22:
                    switch (yytok) {
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
                    yysv[yysp] = (yylval
                                 );
                    yytok = (yylex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 23:
                    switch (yytok) {
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
                    yysv[yysp] = (yylval
                                 );
                    yytok = (yylex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 24:
                    switch (yytok) {
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
                    yysv[yysp] = (yylval
                                 );
                    yytok = (yylex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 25:
                    switch (yytok) {
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
                    yysv[yysp] = (yylval
                                 );
                    yytok = (yylex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 26:
                    switch (yytok) {
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
                    yysv[yysp] = (yylval
                                 );
                    yytok = (yylex()
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

    protected void yyexpand() {
        int[] newyyst = new int[2*yyst.length];
        int[] newyysv = new int[2*yyst.length];
        for (int i=0; i<yyst.length; i++) {
            newyyst[i] = yyst[i];
            newyysv[i] = yysv[i];
        }
        yyst = newyyst;
        yysv = newyysv;
    }

    private int yys2() {
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
        { System.out.println(yysv[yysp-1]); }
        yysv[yysp-=3] = yyrv;
        return 1;
    }

    private int yyr2() { // prog : expr
        { System.out.println(yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return 1;
    }

    private int yyr3() { // expr : expr '+' expr
        { yyrv = yysv[yysp-3] + yysv[yysp-1]; }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr4() { // expr : expr '-' expr
        { yyrv = yysv[yysp-3] - yysv[yysp-1]; }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr5() { // expr : expr '*' expr
        { yyrv = yysv[yysp-3] * yysv[yysp-1]; }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr6() { // expr : expr '/' expr
        { yyrv = yysv[yysp-3] / yysv[yysp-1]; }
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

    protected String[] yyerrmsgs = {
    };


  private void yyerror(String msg) {
    System.out.println("ERROR: " + msg);
    System.exit(1);
  }

  private int c;

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

  int token;
  int yylval;

  /** Read the next token and return the
   *  corresponding integer code.
   */
  int yylex() {
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
                       yylval = n;
                       return token=INTEGER;
                     } else {
                       yyerror("Illegal character "+c);
                       nextChar();
                     }
      }
    }
  }

  public static void main(String[] args) {
    Calc calc = new Calc();
    calc.nextChar(); // prime the character input stream
    calc.yylex();    // prime the token input stream
    calc.parse();    // parse the input
  }

}
