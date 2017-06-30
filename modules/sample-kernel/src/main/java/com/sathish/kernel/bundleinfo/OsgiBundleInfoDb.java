package com.sathish.kernel.bundleinfo;

import javax.persistence.EntityManagerFactory;

/*import static com.sathish.util.persistence.PersistenceEnvs.persistenceEnvironment;

import com.sathish.util.persistence.PersistenceEnv;*/

/** OSGi bound bundle info database. */
public class OsgiBundleInfoDb extends AbstractBundleInfoDb {

	  public static final String PERSISTENCE_UNIT = "com.capestartproject.kernel";

	  private EntityManagerFactory emf;
	  //private PersistenceEnv penv;

	  /** OSGi DI */
	  void setEntityManagerFactory(EntityManagerFactory emf) {
	    this.emf = emf;
	    System.out.println("Entity manager factory created ::"+emf);
	  }

	  /*@Override protected PersistenceEnv getPersistenceEnv() {
	    return penv;
	  }*/

	  /** OSGi callback */
	  public void activate() {
	    //penv = persistenceEnvironment(emf);
	  }

	  public void deactivate() {
	    System.out.println("Closing persistence environment");
	    //penv.close();
	  }
}
