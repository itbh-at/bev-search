/* 
 * RegexPatternCollection.java
 *  
 * Copyright (C) 2017 Christoph D. Hermann <christoph.hermann@itbh.at>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
