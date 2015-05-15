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
package org.scribble.model.local;

import org.scribble.model.Message;

/**
 *
 * @author simon
 */
public class LCallRespSend extends LCallSend {
    /**
    * {@inheritDoc}
    */
    public void toText(StringBuffer buf, int level) {
		
    	indent(buf, level);
        buf.append("send_call_response ");
        Message message = getMessage();	
    	message.toText(buf, level);
        
        buf.append(" to ");
        buf.append(getToRole());
    
		buf.append(";\n");
	}

    @Override
    public String toString() {
        StringBuffer ret=new StringBuffer();

        ret.append("send_call_response ");
        
        if (getMessage() != null) {
            ret.append(getMessage());
        }
        
        ret.append(" to ");
        ret.append(getToRole());

        return (ret.toString());
    }

	@Override
    public void visit(LVisitor visitor) {
        visitor.accept(this);
        
        if (getMessage() != null) {
            getMessage().visit(visitor);
        }
    }
    
}
