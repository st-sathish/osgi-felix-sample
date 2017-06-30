package com.sathish.util.data;

import static com.sathish.util.EqualsUtil.ne;
import static com.sathish.util.RequireUtil.notNull;
import static com.sathish.util.data.Cells.fcell;
import static com.sathish.util.data.Option.some;
import static com.sathish.util.data.Tuple.tuple;

/**
 * Value cell, a mutable data container.
 *
 * Cells provide a pattern that reverses the listener pattern. Instead of propagating changes of a value to registered
 * listeners via callbacks, the dependent object just holds a reference to a cell and pulls the value when needed.
 *
 * Cells must not contain null!
 *
 * @param <A>
 *          the value type
 */
public final class VCell<A> extends Cell<A> {
  private volatile A a;
  private int change;
  private final boolean stable;

  private final Object lock = new Object();

  public VCell(A a, boolean stable) {
    this.a = notNull(a, "a");
    this.stable = stable;
    change = 1;
  }

  /** Constructor function. */
  public static <A> VCell<A> cell(A a) {
    return new VCell<A>(a, true);
  }

  public static <A> VCell<A> iocell(A a) {
    return new VCell<A>(a, false);
  }

  /** Create a cell containing some a. */
  public static <A> VCell<Option<A>> ocell(A a) {
    return cell(some(a));
  }

  /** Create a cell containing none. */
  public static <A> VCell<Option<A>> ocell() {
    return cell(Option.<A> none());
  }

  /** Get the cell's value. */
  @Override
  public A get() {
    return a;
  }

  @Override
  protected Tuple<A, Object> change() {
    synchronized (lock) {
      if (!stable)
        change += 1;
      return tuple(a, (Object) change);
    }
  }

  /** Set the cell's value. */
  public void set(A a) {
    synchronized (lock) {
      if (ne(a, this.a)) {
        this.a = a;
        change += 1;
      }
    }
  }

  @Override
  public <B> Cell<B> lift(Function<A, B> f) {
    return fcell(this, f);
  }
}
