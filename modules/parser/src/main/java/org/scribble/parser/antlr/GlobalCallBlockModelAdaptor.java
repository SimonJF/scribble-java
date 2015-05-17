/*
 * Copyright 2015 simon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scribble.parser.antlr;

import org.antlr.runtime.CommonToken;
import org.scribble.model.Message;
import org.scribble.model.PayloadElement;
import org.scribble.model.Role;
import org.scribble.model.global.GBlock;
import org.scribble.model.global.GCallBlock;

/**
 *
 * @author simon
 */
public class GlobalCallBlockModelAdaptor extends AbstractModelAdaptor {

    @Override
    public Object createModelObject(ParserContext context) {
        GCallBlock ret = new GCallBlock();
        ret.setBlock(new GBlock());
        setEndProperties(ret, context.peek());
        Object lastToken = context.peek();
        // There are two forms of call.
        // In the one, there are no intermediate messages between receiving
        // the call request and sending the response.
        // In the other, we have a block of activities happening between the
        // call and response.
        if (lastToken instanceof GBlock) {
            ret.setBlock((GBlock)context.pop());
        } else if (lastToken instanceof CommonToken && 

                ((CommonToken)lastToken).getText().equals(";")) {
            context.pop(); // ;
        }

        // This bit is common to both cases. Get the callee and caller, and msg.
        Role callee = new Role();
        CommonToken calleeToken = (CommonToken) context.pop();
        callee.setName(calleeToken.getText());

        context.pop(); // to
        Role caller = new Role();
        CommonToken callerToken = (CommonToken) context.pop();
        caller.setName(callerToken.getText());
        context.pop(); // from  
        PayloadElement responseType = (PayloadElement) context.pop();
        context.pop(); // returning
        Message msg = (Message) context.pop();

        ret.setCallee(callee);
        ret.setCaller(caller);
        ret.setRequestName(msg.getMessageSignature().getOperator());
        ret.setRequestTypes(msg.getMessageSignature().getPayloadElements());
        ret.setResponseType(responseType);

        setStartProperties(ret, context.pop()); // call
        context.push(ret);
        return ret;
    }
}
