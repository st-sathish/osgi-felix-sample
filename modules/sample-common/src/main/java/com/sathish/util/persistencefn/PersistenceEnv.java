package com.sathish.util.persistencefn;

import com.entwinemedia.fn.Fn;

import javax.persistence.EntityManager;

/** Persistence environment to perform a transaction. */
public abstract class PersistenceEnv {
  /** Run code inside a transaction. */
  public abstract <A> A tx(Fn<EntityManager, A> transactional);

  /** {@link #tx(com.entwinemedia.fn.Fn)} as a function. */
  public <A> Fn<Fn<EntityManager, A>, A> tx() {
    return new Fn<Fn<EntityManager, A>, A>() {
      @Override
      public A ap(Fn<EntityManager, A> transactional) {
        return tx(transactional);
      }
    };
  }

  /** Close the environment and free all associated resources. */
  public abstract void close();
}
