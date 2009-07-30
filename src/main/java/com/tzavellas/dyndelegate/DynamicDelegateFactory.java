package com.tzavellas.dyndelegate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A Dynamic Delegate for Session Beans.
 * <p>
 * This class  dynamically creates a <i>Business Delegate</i>
 * for a Session EJB. The delegate will implement the specified interface.
 * When a method is called on the delegate the corresponding method
 * on the client interface of bean will be invoked. Any exception that
 * may be thrown from the invocation will be converted and re-thrown
 * according to the <code>ThrowableConverter</code> of this class.
 * <p>
 * To use this class your EJBs must implement the
 * <i>Business Interface</i> EJB design pattern (see the EJB Design
 * Patterns book by Floyd Marinescu). In this pattern your EJB's bean
 * class and client interface (local or remote), both implement a
 * common "Business Interface" that defines the business methods.</p>
 * <p>
 * When creating a dynamic delegate for an EJB, supply as the delegate
 * interface the Business Interface of your bean. The business interface
 * usually does not throw any EJB specific exceptions (like RemoteException
 * and EJBException) and does not have any dependency on the EJB APIs.</p>
 * <p>
 * This work is based on the "EJB best practices: The dynamic delegate"
 * article by Brett McLaughlin and on the Spring Framework EJB Access
 * classes found at the <code>org.springframework.ejb.access</code> package.</p>
 * 
 * @author Spiros Tzavellas
 * 
 * @see ThrowableConverter
 * @see EJBHomeFactory
 * @see <a href="http://www-128.ibm.com/developerworks/java/library/j-ejb1119.html">EJB best practices: The dynamic delegate</a>
 * @see <a href="http://www-128.ibm.com/developerworks/java/library/j-ejbtip0820/">The Business Interface design pattern</a>
 * @see <a href="http://www.springframework.org/">Spring Framework</a>
 * 
 */
public class DynamicDelegateFactory {
	
	private ThrowableConverter exceptionConverter;
	private EJBHomeFactory homeFactory;
	
	
	/**
	 * Construct a <code>DynamicDelegateFactory</code> using the specified
	 * <code>ThrowableConverter</code> and <code>EJBHomeFactory</code>.
	 * 
	 * @see ThrowableConverter
	 * @see EJBHomeFactory
	 */
	public DynamicDelegateFactory(ThrowableConverter exceptionConverter, EJBHomeFactory homeFactory) {
		this.exceptionConverter = exceptionConverter;
		this.homeFactory = homeFactory;
	}
	
	
	/**
	 * Construct a DynamicDelegateFactory with an EJBExceptionConverter
	 * and a SimpleEJBHomeFactory.
	 * 
	 * @see EJBExceptionConverter
	 * @see SimpleEJBHomeFactory
	 */
	public DynamicDelegateFactory() {
		this(new EJBExceptionConverter(), new SimpleEJBHomeFactory());
	}
	
	
	/**
	 * Set the <code>ThrowableConverter</code> to use with this factory.
	 */
	public void setExceptionConverter(ThrowableConverter exceptionConverter) {
		this.exceptionConverter = exceptionConverter;
	}
	
	
	/**
	 * Set the <code>EJBHomeFactory</code> to use with this factory.
	 */
	public void setHomeFactory(EJBHomeFactory homeFactory) {
		this.homeFactory = homeFactory;
	}
	
	
	/**
	 * Dynamically create a Business Delegate for an EJB with remote access.
	 * 
	 * @param jndiName the JNDI name to lookup the EJB.
	 * @param homeInterface the class of the home interface of the EJB that
	 *        the created delegate will proxy
	 * @param businnessInterface the interface that the created delegate
	 *        will implement
	 * 
	 * @return a Business Delegate object for the EJB bound to the specified
	 *         JNDI name implementing the specified interface.
	 */
	public Object createRemoteDelegate(String jndiName, Class homeInterface, Class businnessInterface) {
		return createRemoteDelegate(jndiName, homeInterface, businnessInterface, new Object[0]);
	}
	
	
	/**
	 * Dynamically create a Business Delegate for an EJB with remote access.
	 * 
	 * @param jndiName the JNDI name to lookup the EJB.
	 * @param homeInterface the class of the home interface of the EJB that
	 *        the created delegate will proxy
	 * @param businnessInterface the interface that the created delegate
	 *        will implement
	 * @param createArgs the arguments of the create method in the EJB's
	 *        home interface
	 * 
	 * @return a Business Delegate object for the EJB bound to the specified
	 *         JNDI name implementing the specified interface.
	 */
	public Object createRemoteDelegate(String jndiName, Class homeInterface, Class businnessInterface, Object[] createArgs) {
		return createDelegate(jndiName, homeInterface, businnessInterface, createArgs, true);
	}
	
	
	/**
	 * Dynamically create a Business Delegate for an EJB with local access.
	 * 
	 * @param jndiName the JNDI name to lookup the EJB.
	 * @param homeInterface the class of the home interface of the EJB that
	 *        the created delegate will proxy
	 * @param businnessInterface the interface that the created delegate
	 *        will implement
	 * 
	 * @return a Business Delegate object for the EJB bound to the specified
	 *         JNDI name implementing the specified interface.
	 */
	public Object createLocalDelegate(String jndiName, Class homeInterface, Class businnessInterface) {
		return createLocalDelegate(jndiName, homeInterface, businnessInterface, new Object[0]);
	}
	
	
	/**
	 * Dynamically create a Business Delegate for an EJB with local access.
	 * 
	 * @param jndiName the JNDI name to lookup the EJB.
	 * @param homeInterface the class of the home interface of the EJB that
	 *        the created delegate will proxy
	 * @param businnessInterface the interface that the created delegate
	 *        will implement
	 * @param createArgs the arguments of the create method in the EJB's
	 *        home interface
	 * 
	 * @return a Business Delegate object for the EJB bound to the specified
	 *         JNDI name implementing the specified interface.
	 */
	public Object createLocalDelegate(String jndiName, Class homeInterface, Class businnessInterface, Object[] createArgs) {
		return createDelegate(jndiName, homeInterface, businnessInterface, createArgs, false);
	}
	

	/**
	 * Dynamically create a Business Delegate.
	 * 
	 * @param jndiName the JNDI name to lookup the EJB.
	 * @param homeInterface the class of the home interface of the EJB that
	 *        the created delegate will proxy
	 * @param businnessInterface the interface that the created delegate
	 *        will implement
	 * @param createArgs the arguments of the create method in the EJB's
	 *        home interface
	 * @param isRemote the access of the EJB (remote or local)
	 * 
	 * @return a Business Delegate object for the EJB bound to the specified
	 *         JNDI name implementing the specified interface.
	 */
	protected Object createDelegate(String jndiName, Class homeInterface,
			Class businnessInterface, Object[] createArgs, boolean isRemote) {

		if (!businnessInterface.isInterface())
			throw new IllegalArgumentException("The specified class '"
					+ businnessInterface.getName() + "' must be an interface!");

			Object home = null;
			if (isRemote)
				home = homeFactory.lookupRemote(jndiName, homeInterface);
			else
				home = homeFactory.lookupLocal(jndiName, homeInterface);
			
			Object ejb = createEJBFromHome(home, createArgs);

			if (!businnessInterface.isAssignableFrom(ejb.getClass())) {
				throw new IllegalArgumentException(
						"The object assigned to JNDI name '" + jndiName
								+ "' does not implement " + businnessInterface.getName());
			}

			return Proxy.newProxyInstance(
						Thread.currentThread().getContextClassLoader(),
						new Class[] { businnessInterface },
						new EJBInvocationHandler(ejb));

	}

	
	private Object createEJBFromHome(Object home, Object[] createArgs) {
		try {
			Class[] args = new Class[createArgs.length];
			for (int i = 0; i < args.length; i++)
				args[i] = createArgs[i].getClass();
			
			return home.getClass()
						.getMethod("create", args)
						.invoke(home, createArgs);
		
		} catch (Exception e) {
			throw new RuntimeException("Error creating EJB from home.", e);
		}
	}

	
	/**
	 * Get the EJB that the specified Business Delegate proxies.
	 * 
	 * @param delegate a dynamic Business Delegate object generated with
	 *                 this class.
	 * 
	 * @throws IllegalArgumentException if the parameter is not a dynamic
	 *         delegate generated with this class.
	 * 
	 * @return a reference of the bean's client interface (local or remote).
	 */
	public Object getBean(Object delegate) throws IllegalArgumentException {	
		return getInvocetionHandler(delegate).getEJB();
	}
	
	
	/**
	 * Remove the EJB behind the specified dynamic delegate object.
	 * <p>
	 * Since this method calls <code>remove()</code> on the bean's client
	 * interface, you cannot invoke methods on the specified delegate object
	 * after this method is invoked.</p>
	 * 
	 * @param delegate the delegate that proxies the bean that we want to
	 *                 remove.
	 * 
	 * @throws IllegalArgumentException if the parameter is not a dynamic
	 *         delegate generated with this class.
	 */
	public void remove(Object delegate) throws IllegalArgumentException {
		Object ejb = getInvocetionHandler(delegate).getEJB();
		try {
			ejb.getClass().getMethod("remove", new Class[0])
							.invoke(ejb, new Object[0]);
		} catch (Exception ignore) { }
	}

	
	private EJBInvocationHandler getInvocetionHandler(Object delegate)
	throws IllegalArgumentException {
		try {
			if (Proxy.isProxyClass(delegate.getClass()))
				return (EJBInvocationHandler) Proxy.getInvocationHandler(delegate);
		} catch (ClassCastException ignore) { }

		throw new IllegalArgumentException("The argument is not a dynamic delegate.");
	}

	private class EJBInvocationHandler implements InvocationHandler {

		private Object ejb;
		
		public EJBInvocationHandler(Object ejb) {
			this.ejb = ejb;
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {

			if (args == null)
				args = new Object[0];
			Class[] argsClass = new Class[args.length];
			for (int i = 0; i < args.length; i++)
				argsClass[i] = args[i].getClass();

			try {
				Method ejbMethod = ejb.getClass().getMethod(method.getName(),
						argsClass);
				if (ejbMethod == null)
					throw new NoSuchMethodException(
							"Could not find the specified method on the proxied EJB.");
				return ejbMethod.invoke(ejb, args);

			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {

				throw exceptionConverter.convert(e.getTargetException());
			}
		}

		public Object getEJB() {
			return ejb;
		}
	}

}
