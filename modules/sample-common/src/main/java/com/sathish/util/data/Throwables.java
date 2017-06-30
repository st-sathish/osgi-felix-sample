package com.sathish.util.data;

import static com.sathish.util.data.functions.Misc.chuck;

public final class Throwables {
  private Throwables() {
  }

  /**
   * Forward exception <code>t</code> directly or wrap it into an instance of <code>wrapper</code>
   * if it is not of the wrapper's type. Class <code>wrapper</code> needs a constructor taking just
   * a {@link Throwable}.
   */
  public static <T extends Throwable, A> A forward(Throwable t, Class<T> wrapper) throws T {
    if (wrapper.isAssignableFrom(t.getClass())) {
      throw (T) t;
    } else {
      try {
        throw wrapper.getConstructor(Exception.class).newInstance(t);
      } catch (Exception e) {
        return chuck(e);
      }
    }
  }
}
