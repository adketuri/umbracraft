package net.alcuria.umbracraft.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Hints at some suggestions for a field in the editor.
 * @author Andrew Keturi */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Suggest {
	public String value();
}
