package at.itbh.bev.index;

import java.util.regex.Pattern;

public final class RegexPatternCollection {

	/**
	 * Matches space, dot, ? and !
	 */
	public static final Pattern houseIdExactRemovePattern = Pattern.compile("\\s|\\.|\\?|\\!");


	/**
	 * Matches all house number separators used by BEV in lower case without dots.
	 */
	public static final Pattern replacePattern = Pattern
			.compile("pavillon|stg|block|los|reihe|parz|stand|haus|obj|weg|gruppe|ladenzeile|stiege|objekt|parzelle");

	/**
	 * Matches multiple occurrences of non word characters. <code>$1</code>
	 * contains the strings without doubled non word characters.
	 */
	public static final Pattern uniquifyNonWordCharPattern = Pattern.compile("([\\W])\\1+");

	/**
	 * Removes common endings on address lines like "straße", "straße", "str.",
	 * "gasse", "g.", etc.
	 */
	public static final Pattern addressLineStemmingPattern = Pattern
			.compile("strasse|straße|str\\.|str\\b|gasse|g\\.|dorf|platz|stadt|zentrum");

	/**
	 * Matches all non-alphabetical characters (whitespace characters, digits, etc.)
	 */
	public static final Pattern nonAlphaCharPattern = Pattern.compile("\\W|\\d");
}
