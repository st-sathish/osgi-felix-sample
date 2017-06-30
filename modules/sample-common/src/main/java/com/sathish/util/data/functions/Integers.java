package com.sathish.util.data.functions;

import com.sathish.util.data.Function;
import com.sathish.util.data.Function2;

/**
 * Functions operating on integers.
 */
public final class Integers {

  private Integers() {
  }

  /**
   * Addition.
   */
  public static Function<Integer, Integer> add(final Integer val) {
    return new Function<Integer, Integer>() {
      @Override
      public Integer apply(Integer number) {
        return number + val;
      }
    };
  }

  /**
   * Addition.
   */
  public static Function2<Integer, Integer, Integer> add() {
    return new Function2<Integer, Integer, Integer>() {
      @Override
      public Integer apply(Integer a, Integer b) {
        return a + b;
      }
    };
  }
}
