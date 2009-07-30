package com.tzavellas.dyndelegate;

import java.rmi.RemoteException;
import javax.ejb.EJBException;
import junit.framework.TestCase;

public class EJBExceptionConverterTest extends TestCase {

	private ThrowableConverter converter = new EJBExceptionConverter();

	
	public void testConvertRemote() {

		Throwable cause = new Throwable();
		RemoteException remote = new RemoteException("", cause);
		
		Throwable converted = converter.convert(remote);
		assertTrue(converted instanceof RuntimeException);
		assertSame(remote, converted.getCause());
		
		cause = new RuntimeException();
		remote = new RemoteException("", cause);
		converted = converter.convert(remote);
		
		assertSame(cause, converted);
	}
	
	
	public void testConvertLocal() {

		Exception cause = new Exception();
		EJBException ejb = new EJBException(cause);
		
		Throwable converted = converter.convert(ejb);
		assertTrue(converted instanceof RuntimeException);
		assertSame(ejb, converted);
		
		cause = new RuntimeException();
		ejb = new EJBException("", cause);
		converted = converter.convert(ejb);
		
		assertSame(cause, converted);
	}
	
	
	public void testConvertOthrer() {
		
		Throwable t = new Throwable();
		Throwable converted = converter.convert(t);
		assertSame(t, converted);
	}

}
