package net.alcuria.umbracraft.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Indicates that a field should show a tooltip
 * @author Andrew Keturi */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Tooltip {
	public String value();
}
