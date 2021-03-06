/*
* generated by Xtext
*/
package org.scribble.trace.editor.dsl.parser.antlr;

import com.google.inject.Inject;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.scribble.trace.editor.dsl.services.ScribbleTraceDslGrammarAccess;

public class ScribbleTraceDslParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private ScribbleTraceDslGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	
	@Override
	protected org.scribble.trace.editor.dsl.parser.antlr.internal.InternalScribbleTraceDslParser createParser(XtextTokenStream stream) {
		return new org.scribble.trace.editor.dsl.parser.antlr.internal.InternalScribbleTraceDslParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "Trace";
	}
	
	public ScribbleTraceDslGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(ScribbleTraceDslGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
}
