package com.tzavellas.dyndelegate;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;

/**
 * 
 * A strategy interface for EJBHome factories.
 * 
 * @see EJB Home Factory design pattern
 * @see SimpleEJBHomeFactory
 * 
 * @author Spiros Tzavellas
 */
public interface EJBHomeFactory {
	
	/**
	 * Return the EJBHome bound at the specified JNDI name.
	 * 
	 * @param jndiName the JNDI name where the remote interface is bound
	 * @param homeInterface the class of the remote home interface.
	 * 
	 * @return the remote home interface
	 */
	EJBHome lookupRemote(String jndiName, Class homeInterface);
	
	/**
	 * Return the EJBLocalHome bound at the specified JNDI name.
	 * 
	 * @param jndiName the JNDI name where the local interface is bound
	 * @param homeInterface the class of the local home interface.
	 * 
	 * @return the local home interface
	 */
	EJBLocalHome lookupLocal(String jndiName, Class homeInterface);

}
