package com.tzavellas.dyndelegate;

import junit.framework.TestCase;

public class RuntimeExceptionWrapperTest extends TestCase {

	public void testConvert() {
		
		ThrowableConverter converter = new RuntimeExceptionWrapper();
		Throwable cause = new Throwable();
		Throwable converted = converter.convert(cause);
		
		assertTrue("The converted exception is not an instance of RuntimeException",
				converted instanceof RuntimeException);
		
		assertSame(cause, converted.getCause());
	}
}
