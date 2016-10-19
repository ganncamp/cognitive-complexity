package com.sonarsource.rules.java;

import com.google.common.collect.Iterables;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;

import java.util.List;


/**
 * Declare rule metadata in server repository of rules. 
 * That allows to list the rules in the page "Rules".
 */
public class JavaCustomRulesDefinition implements RulesDefinition {

  public static final String REPOSITORY_KEY = "java-cognitive-complexity";

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(REPOSITORY_KEY, "java");
    repository.setName("Java Cognitive Complexity");

    List<Class> checks = JavaCustomRulesList.getChecks();
    new RulesDefinitionAnnotationLoader().load(repository, Iterables.toArray(checks, Class.class));
    repository.done();
  }

}
