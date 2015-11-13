package com.paxar.qps.common.config;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

/**
 * This class contains properties for D2Comm application. List of properties:
 * <ul>
 * 	<li>DB Host</li>
 * 	<li>Server Base</li>
 * 	<li>SMTP Host</li>
 * </ul>
 * @author rsav
 *
 */
public class D2CommProperties {
	
	/**
	 * Contains DB Host value.
	 */
	public static final String DB_HOST;
	/**
	 * Contains Server Base value.
	 */
	public static final String SERVER_BASE;
	/**
	 * Contains SMTP Host value.
	 */
	public static final String SMTP_HOST;
	
	/**
	 * Indicates key under which {@link #DB_HOST} property value will be stored in property file.
	 */
	private static final String DB_HOST_PROPERTY_KEY = "db.host";
	/**
	 * Indicates key under which {@link #SERVER_BASE} property value will be stored in property file.
	 */
	private static final String SERVER_BASE_PROPERTY_KEY = "server.base";
	/**
	 * Indicates key under which {@link #SMTP_HOST} property value will be stored in property file.
	 */
	private static final String SMTP_HOST_PROPERTY_KEY = "smtp.host";
	
	static {
		final File configFile = new File(System.getProperty("catalina.base"), "conf/d2comm.properties");
		
		if ((configFile == null) || !configFile.exists()) {
			throw new D2CommConfigurationException("Failed to foud d2omm.properties file in Tomcat conf directory");
		}
		
		try {
			final PropertiesConfiguration configuration = new PropertiesConfiguration(configFile);
			
			DB_HOST = notBlank(configuration.getString(DB_HOST_PROPERTY_KEY), DB_HOST_PROPERTY_KEY);
			SERVER_BASE = notBlank(configuration.getString(SERVER_BASE_PROPERTY_KEY), SERVER_BASE_PROPERTY_KEY);
			SMTP_HOST = notBlank(configuration.getString(SMTP_HOST_PROPERTY_KEY), SMTP_HOST_PROPERTY_KEY);
		} catch (ConfigurationException e) {
			throw new D2CommConfigurationException("Failed to load D2Comm properties", e);
		} 
	}

	private static String notBlank(String value, String property) {
		if (StringUtils.isBlank(value)) {
			throw new D2CommConfigurationException(String.format("D2Comm property [%s] should not be blank", property));
		}
		
		return value;
	}
}
