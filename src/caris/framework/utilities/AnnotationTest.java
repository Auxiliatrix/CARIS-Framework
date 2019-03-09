package caris.framework.utilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public class AnnotationTest {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		AnnotatedClass a = new AnnotatedClass(true);
		Method m = a.getClass().getMethod("testFunction");
		TestAnnotation t = m.getAnnotation(TestAnnotation.class);
		System.out.println(t.value());
	}
	
	@ClassAnnotation(name = "")
	public static class AnnotatedClass {
		
		private boolean set;
		
		public AnnotatedClass(boolean set) {
			this.set = set;
		}
		
		@TestAnnotation(value = 1)
		public void testFunction() {
			System.out.println("Hello!");
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface TestAnnotation {
		int value() default 0;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface ClassAnnotation {
		String name();
	}
	
}
