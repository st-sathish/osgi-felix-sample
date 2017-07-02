package com.sathish.kernel.bundleinfo;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.spi.PersistenceProvider;
import javax.print.attribute.TextSyntax;

/** OSGi bound bundle info database. */
public class OsgiBundleInfoDb implements BundleInfoDb {

	 private Map<String, Object> persistenceProperties;
	  private PersistenceProvider persistenceProvider;
	  
	  /** The factory used to generate the entity manager */
	  protected EntityManagerFactory emf = null;
	  
	/** OSGi DI */
	  public void setPersistenceProperties(Map<String, Object> persistenceProperties) {
	    this.persistenceProperties = persistenceProperties;
	  }

	  /** The entity manager used for persisting Java objects. */
	  protected EntityManager em = null;
	  
	  /** OSGi DI */
	  public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
	    this.persistenceProvider = persistenceProvider;
	  }
	  
	  /** OSGi callback */
	  @SuppressWarnings("unchecked")
	public void activate() {
		  emf = persistenceProvider.createEntityManagerFactory("com.capestartproject.kernel", persistenceProperties);
		  System.out.println("Sample Kernel Entity manager factory created" +emf);
		  em = emf.createEntityManager();
		  Query query = em.createNamedQuery("Test.findAll");
		  List<Test> tests = query.getResultList();
		  for(Test t : tests) {
			  System.out.println("Helloooooooooooooooooo############# "+t.testMessage);
		  }
	  }

	  public void deactivate() {
	    System.out.println("Closing persistence environment");
	    if(emf.isOpen()) {
	    	emf.close();
	    	em.close();
	    }
	  }
}
