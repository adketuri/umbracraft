package net.alcuria.umbracraft.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Determines order fields are displayed (since the save actions want them
 * alphabetical). If not present it should revert to default ordering. Presence
 * of this field should assume priority.
 * @author Andrew Keturi */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Order {
	public int value();
}