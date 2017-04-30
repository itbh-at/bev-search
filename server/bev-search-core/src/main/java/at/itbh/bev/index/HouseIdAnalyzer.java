/* 
 * HouseIdAnalyzer.java
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;

/**
 * Tokenizes as one unit, converts to lower case, removes some whitespace
 * characters and replaces words by slashes. Replacing chars by <code>/</code>
 * leads sometimes to multiple adjacent occurrences of slashes. These are
 * reduced to one slash. After that it builds edge 1-4 n-grams
 * 
 * @author Christoph D. Hermann (ITBH) <christoph.hermann@itbh.at>
 *
 */
public class HouseIdAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new KeywordTokenizer();
		TokenStream filter = new LowerCaseFilter(source);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.houseIdExactRemovePattern, "", true);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.replacePattern, "/", true);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.uniquifyNonWordCharPattern, "$1", true);
		filter = new EdgeNGramTokenFilter(filter, 1, 4);
		return new TokenStreamComponents(source, filter);
	}
}