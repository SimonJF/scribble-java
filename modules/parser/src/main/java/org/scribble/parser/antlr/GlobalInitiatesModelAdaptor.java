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

import java.util.List;
import org.antlr.runtime.CommonToken;
import org.scribble.model.Role;
import org.scribble.model.RoleInstantiation;
import org.scribble.model.global.GBlock;
import org.scribble.model.global.GHandleBlock;
import org.scribble.model.global.GInitiates;
import org.scribble.model.global.GProtocolInstance;

/**
 * This class provides the model adapter for the 'choice' parser rule.
 *
 */
public class GlobalInitiatesModelAdaptor extends AbstractModelAdaptor {

	/**
	 * {@inheritDoc}
	 */
	public Object createModelObject(ParserContext context) {
        GInitiates ret = new GInitiates();
		
		setEndProperties(ret, context.peek());
	
		while (context.peek() instanceof GBlock) {
			GBlock block = (GBlock) context.pop();
            Object peek1 = context.peek();
            if (peek1 instanceof CommonToken) {
                // A handle block
                CommonToken tokPeek1 = (CommonToken) peek1;
                String exceptionName = tokPeek1.getText();
                context.pop(); // handle
                GHandleBlock handleBlock = new GHandleBlock();
                handleBlock.setFailureName(exceptionName);
                handleBlock.getContents().addAll(block.getContents());
            } else {
                // The success block
                ret.getBlock().getContents().addAll(block.getContents());
            }
		}
		
        List<RoleInstantiation> roleInstantiationList =
                (List<RoleInstantiation>) context.pop();
        ret.getSubsessionRoles().addAll(roleInstantiationList);
        String subsessionName = ((CommonToken) context.pop()).getText();
        ret.setSubsessionName(subsessionName);

        context.pop(); // initiates
        
		Role r=new Role();
		setStartProperties(r, context.peek());
		setEndProperties(r, context.peek());
		
		r.setName(((CommonToken)context.pop()).getText());

		ret.setRole(r);
		
		setStartProperties(ret, context); // choice
		
		context.push(ret);
		
		return ret;
	}

}
