package com.sathish.util.data;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators {
  private Iterators() {
  }

  /** Integer range. Upper bound excluded. */
  public static Iterator<Integer> intRangeE(final int start, final int end) {
    return new Iter<Integer>() {
      private int i = start;

      @Override public boolean hasNext() {
        return i < end;
      }

      @Override public Integer next() {
        if (hasNext()) {
          int r = i;
          i++;
          return r;
        } else {
          throw new NoSuchElementException();
        }
      }
    };
  }

  /** An infinite stream of <code>a</code>s. */
  public static <A> Iterator<A> constant(final A a) {
    return new Iter<A>() {
      @Override public boolean hasNext() {
        return true;
      }

      @Override public A next() {
        return a;
      }
    };
  }

  private abstract static class Iter<A> implements Iterator<A> {
    @Override
    public final void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public static <A> Iterable<A> iterable(final Iterator<A> ix) {
    return new Iterable<A>() {
      @Override public Iterator<A> iterator() {
        return ix;
      }
    };
  }
}
