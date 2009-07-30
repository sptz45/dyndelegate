package com.tzavellas.dyndelegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;

import org.easymock.MockControl;

import junit.framework.TestCase;

public class DynamicDelegateFactoryTest extends TestCase {

	public void testCreateLocalDelegateStringClassClass() throws Exception {
		
		MockControl beanControl = MockControl.createControl(MyEJBLocal.class);
		MyEJBLocal bean = (MyEJBLocal) beanControl.getMock();
		MockControl homeControl = MockControl.createControl(MyEJBLocalHome.class);
		MyEJBLocalHome home = (MyEJBLocalHome) homeControl.getMock();
		MockControl factoryControl = MockControl.createControl(EJBHomeFactory.class);
		EJBHomeFactory factory = (EJBHomeFactory) factoryControl.getMock();
		
		factory.lookupLocal("ejb/MyEJBLocalHome", MyEJBLocalHome.class);
		factoryControl.setReturnValue(home);
		home.create();
		homeControl.setReturnValue(bean);
		bean.businessMethod();
		bean.businessMethod2("arg");
		
		factoryControl.replay();
		homeControl.replay();
		beanControl.replay();
		
		DynamicDelegateFactory delegateFactory = new DynamicDelegateFactory();
		delegateFactory.setHomeFactory(factory);
		BusinessInterface delegate = (BusinessInterface)
			delegateFactory.createLocalDelegate("ejb/MyEJBLocalHome",
												MyEJBLocalHome.class,
												BusinessInterface.class);
		delegate.businessMethod();
		delegate.businessMethod2("arg");
		
		factoryControl.verify();
		homeControl.verify();
		beanControl.verify();
	}
	
	
	public void testCreateLocalDelegateStringClassClassObjectArray() throws Exception {
		
		MockControl beanControl = MockControl.createControl(MyEJBLocal.class);
		MyEJBLocal bean = (MyEJBLocal) beanControl.getMock();
		MockControl homeControl = MockControl.createControl(MyEJBLocalHome.class);
		MyEJBLocalHome home = (MyEJBLocalHome) homeControl.getMock();
		MockControl factoryControl = MockControl.createControl(EJBHomeFactory.class);
		EJBHomeFactory factory = (EJBHomeFactory) factoryControl.getMock();
		
		factory.lookupLocal("ejb/MyEJBLocalHome", MyEJBLocalHome.class);
		factoryControl.setReturnValue(home);
		home.create("arg1");
		homeControl.setReturnValue(bean);
		bean.businessMethod();
		bean.businessMethod2("arg");
		
		factoryControl.replay();
		homeControl.replay();
		beanControl.replay();
		
		DynamicDelegateFactory delegateFactory = new DynamicDelegateFactory();
		delegateFactory.setHomeFactory(factory);
		BusinessInterface delegate = (BusinessInterface)
			delegateFactory.createLocalDelegate("ejb/MyEJBLocalHome",
												MyEJBLocalHome.class,
												BusinessInterface.class,
												new Object[]{"arg1"});
		delegate.businessMethod();
		delegate.businessMethod2("arg");
		
		factoryControl.verify();
		homeControl.verify();
		beanControl.verify();
	}
	
	public void testCreateRemoteDelegateStringClassClass() throws Exception {
		
		MockControl beanControl = MockControl.createControl(MyEJB.class);
		MyEJB bean = (MyEJB) beanControl.getMock();
		MockControl homeControl = MockControl.createControl(MyEJBHome.class);
		MyEJBHome home = (MyEJBHome) homeControl.getMock();
		MockControl factoryControl = MockControl.createControl(EJBHomeFactory.class);
		EJBHomeFactory factory = (EJBHomeFactory) factoryControl.getMock();
		
		factory.lookupRemote("ejb/MyEJBLocalHome", MyEJBHome.class);
		factoryControl.setReturnValue(home);
		home.create();
		homeControl.setReturnValue(bean);
		bean.businessMethod();
		bean.businessMethod2("arg");
		
		factoryControl.replay();
		homeControl.replay();
		beanControl.replay();
		
		DynamicDelegateFactory delegateFactory = new DynamicDelegateFactory();
		delegateFactory.setHomeFactory(factory);
		BusinessInterface delegate = (BusinessInterface)
				delegateFactory.createRemoteDelegate("ejb/MyEJBLocalHome",
													MyEJBHome.class,
													BusinessInterface.class);
		delegate.businessMethod();
		delegate.businessMethod2("arg");
		
		factoryControl.verify();
		homeControl.verify();
		beanControl.verify();
	}
	
	public void testCreateRemoteDelegateStringClassClassObjectArray() throws Exception {
		
		MockControl beanControl = MockControl.createControl(MyEJB.class);
		MyEJB bean = (MyEJB) beanControl.getMock();
		MockControl homeControl = MockControl.createControl(MyEJBHome.class);
		MyEJBHome home = (MyEJBHome) homeControl.getMock();
		MockControl factoryControl = MockControl.createControl(EJBHomeFactory.class);
		EJBHomeFactory factory = (EJBHomeFactory) factoryControl.getMock();
		
		factory.lookupRemote("ejb/MyEJBLocalHome", MyEJBHome.class);
		factoryControl.setReturnValue(home);
		home.create("arg1");
		homeControl.setReturnValue(bean);
		bean.businessMethod();
		bean.businessMethod2("arg");
		
		factoryControl.replay();
		homeControl.replay();
		beanControl.replay();
		
		DynamicDelegateFactory delegateFactory = new DynamicDelegateFactory();
		delegateFactory.setHomeFactory(factory);
		BusinessInterface delegate = (BusinessInterface)
				delegateFactory.createRemoteDelegate("ejb/MyEJBLocalHome",
													MyEJBHome.class,
													BusinessInterface.class,
													new Object[]{"arg1"});
		delegate.businessMethod();
		delegate.businessMethod2("arg");
		
		factoryControl.verify();
		homeControl.verify();
		beanControl.verify();
	}
	
	
	public void testGetBean() throws Exception {
		
		MockControl beanControl = MockControl.createControl(MyEJBLocal.class);
		MyEJBLocal bean = (MyEJBLocal) beanControl.getMock();
		MockControl homeControl = MockControl.createControl(MyEJBLocalHome.class);
		MyEJBLocalHome home = (MyEJBLocalHome) homeControl.getMock();
		MockControl factoryControl = MockControl.createControl(EJBHomeFactory.class);
		EJBHomeFactory factory = (EJBHomeFactory) factoryControl.getMock();
		
		factory.lookupLocal("ejb/MyEJBLocalHome", MyEJBLocalHome.class);
		factoryControl.setReturnValue(home);
		home.create();
		homeControl.setReturnValue(bean);
		
		factoryControl.replay();
		homeControl.replay();
		beanControl.replay();
		
		DynamicDelegateFactory delegateFactory = new DynamicDelegateFactory();
		delegateFactory.setHomeFactory(factory);
		BusinessInterface delegate = (BusinessInterface) delegateFactory.createLocalDelegate("ejb/MyEJBLocalHome", MyEJBLocalHome.class, BusinessInterface.class);
		assertSame(bean, delegateFactory.getBean(delegate));
		
		factoryControl.verify();
		homeControl.verify();
		beanControl.verify();
		
		try {
			delegateFactory.getBean("illegal");
			fail("Should throw an IllegalArgumentException, since the argument is not a dynamic delegate.");
		} catch (IllegalArgumentException expected) {}
	}
	
	public void testRemove() throws Exception {
		
		MockControl beanControl = MockControl.createControl(MyEJBLocal.class);
		MyEJBLocal bean = (MyEJBLocal) beanControl.getMock();
		MockControl homeControl = MockControl.createControl(MyEJBLocalHome.class);
		MyEJBLocalHome home = (MyEJBLocalHome) homeControl.getMock();
		MockControl factoryControl = MockControl.createControl(EJBHomeFactory.class);
		EJBHomeFactory factory = (EJBHomeFactory) factoryControl.getMock();
		
		factory.lookupLocal("ejb/MyEJBLocalHome", MyEJBLocalHome.class);
		factoryControl.setReturnValue(home);
		home.create();
		homeControl.setReturnValue(bean);
		bean.remove();
		
		factoryControl.replay();
		homeControl.replay();
		beanControl.replay();
		
		DynamicDelegateFactory delegateFactory = new DynamicDelegateFactory();
		delegateFactory.setHomeFactory(factory);
		BusinessInterface delegate = (BusinessInterface) delegateFactory.createLocalDelegate("ejb/MyEJBLocalHome", MyEJBLocalHome.class, BusinessInterface.class);
		delegateFactory.remove(delegate);
		
		factoryControl.verify();
		homeControl.verify();
		beanControl.verify();
		
		try {
			delegateFactory.remove("illegal");
			fail("Should throw an IllegalArgumentException, since the argument is not a dynamic delegate.");
		} catch (IllegalArgumentException expected) {}
	}
	
	
	
	static interface BusinessInterface {
		void businessMethod();
		void businessMethod2(String arg);
	}
	static interface MyEJBLocal extends EJBLocalObject, BusinessInterface { }
	static interface MyEJBLocalHome extends EJBLocalHome {
		Object create() throws CreateException;
		Object create(String arg1) throws CreateException;
	}
	static interface MyEJB extends EJBLocalObject, BusinessInterface { }
	static interface MyEJBHome extends EJBHome {
		Object create() throws CreateException, RemoteException;
		Object create(String arg1) throws CreateException, RemoteException;
	}

}
