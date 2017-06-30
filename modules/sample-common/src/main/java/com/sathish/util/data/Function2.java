package com.sathish.util.data;

import static com.sathish.util.data.functions.Misc.chuck;

import com.sathish.util.data.functions.Functions;

/**
 * Function of arity 2.
 *
 * @see X
 */
public abstract class Function2<A, B, C> {
  /** Apply function to <code>a</code> and <code>b</code>. */
  public abstract C apply(A a, B b);

  /** Currying. */
  public Function<B, C> curry(final A a) {
    return Functions.curry(this, a);
  }

  /** Currying. (a, b) -&gt; c =&gt; a -&gt; b -&gt; c */
  public Function<A, Function<B, C>> curry() {
    return Functions.curry(this);
  }

  public Function<Tuple<A, B>, C> tupled() {
    return Functions.tupled(this);
  }

  /** Argument flipping. */
  public Function2<B, A, C> flip() {
    return Functions.flip(this);
  }

  /** Turn this function into an effect by discarding its result. */
  public Effect2<A, B> toEffect() {
    return Functions.toEffect(this);
  }

  /** Version of {@link Function2} that allows for throwing a checked exception. */
  public abstract static class X<A, B, C> extends Function2<A, B, C> {
    @Override
    public final C apply(A a, B b) {
      try {
        return xapply(a, b);
      } catch (Exception e) {
        return chuck(e);
      }
    }

    /**
     * Apply function to <code>a</code>. Any thrown exception gets "chucked" so that you may
     * catch them as is. See {@link Functions#chuck(Throwable)} for details.
     */
    public abstract C xapply(A a, B b) throws Exception;
  }
}
