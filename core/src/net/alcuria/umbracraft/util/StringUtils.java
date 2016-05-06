package net.alcuria.umbracraft.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;

/** Utility functions for {@link String} objects.
 * @author Andrew Keturi */
public class StringUtils {

	/** Makes the name of the definition a little nicer.
	 * @param name the class definition name
	 * @return the name without "Definition" and some added whitespace */
	public static CharSequence formatName(String name) {
		if (name.contains("Definition")) {
			name = name.replaceAll("Definition", "");
		}
		return splitCamelCase(name);
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

	/** Given an id in the form {argN} where N is an index into the args array,
	 * this returns the arg at that index.
	 * @param id the zero-based index
	 * @param arguments the {@link Entity} object's arguments
	 * @return the argument at that index */
	public static String replaceArgs(String id, Array<String> arguments) {
		Pattern pattern = Pattern.compile("\\{arg[0-9]\\}");
		Matcher matcher = pattern.matcher(id);
		while (matcher.find()) {
			String match = matcher.group();
			String number = match.replace("{arg", "").replace("}", "");
			if (StringUtils.isNumber(number)) {
				int idx = Integer.valueOf(number);
				if (idx >= 0 && idx < arguments.size) {
					id = id.replace(match, arguments.get(idx));
				} else {
					throw new ArrayIndexOutOfBoundsException("No argument at index: " + idx + ". size: " + arguments.size);
				}
			} else {
				throw new NumberFormatException("String is not a number: " + id);
			}
		}
		return id;

	}

	/** Splits a string that's in camel-case.
	 * @param s a {@link String}
	 * @return */
	public static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	/** Truncates a string, appending ellipses if it's too long. */
	public static String truncate(String string, int length) {
		if (string == null) {
			return "";
		}
		return string.length() < length ? string : string.substring(0, length) + "...";
	}
}
