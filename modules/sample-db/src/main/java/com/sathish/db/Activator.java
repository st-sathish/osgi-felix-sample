package com.sathish.db;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import javax.sql.DataSource;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class Activator implements BundleActivator {

	  /*private String rootDir;
	  private ServiceRegistration<?> datasourceRegistration;
	  private ComboPooledDataSource pooledDataSource;
	  
	  @Override
	  public void start(BundleContext bundleContext) throws Exception {
		// Use the configured storage directory
		    rootDir = bundleContext.getProperty("com.capestartproject.storage.dir") + File.separator + "db";

		    // Register the data source, defaulting to an embedded H2 database if DB configurations are not specified
		    String jdbcDriver = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.driver"),
		            "org.h2.Driver");
		    String jdbcUrl = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.url"),
		            "jdbc:h2:" + rootDir);
		    String jdbcUser = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.user"), "capehub");
		    String jdbcPass = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.pass"), "capehub");

		    Integer maxPoolSize = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.pool.max.size"));
		    Integer minPoolSize = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.pool.min.size"));
		    Integer acquireIncrement = getConfigProperty(
		            bundleContext.getProperty("com.capestartproject.db.jdbc.pool.acquire.increment"));
		    Integer maxStatements = getConfigProperty(
		            bundleContext.getProperty("com.capestartproject.db.jdbc.pool.max.statements"));
		    Integer loginTimeout = getConfigProperty(
		            bundleContext.getProperty("com.capestartproject.db.jdbc.pool.login.timeout"));
		    Integer maxIdleTime = getConfigProperty(
		            bundleContext.getProperty("com.capestartproject.db.jdbc.pool.max.idle.time"));
		    Integer maxConnectionAge = getConfigProperty(
		            bundleContext.getProperty("com.capestartproject.db.jdbc.pool.max.connection.age"));

		    pooledDataSource = new ComboPooledDataSource();
		    pooledDataSource.setDriverClass(jdbcDriver);
		    pooledDataSource.setJdbcUrl(jdbcUrl);
		    pooledDataSource.setUser(jdbcUser);
		    pooledDataSource.setPassword(jdbcPass);
		    if (minPoolSize != null)
		      pooledDataSource.setMinPoolSize(minPoolSize);
		    if (maxPoolSize != null)
		      pooledDataSource.setMaxPoolSize(maxPoolSize);
		    if (acquireIncrement != null)
		      pooledDataSource.setAcquireIncrement(acquireIncrement);
		    if (maxStatements != null)
		      pooledDataSource.setMaxStatements(maxStatements);
		    if (loginTimeout != null)
		      pooledDataSource.setLoginTimeout(loginTimeout);
		    if (maxIdleTime != null)
		      pooledDataSource.setMaxIdleTime(maxIdleTime);
		    if (maxConnectionAge != null)
		      pooledDataSource.setMaxConnectionAge(maxConnectionAge);

		    Connection connection = null;
		    try {
		    	System.out.println("Testing connectivity to database at {} "+jdbcUrl);
		      connection = pooledDataSource.getConnection();
		      Hashtable<String, String> dsProps = new Hashtable<>();
		      dsProps.put("osgi.jndi.service.name", "jdbc/capehub");
		      datasourceRegistration = bundleContext.registerService(DataSource.class.getName(), pooledDataSource, dsProps);
		    } catch (SQLException e) {
		    	System.out.println("Connection attempt to {} failed"+jdbcUrl);
		    	System.out.println("Exception: "+e);
		      throw e;
		    } finally {
		      if (connection != null)
		        connection.close();
		    }

		    System.out.println("Database connection pool established at {} "+jdbcUrl);
		  }

		  @Override
		  public void stop(BundleContext context) throws Exception {
			  System.out.println("Shutting down database");
		    if (datasourceRegistration != null)
		      datasourceRegistration.unregister();
		    System.out.println("Shutting down connection pool");
		    DataSources.destroy(pooledDataSource);
		  }

		  private String getConfigProperty(String config, String defaultValue) {
		    return config == null ? defaultValue : config;
		  }

		  private Integer getConfigProperty(String config) {
		    return config == null ? null : Integer.parseInt(config);
		  }*/
	
	/** The logging facility */
	  //protected static final Logger logger = LoggerFactory.getLogger(Activator.class);

	  /**
	   * The persistence properties service registration
	   */
	  protected ServiceRegistration propertiesRegistration = null;

	  protected String rootDir;
	  protected ServiceRegistration datasourceRegistration;
	  protected ComboPooledDataSource pooledDataSource;
	  protected BundleContext bundleContext;
	  protected BundleListener jpaClientBundleListener;

	  public Activator() {
	  }

	  public Activator(String rootDir) {
	    this.rootDir = rootDir;
	  }

	  /**
	   * {@inheritDoc}
	   *
	   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	   */
	  @Override
	  public void start(BundleContext bundleContext) throws Exception {
	    this.bundleContext = bundleContext;

	    // Use the configured storage directory
	    rootDir = bundleContext.getProperty("com.capestartproject.storage.dir") + File.separator + "db";

	    // Register the Datasource, defaulting to an embedded H2 database if DB configurations are not specified
	    String vendor = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.vendor"), "HSQL");
	    String jdbcDriver = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.driver"),
	            "org.h2.Driver");
	    String jdbcUrl = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.url"), "jdbc:h2:"
	            + rootDir);
	    String jdbcUser = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.user"), "capehub");
	    String jdbcPass = getConfigProperty(bundleContext.getProperty("com.capestartproject.db.jdbc.pass"), "capehub");
	    pooledDataSource = new ComboPooledDataSource();
	    pooledDataSource.setDriverClass(jdbcDriver);
	    pooledDataSource.setJdbcUrl(jdbcUrl);
	    pooledDataSource.setUser(jdbcUser);
	    pooledDataSource.setPassword(jdbcPass);

	    Connection connection = null;
	    try {
	      System.out.println("Testing connectivity to database at {}" +jdbcUrl);
	      connection = pooledDataSource.getConnection();
	      datasourceRegistration = bundleContext.registerService(DataSource.class.getName(), pooledDataSource, null);
	    } catch (SQLException e) {
	      System.out.println("Connection attempt to {} failed"+ jdbcUrl);
	      System.out.println("Exception: " +e);
	      throw e;
	    } finally {
	      if (connection != null)
	        connection.close();
	    }

	    // Register the persistence properties
	    Dictionary<String, Serializable> props = new Hashtable<String, Serializable>();
	    props.put("type", "persistence");
	    props.put("javax.persistence.nonJtaDataSource", pooledDataSource);
	    props.put("eclipselink.target-database", vendor);
	    props.put("eclipselink.logging.logger", "JavaLogger");
	    props.put("eclipselink.cache.shared.default", "false");
	    if ("true".equalsIgnoreCase(bundleContext.getProperty("com.capestartproject.db.ddl.generation"))) {
	      props.put("eclipselink.ddl-generation", "create-tables");
	      if ("true".equalsIgnoreCase(bundleContext.getProperty("com.capestartproject.db.ddl.script.generation"))) {
	        props.put("eclipselink.ddl-generation.output-mode", "both");
	      } else {
	        props.put("eclipselink.ddl-generation.output-mode", "database");
	      }
	    }
	    propertiesRegistration = bundleContext.registerService(Map.class.getName(), props, props);

	    // Listen for bundles with persistence units restarting
	    jpaClientBundleListener = new JpaClientBundleListener();
	    bundleContext.addBundleListener(jpaClientBundleListener);

	    System.out.println("Database connection pool established at {}"+jdbcUrl);
	  }

	  /**
	   * {@inheritDoc}
	   *
	   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	   */
	  @Override
	  public void stop(BundleContext context) throws Exception {
	    context.removeBundleListener(jpaClientBundleListener);
	    if (propertiesRegistration != null)
	      propertiesRegistration.unregister();
	    if (datasourceRegistration != null)
	      datasourceRegistration.unregister();
	    DataSources.destroy(pooledDataSource);
	  }

	  private String getConfigProperty(String config, String defaultValue) {
	    return config == null ? defaultValue : config;
	  }

	  /**
	   * Listens for bundles using JPA, and refreshes the persistence provider bundle when they are updated.
	   */
	  class JpaClientBundleListener implements BundleListener {
	    /**
	     * {@inheritDoc}
	     *
	     * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework.BundleEvent)
	     */
	    @Override
	    public void bundleChanged(BundleEvent event) {
	      if (event.getType() != BundleEvent.UPDATED)
	        return;
	      Bundle updatedBundle = event.getBundle();
	      if (updatedBundle.getEntry("/META-INF/persistence.xml") == null)
	        return;
	      ServiceReference jpaProviderRef = bundleContext.getServiceReference("javax.persistence.spi.PersistenceProvider");
	      if (jpaProviderRef != null) {
	        Bundle jpaBundle = jpaProviderRef.getBundle();
	        try {
	          jpaBundle.update();
	          System.out.println("Updated the JPA provider bundle");
	        } catch (BundleException e) {
	        	System.out.println("Failed to update the JPA provider bundle: {}"+ e);
	        }
	      }
	    }
	  }
}
