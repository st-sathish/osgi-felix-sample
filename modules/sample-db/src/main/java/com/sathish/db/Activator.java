package com.sathish.db;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.sql.DataSource;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class Activator implements BundleActivator {

	  private String rootDir;
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
		  }
}
