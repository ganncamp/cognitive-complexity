class CognitiveComplexityCheck {

  String bulkActivate(Iterator<String> rules) { // Noncompliant {{The Cognitive Complexity of this method "bulkActivate" is 5 which is greater than 0 authorized.}}

    try {
      while (rules.hasNext()) {  //
        try {
          if (!changes.isEmpty()) {  }  //
        } catch (BadRequestException e) { }  //
      }
    } finally {
      if (condition) {
        doTheThing();
      }
    }
    return result;
  }

  private static String getValueToEval( List<String> strings ) { // Noncompliant {{The Cognitive Complexity of this method "getValueToEval" is 5 which is greater than 0 authorized.}}

    if (Measure.Level.ERROR.equals(alertLevel) && foo = YELLOW) {
      return condition.getErrorThreshold();
    } else if (Measure.Level.WARN.equals(alertLevel)) {
      return condition.getWarningThreshold();
    } else {
      while (true) {
        doTheThing();
      }
      throw new IllegalStateException(alertLevel.toString());
    }
  }

  boolean isPalindrome(char [] s, int len) { // Noncompliant {{The Cognitive Complexity of this method "isPalindrome" is 1 which is greater than 0 authorized.}}

    if(len < 2)
      return true;
    else
      return s[0] == s[len-1] && isPalindrome(s[1], len-2); // TODO find recursion
  }

  void extraConditions() { // Noncompliant {{The Cognitive Complexity of this method "extraConditions" is 11 which is greater than 0 authorized.}}

    if (a < b) {
      doTheThing();
    }

    if (a == b || c > 3 || b-7 == c) {
      while (a-- > 0 && b++ < 10) {
        doTheOtherThing();
      }
    }

    do {

    } while (a-- > 0 || b != YELLOW);

    for (int i = 0; i < 10 && j > 20; i++) {
      doSomethingElse();
    }
  }

  public static void main (String [] args) { // Noncompliant {{The Cognitive Complexity of this method "main" is 4 which is greater than 0 authorized.}}

    Runnable r = () -> {
      if (condition) {
        System.out.println("Hello world!");
      }
    };

    r = new MyRunnable();

    r = new Runnable () {
      public void run(){
        if (condition) {
          System.out.println("Well, hello again");
        }
      }
    };
  }

  int sumOfNonPrimes(int limit) { // Noncompliant {{The Cognitive Complexity of this method "sumOfNonPrimes" is 9 which is greater than 0 authorized.}}

    int sum = 0;
    OUTER: for (int i = 0; i < limit; ++i) {
      if (i <= 2) {
        continue;
      }
      for (int j = 2; j < 1; ++j) {
        if (i % j == 0) {
          continue OUTER;
        }
      }
      sum += i;
    }
    return sum;
  }

  String getWeight(int i){ // Noncompliant {{The Cognitive Complexity of this method "getWeight" is 4 which is greater than 0 authorized.}}

    if (i <=0) {
      return "no weight";
    }
    if (i < 10) {
      return "light";
    }
    if (i < 20) {
      return "medium";
    }
    if (i < 30) {
      return "heavy";
    }
    return "very heavy";
  }

  public static HighlightingType toProtocolType(TypeOfText textType) { // Noncompliant {{The Cognitive Complexity of this method "toProtocolType" is 1 which is greater than 0 authorized.}}

    switch (textType) {
      case ANNOTATION:
        return HighlightingType.ANNOTATION;
      case CONSTANT:
        return HighlightingType.CONSTANT;
      case CPP_DOC:
        return HighlightingType.CPP_DOC;
      default:
        throw new IllegalArgumentException(textType.toString());
    }
  }

  public String getSpecifiedByKeysAsCommaList() {
    return getRuleKeysAsString(specifiedBy);
  }


}

