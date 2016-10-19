package com.sonarsource.rules.java;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;

import static org.fest.assertions.Assertions.assertThat;

public class JavaCustomRulesDefinitionTest {

  @Test
  public void registration_test() {
    JavaCustomRulesDefinition definition = new JavaCustomRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    definition.define(context);
    RulesDefinition.Repository repository = context.repository(JavaCustomRulesDefinition.REPOSITORY_KEY);

    assertThat(repository.key()).isEqualTo("java-cognitive-complexity");
    assertThat(repository.name()).isEqualTo("Java Cognitive Complexity");
    assertThat(repository.language()).isEqualTo("java");
    assertThat(repository.rules()).hasSize(JavaCustomRulesList.getChecks().size());
  }
}
