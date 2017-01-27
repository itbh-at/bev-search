package at.itbh.bev.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;

/**
 * Tokenize as one unit, convert to lower case and remove non word
 * characters
 * 
 * @author Christoph D. Hermann (ITBH) <christoph.hermann@itbh.at>
 *
 */
public class AddressLineExactMatchAnalyzer extends Analyzer {
			
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new KeywordTokenizer();
		TokenStream filter = new LowerCaseFilter(source);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.nonAlphaCharPattern, "", true);
		return new TokenStreamComponents(source, filter);
	}
}