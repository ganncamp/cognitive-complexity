class CognitiveComplexityCheck {


  public void doFilter(ServletRequest servletRequest) { // Noncompliant {{The Cognitive Complexity of this method "doFilter" is 13 which is greater than 0 authorized.}}

    if (consumedByStaticFile) {
      return;
    }

    try {

    } catch (HaltException halt) {

    } catch (Exception generalException) {

    }

    if (body.notSet() && responseWrapper.isRedirected()) {
      body.set("");
    }

    if (body.notSet() && hasOtherHandlers) {
      if (servletRequest instanceof HttpRequestWrapper) {
        ((HttpRequestWrapper) servletRequest).notConsumed(true);
        return;
      }
    }

    if (body.notSet() && !externalContainer) {
      LOG.info("The requested route [" + uri + "] has not been mapped in Spark");
    }

    if (body.isSet()) {
      body.serializeTo(httpResponse, serializerChain, httpRequest);
    } else if (chain != null) {
      chain.doFilter(httpRequest, httpResponse);
    }
  }


  public final T to(U u) { // Noncompliant {{The Cognitive Complexity of this method "to" is 7 which is greater than 0 authorized.}}

    for (int ctr=0; ctr<args.length; ctr++)
      if (args[ctr].equals("-debug"))
        debug = true ;

    for (int i = chain.length - 1; i >= 0; i--)
      result = chain[i].to(result);

    if (foo)
      for (int i = 0; i < 10; i++)
        doTheThing();

    return (T) result;
  }


  static boolean enforceLimits(BoundTransportAddress boundTransportAddress) {
    Iterable<JoinTuple> itr = () -> new JoinTupleIterator(tuples.tuples(), parentIndex, parentReference);

    Predicate<TransportAddress> isLoopbackOrLinkLocalAddress = t -> t.address().getAddress().isLinkLocalAddress()
            || t.address().getAddress().isLoopbackAddress();

  }

  String bulkActivate(Iterator<String> rules) { // Noncompliant {{The Cognitive Complexity of this method "bulkActivate" is 6 which is greater than 0 authorized.}}

    try {
      while (rules.hasNext()) {  // +1
        try {
          if (!changes.isEmpty()) {  }  // +2, nesting 1
        } catch (BadRequestException e) { }  // +2, nesting 1
      }
    } finally {
      if (condition) {  // +1
        doTheThing();
      }
    }
    return result;
  }

  private static String getValueToEval( List<String> strings ) { // Noncompliant {{The Cognitive Complexity of this method "getValueToEval" is 6 which is greater than 0 authorized.}}

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

  boolean isPalindrome(char [] s, int len) { // Noncompliant {{The Cognitive Complexity of this method "isPalindrome" is 2 which is greater than 0 authorized.}}

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
      case ANNOTATION: {
        return HighlightingType.ANNOTATION;
      }
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

