package at.itbh.bev.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;

/**
 * Tokenizes as one unit, convert to lower case, remove some whitespace
 * characters and replace words by slashes. Replacing chars by <code>/</code>
 * leads sometimes to multiple adjacent occurrences of slashes. These are
 * reduced to one slash.
 * 
 * @author Christoph D. Hermann (ITBH) <christoph.hermann@itbh.at>
 *
 */
public class HouseIdExactMatchAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new KeywordTokenizer();
		TokenStream filter = new LowerCaseFilter(source);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.houseIdExactRemovePattern, "", true);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.replacePattern, "/", true);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.uniquifyNonWordCharPattern, "$1", true);
		return new TokenStreamComponents(source, filter);
	}
}