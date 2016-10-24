package com.sonarsource.rules.java.checks;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.AssignmentExpressionTree;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.BreakStatementTree;
import org.sonar.plugins.java.api.tree.CaseGroupTree;
import org.sonar.plugins.java.api.tree.CatchTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ContinueStatementTree;
import org.sonar.plugins.java.api.tree.DoWhileStatementTree;
import org.sonar.plugins.java.api.tree.ExpressionStatementTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.ForEachStatement;
import org.sonar.plugins.java.api.tree.ForStatementTree;
import org.sonar.plugins.java.api.tree.IfStatementTree;
import org.sonar.plugins.java.api.tree.LabeledStatementTree;
import org.sonar.plugins.java.api.tree.LambdaExpressionTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.NewClassTree;
import org.sonar.plugins.java.api.tree.StatementTree;
import org.sonar.plugins.java.api.tree.SwitchStatementTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;
import org.sonar.plugins.java.api.tree.TryStatementTree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.plugins.java.api.tree.WhileStatementTree;

import java.util.ArrayList;
import java.util.List;

import static org.sonar.plugins.java.api.tree.Tree.Kind.*;


@Rule(
      key = "CognitiveComplexity",
      name = "Cognitive Complexity of methods should not be too high",
      description = "[Something profound and moving here...]",
      priority = Priority.CRITICAL,
      tags = ("brainoverload"))
public class CognitiveComplexityCheck extends IssuableSubscriptionVisitor{

  private static final int DEFAULT_MAX = 10;

  @RuleProperty(
          key = "Threshold",
          description = "The maximum authorized complexity.",
          defaultValue = "" + DEFAULT_MAX)
  private int max = DEFAULT_MAX;


  private List<Kind> kindsAffectedByNesting = ImmutableList.<Kind>builder()
          .add(IF_STATEMENT)
          .add(FOR_STATEMENT)
          .add(FOR_EACH_STATEMENT)
          .add(DO_STATEMENT)
          .add(WHILE_STATEMENT)
.add(SWITCH_STATEMENT)
//          .add(TERNARY_OP)
          .build();


  @Override
  public List<Kind> nodesToVisit() {
    return ImmutableList.of(METHOD, CONSTRUCTOR);
  }

  @Override
  public void visitNode(Tree tree) {

    MethodTree method = (MethodTree) tree;
    if (method.block() == null || ((ClassTree)method.parent()).simpleName() == null) {
      return;
    }

    List<JavaFileScannerContext.Location> flow = new ArrayList<>();
    int total = countComplexity(method.block().body(), flow, 0);

    if (total > max) {
      reportIssue(
              method.simpleName(),
              "The Cognitive Complexity of this method \"" + method.simpleName().name() + "\" is " + total +
                      " which is greater than " + max + " authorized.",
              flow,
              total - max);
    }

  }

  private int countComplexity(List<StatementTree> statements, List<JavaFileScannerContext.Location> flow, int nestingLevel) {
    if (statements == null || statements.isEmpty()) {
      return 0;
    }

    int total = 0;

    for (StatementTree st : statements) {

      Tree tree = drillIn(st);

      if (kindsAffectedByNesting.contains(tree.kind())) {
        int hit = 1 + nestingLevel + countExtraConditions(tree);

        addSecondaryLocation(flow, tree, hit, nestingLevel);

        total += hit;
        total += countIfElseChains(tree, flow, nestingLevel);

      } else {
        total += countBreakAndContinue(tree, flow);
        total += countTryChains(tree, flow, nestingLevel);
        total += countAnonymousClasses(tree, flow, nestingLevel);
      }

      if (tree.is(TRY_STATEMENT)) {
        total += countComplexity(getChildren(tree), flow, nestingLevel);
      } else {
        total += countComplexity(getChildren(tree), flow, nestingLevel + 1);
      }

    }

    return total;
  }

  private void addSecondaryLocation(List<JavaFileScannerContext.Location> flow, Tree st, int hit,  int nestingLevel) {

    Tree tree = st;

    switch (st.kind()) {
      case IF_STATEMENT:
        tree = ((IfStatementTree) st).ifKeyword();
        break;
      case SWITCH_STATEMENT:
        tree = ((SwitchStatementTree) st).switchKeyword();
        break;
      case WHILE_STATEMENT:
        tree = ((WhileStatementTree) st).whileKeyword();
        break;
      case DO_STATEMENT:
        tree = ((DoWhileStatementTree) st).doKeyword();
        break;
      case FOR_EACH_STATEMENT:
        tree = ((ForEachStatement) st).forKeyword();
        break;
      case FOR_STATEMENT:
        tree = ((ForStatementTree) st).forKeyword();
        break;
      case LAMBDA_EXPRESSION:
        tree = ((LambdaExpressionTree) st).arrowToken();
        break;
    }

    if (nestingLevel > 0) {
      flow.add(new JavaFileScannerContext.Location("+" + hit +" (incl " + nestingLevel + " for nesting)", tree));
    } else {
      flow.add(new JavaFileScannerContext.Location("+" + hit, tree));
    }
  }


  private int countAnonymousClasses(Tree st, List<JavaFileScannerContext.Location> flow, int nestingLevel) {
    int total = 0;

    if (st.is(Kind.CLASS) && ! ((ClassTree)st).members().isEmpty()) {

      for (Tree member : ((ClassTree) st).members()) {
        if (member.is(Kind.METHOD)) {
          total += countComplexity(((MethodTree) member).block().body(), flow, nestingLevel+1);
        }
      }
    }

    return total;
  }

  private int countIfElseChains(Tree st, List<JavaFileScannerContext.Location> flow, int nestingLevel) {
    int total = 0;

    if (! st.is(Kind.IF_STATEMENT)) {
      return total;
    }

    if (((IfStatementTree) st).elseStatement() == null) {
      return total;
    }

    StatementTree elseStatement = ((IfStatementTree) st).elseStatement();

    total++;
    addSecondaryLocation(flow, elseStatement, total, nestingLevel);

    if (elseStatement.is(Kind.IF_STATEMENT)) {
      total += countConditions(((IfStatementTree) elseStatement).condition() );
      total += countIfElseChains(elseStatement, flow, nestingLevel);
    }

    List<StatementTree> children = getChildren(elseStatement);
    total += countComplexity(children, flow, nestingLevel+1);

    return total;
  }

  private int countExtraConditions(Tree tree) {
    switch (tree.kind()) {
      case WHILE_STATEMENT:
        return countConditions(((WhileStatementTree) tree).condition());
      case FOR_STATEMENT:
        return countConditions(((ForStatementTree) tree).condition());
      case DO_STATEMENT:
        return countConditions(((DoWhileStatementTree) tree).condition());
      case IF_STATEMENT:
        return countConditions(((IfStatementTree) tree).condition());
      default:
        return 0;
    }

  }

  private int countConditions(ExpressionTree expressionTree) {
    int total = 0;

    ExpressionTree tree = expressionTree;
    if (expressionTree.is(ASSIGNMENT)) {
      tree = ((AssignmentExpressionTree) tree).variable();
    }

    if (tree.is(CONDITIONAL_AND) || tree.is(CONDITIONAL_OR)) {
      total++;

      for (BinaryExpressionTree binTree = (BinaryExpressionTree) tree;
           binTree.leftOperand().is(CONDITIONAL_AND) || binTree.leftOperand().is(CONDITIONAL_OR);
           binTree = (BinaryExpressionTree) binTree.leftOperand()) {
        total++;
      }
    }

    return total;
  }

  private int countTryChains(Tree st, List<JavaFileScannerContext.Location> flow, int nestingLevel) {
    int total = 0;

    if (st.is(Kind.TRY_STATEMENT)) {
      TryStatementTree tryStatment = (TryStatementTree) st;

      List<CatchTree> catches = tryStatment.catches();
      for (CatchTree catchTree : catches) {
        total += nestingLevel;
        addSecondaryLocation(flow, catchTree, total, nestingLevel);
        total += countComplexity(catchTree.block().body(), flow, nestingLevel +1);
      }

      if (tryStatment.finallyBlock() != null && tryStatment.finallyBlock().body() != null) {
        total += countComplexity(tryStatment.finallyBlock().body(), flow, nestingLevel);
      }
    }

    return total;
  }

  private int countBreakAndContinue(Tree st, List<JavaFileScannerContext.Location> flow) {
    if (st.is(BREAK_STATEMENT) && ((BreakStatementTree) st).label() != null) {
      flow.add(new JavaFileScannerContext.Location("+1", st));
      return 1;

    } else if (st.is(CONTINUE_STATEMENT) && ((ContinueStatementTree) st).label() != null) {
      flow.add(new JavaFileScannerContext.Location("+1", st));
      return 1;

    }

    return 0;
  }

  private Tree drillIn(StatementTree st) {
    switch (st.kind()) {
      case LABELED_STATEMENT:
        return ((LabeledStatementTree)st).statement();
      case VARIABLE:
        VariableTree vt = (VariableTree)st;
        if (vt.initializer() != null) {
          return vt.initializer();
        }
        break;
      case EXPRESSION_STATEMENT:
        if (((ExpressionStatementTree)st).expression() != null && ((ExpressionStatementTree)st).expression().is(Kind.ASSIGNMENT)) {

          AssignmentExpressionTree aet = (AssignmentExpressionTree)((ExpressionStatementTree) st).expression();
          if (aet.expression() != null && aet.expression().is(Kind.NEW_CLASS) && ((NewClassTree) aet.expression()).classBody() != null) {
            return ((NewClassTree) aet.expression()).classBody();
          }
        }
    }

    return st;
  }


  private List<StatementTree> getChildren(Tree st) {

    switch (st.kind()) {
      case METHOD:
        return ((MethodTree) st).block().body();
      case IF_STATEMENT:
        if (((IfStatementTree) st).thenStatement().is(Kind.BLOCK)) {
          return ((BlockTree) ((IfStatementTree) st).thenStatement()).body();
        }
        return null;
      case FOR_EACH_STATEMENT:
        return ((BlockTree)((ForEachStatement) st).statement()).body();
      case FOR_STATEMENT:
        return ((BlockTree)((ForStatementTree) st).statement()).body();
      case DO_STATEMENT:
        return ((BlockTree)((DoWhileStatementTree) st).statement()).body();
      case WHILE_STATEMENT:
        return ((BlockTree)((WhileStatementTree) st).statement()).body();
      case LAMBDA_EXPRESSION:
        return ((BlockTree)((LambdaExpressionTree) st).body()).body();
      case SWITCH_STATEMENT:
        List<StatementTree> children = new ArrayList<>();
        for(CaseGroupTree cgt : ((SwitchStatementTree) st).cases()) {
          children.addAll(cgt.body());
        }
        return children;
      case TRY_STATEMENT:
        return ((TryStatementTree) st).block().body();
      case BLOCK:
        // final `else` clause
        return ((BlockTree) st).body();
      default:
        return null;
    }
  }

  public void setMax(int max) {
    this.max = max;
  }

}
