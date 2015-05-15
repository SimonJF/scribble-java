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
package org.scribble.model.global;

import java.util.List;
import java.util.Set;
import org.scribble.model.PayloadElement;
import org.scribble.model.Role;
import org.scribble.model.RoleDecl;

/**
 * A synchronous call block
 */
public class GCallBlock extends GSinglePathActivity {
    
    private GBlock _block;
    private Role _caller;
    private Role _callee;
    private String _requestName;
    private List<PayloadElement> _requestTypes;
    private PayloadElement _responseType;

    public GCallBlock(GBlock block, Role caller, Role callee,
            String requestName, List<PayloadElement> requestTypes,
            PayloadElement responseType) {
        this._block = block;
        this._caller = caller;
        this._callee = callee;
        this._requestName = requestName;
        this._requestTypes = requestTypes;
        this._responseType = responseType;
    }

    public GCallBlock() {
    }

    public void setBlock(GBlock _block) {
        this._block = _block;
    }

    public void setCaller(Role _caller) {
        this._caller = _caller;
    }

    public void setCallee(Role _callee) {
        this._callee = _callee;
    }

    public void setRequestName(String _requestName) {
        this._requestName = _requestName;
    }

    public void setRequestTypes(List<PayloadElement> _requestTypes) {
        this._requestTypes = _requestTypes;
    }

    public void setResponseType(PayloadElement _responseType) {
        this._responseType = _responseType;
    }

    

    public Role getCaller() {
        return _caller;
    }

    public Role getCallee() {
        return _callee;
    }

    public String getRequestName() {
        return _requestName;
    }

    public List<PayloadElement> getRequestTypes() {
        return _requestTypes;
    }

    public PayloadElement getResponseType() {
        return _responseType;
    }

   
    
    @Override
    public GBlock getBlock() {
        return _block;
    }

    @Override
    public boolean isRoleInvolved(RoleDecl role) {
        if (role.isRole(_caller) || role.isRole(_callee)) {
            return true;
        }

        if (_block != null) {
            return _block.isRoleInvolved(role);
        }
        return false;
    }

    @Override
    public void identifyInvolvedRoles(Set<Role> roles) {
        roles.add(_caller);
        roles.add(_callee);
        if (_block != null) {
            _block.identifyInvolvedRoles(roles);
        }
    }

    @Override
    public void visit(GVisitor visitor) {
        if (visitor.start(this)) {
	    
	        if (getBlock() != null) {
	            getBlock().visit(visitor);
	        }
	    }
	    visitor.end(this);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("call ");
        ret.append(_requestName);
        ret.append("(");

        int typesLength = _requestTypes.size();
        int i = 0;
        for (PayloadElement requestType : _requestTypes) {
            ret.append(requestType.toString());
            i++;
            if (i < typesLength) {
                ret.append(", ");
            }
        }
        
        ret.append(")");
        ret.append(" returning ");
        ret.append(_responseType.getName());
        ret.append(" from ");
        ret.append(_caller.getName());
        ret.append(" to ");
        ret.append(_callee.getName());

        if (_block == null) {
            ret.append(";");
        } else {
            ret.append(_block.toString());
        }

        return ret.toString();
    }

	/**
	 * {@inheritDoc}
	 */
    public void toText(StringBuffer buf, int level) {
		
    	indent(buf, level);
        buf.append("call ");
        buf.append(_requestName);
        buf.append("(");

        int typesLength = _requestTypes.size();
        int i = 0;
        for (PayloadElement requestType : _requestTypes) {
            buf.append(requestType.toString());
            i++;
            if (i < typesLength) {
                buf.append(", ");
            }
        }
        
        buf.append(")");
        buf.append(" returning ");
        buf.append(_responseType.getName());
        buf.append(" from ");
        buf.append(_caller.getName());
        buf.append(" to ");
        buf.append(_callee.getName());

        if (_block != null) {
            buf.append(";");
        } else {
            buf.append(_block.toString());
        }

        buf.append("\n");
	} 
}
