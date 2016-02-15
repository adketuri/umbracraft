package net.alcuria.umbracraft.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Marks a field as ignored for the generic populate methods. Ignored fields,
 * like private fields, will not generate editable textfields. The only
 * difference of course is that private fields require getters/setters to access
 * and this bypasses that restriction.
 * @author Andrew Keturi */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IgnorePopulate {

}
