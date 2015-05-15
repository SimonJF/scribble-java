/*
 * Copyright 2009 www.scribble.org
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
package org.scribble.model.local;

import org.scribble.model.Message;
import org.scribble.model.Role;

/**
 * This class represents an interaction: the communication
 * of a message from one role to another, or several others.
 * 
 */
public abstract class LCallSend extends LActivity {

    private Message _message=null;
    private Role _toRole;
    //private java.util.List<Role> _toRoles=new java.util.ArrayList<Role>();

    /**
     * The default constructor.
     */
    public LCallSend() {
    }

    /**
     * The copy constructor.
     * 
     * @param i The interaction to copy
     */
    public LCallSend(LCallSend i) {
        super(i);
        
        if (i._message != null) {
            _message = new Message(i._message);
        }
        
        if (i._toRole != null) {
            _toRole = i._toRole;
        }
    }

    /**
     * This constructor initializes the 'to' role and message
     * signature.
     * 
     * @param sig The message signature
     * @param toRole The 'to' role
     */
    public LCallSend(Message sig, Role toRole) {
        _message = sig;
        _toRole = toRole;
    }

    /**
     * This method returns the message.
     * 
     * @return The message
     */
    public Message getMessage() {
        return (_message);
    }
    
    /**
     * This method sets the message.
     * 
     * @param message The message
     */
    public void setMessage(Message message) {
        
        if (_message != null) {
            _message.setParent(null);
        }
        
        _message = message;
        
        if (_message != null) {
            _message.setParent(this);
        }
    }
    
    /**
     * This method returns the 'to' role.
     * 
     * @return The 'to' role
     */
    public Role getToRole() {
        return _toRole;
    }
    
    /**
     * This method sets the 'to' roles.
     * 
     * @param part The 'to' role
     */
    public void setToRole(Role toRole) {
    	_toRole = toRole;
    }

    @Override
    public int hashCode() {
            int hash = 7;
            return hash;
    }

    @Override
    public boolean equals(Object obj) {
            if (obj == null) {
                    return false;
            }
            if (getClass() != obj.getClass()) {
                    return false;
            }
            final LCallSend other = (LCallSend) obj;
            if (this._message != other._message && (this._message == null || !this._message.equals(other._message))) {
                    return false;
            }
            if (this._toRole != other._toRole && (this._toRole == null || !this._toRole.equals(other._toRole))) {
                    return false;
            }
            return true;
    }


    
    @Override
    public abstract void toText(StringBuffer buf, int level);
    
    @Override
    public abstract void visit(LVisitor visitor);

    @Override
    public abstract String toString();
	/**
	 * {@inheritDoc}
	 */
    /*
    public void toText(StringBuffer buf, int level) {
		
    	indent(buf, level);
    	
    	_message.toText(buf, level);
    	
    	if (_toRoles != null) {
    		buf.append(" to ");
            for (int i=0; i < getToRoles().size(); i++) {
            	if (i > 0) {
            		buf.append(",");
            	}
            	_toRoles.get(i).toText(buf, level);
            }
    		
    	}
    	
		buf.append(";\n");
	}

    public void visit(LVisitor visitor) {
        visitor.accept(this);
        
        if (getMessage() != null) {
            getMessage().visit(visitor);
        }
    }
/*
    @Override
    public String toString() {
        StringBuffer ret=new StringBuffer();
        
        if (getMessage() != null) {
            ret.append(getMessage());
        }
        
        ret.append(" to ");
        
        for (int i=0; i < getToRoles().size(); i++) {
        	if (i > 0) {
        		ret.append(",");
        	}
        	ret.append(getToRoles().get(i));
        }
        
        return (ret.toString());
    }
    */
}
