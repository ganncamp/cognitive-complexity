package com.sonarsource.rules.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;


public class CognitiveComplexityCheckTest {

  @Test
  public void testBasicCalculations(){

    CognitiveComplexityCheck check = new CognitiveComplexityCheck();
    check.setMax(0);
    JavaCheckVerifier.verify("src/test/files/CognitiveComplexityCheckMax0.java", check);
  }

}