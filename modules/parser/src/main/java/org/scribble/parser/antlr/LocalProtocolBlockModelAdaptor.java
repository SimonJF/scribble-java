/*
 * Copyright 2009-11 www.scribble.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.scribble.parser.antlr;

import org.scribble.model.local.LActivity;
import org.scribble.model.local.LBlock;

/**
 * This class provides the model adapter for the 'localInteractionBlock' parser rule.
 *
 */
public class LocalProtocolBlockModelAdaptor extends AbstractModelAdaptor {

	/**
	 * {@inheritDoc}
	 */
	public Object createModelObject(ParserContext context) {
		LBlock ret=new LBlock();
		
		setEndProperties(ret, context.pop()); // consume }
		
		while (context.peek() instanceof LActivity) {
			ret.getContents().add(0, (LActivity)context.pop());
		}
		
		setStartProperties(ret, context.pop()); // consume {

		context.push(ret);
		
		return ret;
	}

}
