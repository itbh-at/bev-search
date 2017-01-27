package at.itbh.bev.index;

import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.phonetic.PhoneticFilter;

/**
 * Tokenizes as one unit, converts to lower case, removes all non-alphabetic
 * characters, applies the {@link ColognePhonetic} filter and builds 2-4
 * n-grams.
 * 
 * @author Christoph D. Hermann (ITBH) <christoph.hermann@itbh.at>
 *
 */
public class TextAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new KeywordTokenizer();
		TokenStream filter = new LowerCaseFilter(source);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.addressLineStemmingPattern, "", true);
		filter = new PatternReplaceFilter(filter, RegexPatternCollection.nonAlphaCharPattern, "", true);
		filter = new PhoneticFilter(filter, new ColognePhonetic(), true);
		filter = new NGramTokenFilter(filter, 2, 6);
		return new TokenStreamComponents(source, filter);
	}

}