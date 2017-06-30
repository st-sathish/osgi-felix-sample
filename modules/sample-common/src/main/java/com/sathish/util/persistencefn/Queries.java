package com.sathish.util.persistencefn;

import com.entwinemedia.fn.Fn;
import com.entwinemedia.fn.P2;
import com.entwinemedia.fn.data.Opt;

import org.joda.time.base.AbstractInstant;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 * JPA query constructors.
 */
public final class Queries {
  private Queries() {
  }

  /** {@link javax.persistence.EntityManager#find(Class, Object)} as a function wrapping the result into an Option. */
  public static <A> Fn<EntityManager, Opt<A>> find(final Class<A> clazz, final Object primaryKey) {
    return new Fn<EntityManager, Opt<A>>() {
      @Override public Opt<A> ap(EntityManager em) {
        return Opt.nul(em.find(clazz, primaryKey));
      }
    };
  }

  /** {@link javax.persistence.EntityManager#persist(Object)} as a function. */
  public static <A> Fn<EntityManager, A> persist(final A a) {
    return new Fn<EntityManager, A>() {
      @Override public A ap(EntityManager em) {
        em.persist(a);
        return a;
      }
    };
  }

  /** {@link javax.persistence.EntityManager#merge(Object)} as a function. */
  public static <A> Fn<EntityManager, A> merge(final A a) {
    return new Fn<EntityManager, A>() {
      @Override public A ap(EntityManager em) {
        return em.merge(a);
      }
    };
  }

  /** {@link javax.persistence.EntityManager#remove(Object)} as a function. */
  public static <A> Fn<EntityManager, A> remove(final A a) {
    return new Fn<EntityManager, A>() {
      @Override public A ap(EntityManager em) {
        em.remove(a);
        return null;
      }
    };
  }

  public static <A> A persistOrUpdate(final EntityManager em, final A a) {
    final Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(a);
    if (id == null) {
      em.persist(a);
      return a;
    } else {
      @SuppressWarnings("unchecked")
      final A dto = (A) em.find(a.getClass(), id);
      if (dto == null) {
        em.persist(a);
        return a;
      } else {
        return em.merge(a);
      }
    }
  }

  public static <A> Fn<EntityManager, A> persistOrUpdate(final A a) {
    return new Fn<EntityManager, A>() {
      @Override public A ap(EntityManager em) {
        return persistOrUpdate(em, a);
      }
    };
  }

  /**
   * Set a list of named parameters on a query.
   * <p>
   * Values of type {@link java.util.Date} and {@link org.joda.time.base.AbstractInstant}
   * are recognized and set as a timestamp ({@link javax.persistence.TemporalType#TIMESTAMP}.
   */
  public static <A extends Query> A setParams(A q, P2<String, ?>... params) {
    for (P2<String, ?> p : params) {
      final Object value = p.get1();
      if (value instanceof Date) {
        q.setParameter(p.get1(), (Date) value, TemporalType.TIMESTAMP);
      }
      if (value instanceof AbstractInstant) {
        q.setParameter(p.get1(), ((AbstractInstant) value).toDate(), TemporalType.TIMESTAMP);
      } else {
        q.setParameter(p.get1(), p.get2());
      }
    }
    return q;
  }

  /**
   * Set a list of positional parameters on a query.
   * <p>
   * Values of type {@link java.util.Date} and {@link org.joda.time.base.AbstractInstant}
   * are recognized and set as a timestamp ({@link javax.persistence.TemporalType#TIMESTAMP}.
   */
  public static <A extends Query> A setParams(A q, Object... params) {
    for (int i = 0; i < params.length; i++) {
      final Object value = params[i];
      if (value instanceof Date) {
        q.setParameter(i, (Date) value, TemporalType.TIMESTAMP);
      }
      if (value instanceof AbstractInstant) {
        q.setParameter(i, ((AbstractInstant) value).toDate(), TemporalType.TIMESTAMP);
      } else {
        q.setParameter(i, value);
      }
    }
    return q;
  }

  // -------------------------------------------------------------------------------------------------------------------

  /** Named queries with support for named parameters. */
  public static final TypedQueriesBase<P2<String, ?>> named = new TypedQueriesBase<P2<String, ?>>() {
    @Override public Query query(EntityManager em, String q, P2<String, ?>... params) {
      return setParams(em.createNamedQuery(q), params);
    }

    @Override
    public <A> TypedQuery<A> query(EntityManager em, String q, Class<A> type, P2<String, ?>... params) {
      return setParams(em.createNamedQuery(q, type), params);
    }
  };

  /** JPQL queries with support for named parameters. */
  public static final TypedQueriesBase<P2<String, ?>> jpql = new TypedQueriesBase<P2<String, ?>>() {
    @Override public Query query(EntityManager em, String q, P2<String, ?>... params) {
      return setParams(em.createQuery(q), params);
    }

    @Override
    public <A> TypedQuery<A> query(EntityManager em, String q, Class<A> type, P2<String, ?>... params) {
      return setParams(em.createQuery(q, type), params);
    }
  };

  /** Native SQL queries. Only support positional parameters. */
  public static final QueriesBase<Object> sql = new QueriesBase<Object>() {
    @Override public Query query(EntityManager em, String q, Object... params) {
      return setParams(em.createNativeQuery(q), params);
    }
  };

  // -------------------------------------------------------------------------------------------------------------------

  public abstract static class TypedQueriesBase<P> extends QueriesBase<P> {
    protected TypedQueriesBase() {
    }

    /**
     * Create a typed query from <code>q</code> with a list of parameters.
     * Values of type {@link java.util.Date} are recognized
     * and set as a timestamp ({@link javax.persistence.TemporalType#TIMESTAMP}.
     */
    public abstract <A> TypedQuery<A> query(EntityManager em,
                                            String queryName,
                                            Class<A> type,
                                            P2<String, ?>... params);

    /** Find multiple entities. */
    public <A> List<A> findAll(EntityManager em,
                               final String q,
                               final Class<A> type,
                               final P2<String, ?>... params) {
      return query(em, q, type, params).getResultList();
    }

    /** {@link #findAll(javax.persistence.EntityManager, String, Class, com.entwinemedia.fn.P2[])} as a function. */
    public <A> Fn<EntityManager, List<A>> findAll(final String q,
                                                  final Class<A> type,
                                                  final P2<String, ?>... params) {
      return new Fn<EntityManager, List<A>>() {
        @Override public List<A> ap(EntityManager em) {
          return findAll(em, q, type, params);
        }
      };
    }
  }

  // -------------------------------------------------------------------------------------------------------------------

  public abstract static class QueriesBase<P> {
    protected QueriesBase() {
    }

    /**
     * Create a query from <code>q</code> with a list of parameters.
     * Values of type {@link java.util.Date} are recognized
     * and set as a timestamp ({@link javax.persistence.TemporalType#TIMESTAMP}.
     */
    public abstract Query query(EntityManager em, String q, P... params);

    /** Run an update (UPDATE or DELETE) query and ensure that at least one row got affected. */
    public boolean update(EntityManager em, String q, P... params) {
      return query(em, q, params).executeUpdate() > 0;
    }

    /** {@link #update(javax.persistence.EntityManager, String, Object[])} as a function. */
    public Fn<EntityManager, Boolean> update(final String q, final P... params) {
      return new Fn<EntityManager, Boolean>() {
        @Override public Boolean ap(EntityManager em) {
          return update(em, q, params);
        }
      };
    }

    /**
     * Run a SELECT query that should return a single result.
     *
     * @return some value if the query yields exactly one result, none otherwise
     */
    public <A> Opt<A> findSingle(final EntityManager em,
                                 final String q,
                                 final P... params) {
      try {
        return Opt.some((A) query(em, q, params).getSingleResult());
      } catch (NoResultException e) {
        return Opt.none();
      } catch (NonUniqueResultException e) {
        return Opt.none();
      }
    }

    /** {@link #findSingle(javax.persistence.EntityManager, String, Object[])} as a function. */
    public <A> Fn<EntityManager, Opt<A>> findSingle(final String q,
                                                    final P... params) {
      return new Fn<EntityManager, Opt<A>>() {
        @Override public Opt<A> ap(EntityManager em) {
          return findSingle(em, q, params);
        }
      };
    }

    /** Run a SELECT query and return only the first result item. */
    public <A> Opt<A> findFirst(final EntityManager em,
                                final String q,
                                final P... params) {
      try {
        return Opt.some((A) query(em, q, params).setMaxResults(1).getSingleResult());
      } catch (NoResultException e) {
        return Opt.none();
      } catch (NonUniqueResultException e) {
        return Opt.none();
      }
    }

    /** {@link #findSingle(javax.persistence.EntityManager, String, Object[])} as a function. */
    public <A> Fn<EntityManager, Opt<A>> findFirst(final String q,
                                                   final P... params) {
      return new Fn<EntityManager, Opt<A>>() {
        @Override public Opt<A> ap(EntityManager em) {
          return findFirst(em, q, params);
        }
      };
    }

    /** Run a COUNT(x) query. */
    public long count(final EntityManager em, final String q, final P... params) {
      return (Long) query(em, q, params).getSingleResult();
    }

    /** {@link #count(javax.persistence.EntityManager, String, Object[])} as a function. */
    public Fn<EntityManager, Long> count(final String q, final P... params) {
      return new Fn<EntityManager, Long>() {
        @Override public Long ap(EntityManager em) {
          return count(em, q, params);
        }
      };
    }

    /** Find multiple entities. */
    public <A> List<A> findAll(EntityManager em, final String q, final P... params) {
      return (List<A>) query(em, q, params).getResultList();
    }

    /** {@link #findAll(javax.persistence.EntityManager, String, Object[])} as a function. */
    public <A> Fn<EntityManager, List<A>> findAll(final String q,
                                                  final P... params) {
      return new Fn<EntityManager, List<A>>() {
        @Override public List<A> ap(EntityManager em) {
          return findAll(em, q, params);
        }
      };
    }

    /** Find multiple objects with optional pagination. */
    public <A> List<A> findAll(final EntityManager em,
                               final String q,
                               final Opt<Integer> offset,
                               final Opt<Integer> limit,
                               final P... params) {
      final Query query = query(em, q, params);
      for (Integer x : offset) query.setFirstResult(x);
      for (Integer x : limit) query.setMaxResults(x);
      return (List<A>) query.getResultList();
    }

    public <A> Fn<EntityManager, List<A>> findAll(final String q,
                                                  final Opt<Integer> offset,
                                                  final Opt<Integer> limit,
                                                  final P... params) {
      return new Fn<EntityManager, List<A>>() {
        @Override public List<A> ap(EntityManager em) {
          return findAll(em, q, offset, limit, params);
        }
      };
    }

    /** Find multiple objects with pagination. */
    public <A> List<A> findAll(final EntityManager em,
                               final String q,
                               final int offset,
                               final int limit,
                               final P... params) {
      final Query query = query(em, q, params);
      query.setFirstResult(offset);
      query.setMaxResults(limit);
      return (List<A>) query.getResultList();
    }

    public <A> Fn<EntityManager, List<A>> findAll(final String q,
                                                  final int offset,
                                                  final int limit,
                                                  final P... params) {
      return new Fn<EntityManager, List<A>>() {
        @Override public List<A> ap(EntityManager em) {
          return findAll(em, q, offset, limit, params);
        }
      };
    }
  }
}
