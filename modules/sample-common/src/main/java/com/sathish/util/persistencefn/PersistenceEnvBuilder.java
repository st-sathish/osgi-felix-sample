package com.sathish.util.persistencefn;

import static com.sathish.util.data.Collections.map;

import com.sathish.util.data.Function0;
import com.sathish.util.data.Lazy;

import java.util.Map;

import javax.persistence.spi.PersistenceProvider;

/**
 * Builder for persistence environments.
 * Useful in OSGi bound services where required properties are injected by the OSGi environment.
 */
public final class PersistenceEnvBuilder {
  private Map<String, Object> persistenceProperties = map();
  private PersistenceProvider persistenceProvider;
  private String persistenceUnit;
  private Lazy<com.sathish.util.persistencefn.PersistenceEnv> penv = Lazy.lazy(new Function0<com.sathish.util.persistencefn.PersistenceEnv>() {
    @Override public com.sathish.util.persistencefn.PersistenceEnv apply() {
      if (persistenceProvider == null) {
        throw new IllegalStateException("Persistence provider has not been set yet");
      }
      if (persistenceUnit == null) {
        throw new IllegalStateException("Persistence unit has not been set yet");
      }
      return PersistenceEnvs.mk(persistenceProvider, persistenceUnit, persistenceProperties);
    }
  });

  public PersistenceEnvBuilder() {
  }

  public PersistenceEnvBuilder(String persistenceUnit) {
    this.persistenceUnit = persistenceUnit;
  }

  /** Set the mandatory name of the persistence unit. */
  public void setPersistenceUnit(String name) {
    this.persistenceUnit = name;
  }

  /** Set the optional persistence properties. */
  public void setPersistenceProperties(Map<String, Object> properties) {
    this.persistenceProperties = properties;
  }

  /** Set the mandatory persistence provider. */
  public void setPersistenceProvider(PersistenceProvider provider) {
    this.persistenceProvider = provider;
  }

  /** Builds the persistence env. Always returns the same environment so it may be safely called multiple times. */
  public PersistenceEnv get() {
    return penv.value();
  }
}
