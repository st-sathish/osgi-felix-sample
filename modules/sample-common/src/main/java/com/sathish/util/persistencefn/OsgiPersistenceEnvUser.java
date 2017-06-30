package com.sathish.util.persistencefn;

import java.util.Map;

import javax.persistence.spi.PersistenceProvider;

/**
 * Definition of a contract for the use of a {@link org.opencastproject.util.persistence.PersistenceEnv} in an OSGi environment.
 * <p>
 * Use in conjunction with {@link org.opencastproject.util.persistence.PersistenceEnvBuilder}.
 */
public interface OsgiPersistenceEnvUser {
  /** OSGi callback to set persistence properties. */
  void setPersistenceProperties(Map<String, Object> persistenceProperties);

  /** OSGi callback to set persistence provider. */
  void setPersistenceProvider(PersistenceProvider persistenceProvider);

  /**
   * Return the persistence environment.
   * <p>
   * Create the persistence environment in the activate method like so:
   * <pre>
   *   penv = PersistenceEnvs.persistenceEnvironment(persistenceProvider, "my.persistence.context", persistenceProperties);
   * </pre>
   * Or better use the {@link org.opencastproject.util.persistence.PersistenceEnvBuilder}.
   */
  PersistenceEnv getPenv();

  /** Close the persistence environment {@link org.opencastproject.util.persistence.PersistenceEnv#close()}. Call from the deactivate method. */
  void closePenv();
}
