package net.alcuria.umbracraft;

/** Utility functions for {@link String} objects.
 * @author Andrew Keturi */
public class StringUtils {

	/** truncates a string, appending ellipses if it's too long. */
	public static String truncate(String string, int length) {
		return string.length() < length ? string : string.substring(0, length) + "...";
	}

}
