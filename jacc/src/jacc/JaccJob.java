// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package dev.travisbrown.jacc;

import java.io.Reader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import dev.travisbrown.jacc.compiler.Handler;
import dev.travisbrown.jacc.compiler.Position;
import dev.travisbrown.jacc.compiler.Diagnostic;
import dev.travisbrown.jacc.compiler.Failure;
import dev.travisbrown.jacc.compiler.Warning;
import dev.travisbrown.jacc.compiler.Phase;

import dev.travisbrown.jacc.grammar.Grammar;
import dev.travisbrown.jacc.grammar.Finitary;
import dev.travisbrown.jacc.grammar.LookaheadMachine;
import dev.travisbrown.jacc.grammar.Resolver;
import dev.travisbrown.jacc.grammar.Tables;
import dev.travisbrown.jacc.grammar.Parser;

/** Encapsulates the process of running a single job for the jacc
 *  parser generator, with some degree of independence from the
 *  actual user interface.
 */
public class JaccJob extends Phase {
    private Settings     settings;
    private JaccTables   tables;
    private JaccResolver resolver;
    private PrintWriter  out;
    private String inputFile;
    private GrammarDef grammarDef;

    public JaccJob(Handler handler, PrintWriter out, Settings settings) {
        super(handler);
        this.out      = out;
        this.settings = settings;
    }

    /** Return the settings for this job.
     */
    Settings getSettings() {
        return settings;
    }

    /** Return the tables for this job.
     */
    JaccTables getTables() {
        return tables;
    }

    /** Return the resolver for this job.
     */
    JaccResolver getResolver() {
        return resolver;
    }

    /** Parse a grammar file.
     */
    public void parseGrammarFile(String inputFile) {
        this.inputFile = inputFile;
    }

    /** Generate a machine and corresponding parse tables for the
     *  input grammar.
     */
    public void buildTables() {
        this.grammarDef = GrammarDefParser.parseFile(this.inputFile);

        Grammar grammar = grammarDef.getGrammar();
        grammarDef.updateSettings(settings);

        if (grammar==null || !allDeriveFinite(grammar)) {
            return;
        }

        LookaheadMachine machine = settings.makeMachine(grammar);

        resolver = new JaccResolver(machine);
        tables   = new JaccTables(machine, resolver);

        if (tables.getProdUnused()>0) {
            report(new Warning(tables.getProdUnused()
                               + " rules never reduced"));
        }

        if (resolver.getNumSRConflicts()>0 || resolver.getNumRRConflicts()>0) {
            report(new Warning("conflicts: "
                               + resolver.getNumSRConflicts()
                               + " shift/reduce, "
                               + resolver.getNumRRConflicts()
                               + " reduce/reduce"));
        }
    }

    /** Check that all nonterminals in the input grammar derive a finite
     *  string.
     */
    private boolean allDeriveFinite(Grammar grammar) {
        Finitary finitary  = grammar.getFinitary();
        boolean  allFinite = true;
        for (int nt=0; nt<grammar.getNumNTs(); nt++) {
            if (!finitary.at(nt)) {
                allFinite = false;
                report(new Failure("No finite strings can be derived for "
                                  + grammar.getNonterminal(nt)));
            }
        }
        return allFinite;
    }

    /** Parse and process a file containing error examples.
     */
    public void readErrorExamples(String inputFile) {
      System.out.println("Reading " + inputFile);
      try {
        Iterable<scala.Tuple2<String, int[]>> it = this.grammarDef.parseErrorExamples(
          new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(inputFile)))
        );

        for (scala.Tuple2<String, int[]> t : it) {
          errorExample(null, t._1(), t._2());
        }
      } catch (Exception e) {
        System.err.println(e);
      }
    }

    /** Process a sequence of input symbols that is expected to result
     *  in an error described by a given tag.
     */
    public void errorExample(Position pos, String tag, int[] syms) {
        Parser p = new Parser(tables, syms);
        int    s;
        do { s = p.step(); } while (s!=Parser.ACCEPT && s!=Parser.ERROR);
        if (s==Parser.ACCEPT) {
            report(new Warning(pos, "Example for \""
                                    + tag + "\" does not produce an error"));
        } else {
            Grammar grammar = tables.getMachine().getGrammar();
            int     sym     = p.getNextSymbol();

            if (grammar.isNonterminal(sym)) {  // maybe could use first set?
                report(new Warning(pos, "Example for \"" + tag
                           + "\" reaches an error at the nonterminal "
                           + grammar.getSymbol(sym)));
            } else {                           // use single terminal
                int state = p.getState();
//              out.println("Error in state " + state
//                          + " on terminal " + grammar.getSymbol(sym)
//                          + " indicates: " + tag);
                if (!tables.errorAt(state, sym)) {
                    // This shouldn't occur because the parser wouldn't
                    // have got stuck here if a shift or reduce had been
                    // indicated!
                    report(new Failure(pos,
                             "Error example results in internal error"));
                } else {
                    String tag1 = tables.errorSet(state, sym, tag);
                    if (tag1!=null) {
                        report(new Warning(pos,
                             "Multiple errors are possible in state " + state
                             + " on terminal " + grammar.getSymbol(sym)
                             + ":\n - " + tag1
                             +  "\n - " + tag));
                    }
                }
            }
        }
    }
}
