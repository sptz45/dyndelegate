package com.tzavellas.dyndelegate;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * A non-caching implementation of the <code>EJBHomeFactory</code>
 * interface.
 * 
 * @see EJBHomeFactory
 * @author Spiros Tzavellas
 */
public class SimpleEJBHomeFactory implements EJBHomeFactory {

	/** {@inheritDoc} */
	public EJBHome lookupRemote(String jndiName, Class homeInterface) {
		try {
			Context ctx = new InitialContext();
			Object home = ctx.lookup(jndiName);
			
			return (EJBHome) PortableRemoteObject.narrow(home, homeInterface); 
		
		} catch (NamingException e) {
			throw new RuntimeException("Error in JNDI lookup with name'"
					+ jndiName + "'", e);
		}
	}
	

	/** {@inheritDoc} */
	public EJBLocalHome lookupLocal(String jndiName, Class homeInterface) {
		try {
			Context ctx = new InitialContext();
			Object home = ctx.lookup(jndiName);
			
			if (! homeInterface.isAssignableFrom(home.getClass()))
				throw new ClassCastException("The returned Home is does not implement "
												+ homeInterface.getName());
			
			return (EJBLocalHome) home; 
		
		} catch (NamingException e) {
			throw new RuntimeException("Error in JNDI lookup with name'"
					+ jndiName + "'", e);
		}
	}
}
