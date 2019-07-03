package net.modificationstation.stationmodloader.mixture;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Mixture {
	Class<?> value();
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Intervene {
		public enum ShiftType {
			BEFORE,
			OVERWRITE,
			AFTER
		}
		String obfuscated() default "";
		ShiftType shift() default ShiftType.OVERWRITE;
		public @interface At {
			String value();
			String target() default "";
			int opcode() default -1;
		}
		At at() default @At("");
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Redirect {
		String obfuscated() default "";
	}
}
