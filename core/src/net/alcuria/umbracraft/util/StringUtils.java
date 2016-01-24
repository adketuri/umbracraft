package net.alcuria.umbracraft.util;

/** Utility functions for {@link String} objects.
 * @author Andrew Keturi */
public class StringUtils {

	/** @param str a string
	 * @return <code>true</code> if the {@link String} is a digit */
	public static boolean isDigit(String str) {
		return isNotEmpty(str) && str.matches("^[+-]?\\d+$");
	}

	/** Checks whether or not a string is empty.
	 * @param string a string to check
	 * @return <code>true</code> if the string is both non-null and contains
	 *         non-space characters. Strings like "" and " " return
	 *         <code>false</code>, whereas Strings like " A " will still return
	 *         <code>true</code>. */
	public static boolean isNotEmpty(String string) {
		return string != null && string.trim().length() > 0;
	}

	/** Truncates a string, appending ellipses if it's too long. */
	public static String truncate(String string, int length) {
		if (string == null) {
			return "";
		}
		return string.length() < length ? string : string.substring(0, length) + "...";
	}

}
