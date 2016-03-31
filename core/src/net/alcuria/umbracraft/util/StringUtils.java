package net.alcuria.umbracraft.util;

/** Utility functions for {@link String} objects.
 * @author Andrew Keturi */
public class StringUtils {

	/** Makes the name of the definition a little nicer.
	 * @param name the class definition name
	 * @return the name without "Definition" and some added whitespace */
	public static CharSequence formatName(String name) {
		name = name.replaceAll("Definition", "");
		for (int i = 0; i < name.length(); i++) {
			if (i != 0 && Character.isUpperCase(name.charAt(i))) {
				name = name.replace(Character.toString(name.charAt(i)), " " + name.charAt(i));
				i += 2;
			}
		}
		return name;
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

	/** @param str a string
	 * @return <code>true</code> if the {@link String} is a number */
	public static boolean isNumber(String str) {
		return isNotEmpty(str) && str.matches("^[+-]?\\d+$");
	}

	/** Truncates a string, appending ellipses if it's too long. */
	public static String truncate(String string, int length) {
		if (string == null) {
			return "";
		}
		return string.length() < length ? string : string.substring(0, length) + "...";
	}

}
