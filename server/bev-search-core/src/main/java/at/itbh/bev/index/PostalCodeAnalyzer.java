package at.itbh.bev.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;

/**
 * Tokenize as one unit, convert to lower case and build edge 3-4 n-grams
 * starting from the left. An Austrian postal code is therefore split into <code>xxx</code>
 * and <code>xxxx</code>.
 * 
 * @author Christoph D. Hermann (ITBH) <christoph.hermann@itbh.at>
 *
 */
public class PostalCodeAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new KeywordTokenizer();
		TokenStream filter = new LowerCaseFilter(source);
		filter = new EdgeNGramTokenFilter(filter, 3, 4);
		return new TokenStreamComponents(source, filter);
	}
}