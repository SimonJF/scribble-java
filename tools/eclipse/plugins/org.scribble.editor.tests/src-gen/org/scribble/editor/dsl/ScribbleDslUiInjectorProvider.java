/*
* generated by Xtext
*/
package org.scribble.editor.dsl;

import org.eclipse.xtext.junit4.IInjectorProvider;

import com.google.inject.Injector;

public class ScribbleDslUiInjectorProvider implements IInjectorProvider {
	
	public Injector getInjector() {
		return org.scribble.editor.dsl.ui.internal.ScribbleDslActivator.getInstance().getInjector("org.scribble.editor.dsl.ScribbleDsl");
	}
	
}