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

import java.util.ArrayList;
import java.util.List;
import org.scribble.model.ContainmentList;
import org.scribble.model.Role;
import org.scribble.model.RoleDecl;
import org.scribble.model.RoleInstantiation;

/**
 * This class represents the Choice construct between
 * two or more paths.
 * 
 */
public class LInitiates1 extends LActivity {
    private String _subsessionName = null;
    private List<RoleInstantiation> _roleInstantiationList = null;
    private Role _role=null;
    private final LBlock _block = new LBlock();
    private final java.util.List<LHandleBlock> _handleBlocks =
            new ContainmentList<LHandleBlock>(this, LHandleBlock.class);

    /**
     * This is the default constructor.
     * 
     */
    public LInitiates1() {
    }
    
    /**
     * This method returns the role.
     * 
     * @return The role
     */
    public Role getRole() {
        return (_role);
    }
    
    /**
     * This method sets the role.
     * 
     * @param role The role
     */
    public void setRole(Role role) {
        _role = role;
    }

    public String getSubsessionName() {
        return _subsessionName;
    }

    public void setSubsessionName(String _subsessionName) {
        this._subsessionName = _subsessionName;
    }

    public List<RoleInstantiation> getSubsessionRoles() {
        return _roleInstantiationList;
    }

    public void setSubsessionRoles(List<RoleInstantiation> _roleInstantiationList) {
        this._roleInstantiationList = _roleInstantiationList;
    }

    public List<RoleInstantiation> getRoleInstantiationList() {
        return _roleInstantiationList;
    }

    public void setRoleInstantiationList(List<RoleInstantiation> _roleInstantiationList) {
        this._roleInstantiationList = _roleInstantiationList;
    }

    /**
     * This method visits the model object using the supplied
     * visitor.
     * 
     * @param visitor The visitor
     */
    @Override
    public void visit(LVisitor visitor) {

    }
   
    @Override
    public String toString() {
        String result = "";
        if (_role != null) {
            result += _role + " initiates ";
        }
        if (_subsessionName != null) {
            result += _subsessionName;
            result += "( ";
            
            for (int i=0; i < getSubsessionRoles().size(); i++) {
                RoleInstantiation role= getSubsessionRoles().get(i);
                
                if (i > 0) {
                    result += ",";
                }
                
                result += ("role "+role.getName()+" ");
                if (role.getAlias() != null) {
                    result += " as "+role.getAlias();
                }
            }
            
            result += ");\n";

        }

        return result;
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void toText(StringBuffer buf, int level) {
		
    	indent(buf, level);
    	
    	
    	if (_role != null) {
    		buf.append(_role);
    		buf.append(" initiates ");
    	}

        if (_subsessionName != null) {
            buf.append(_subsessionName);
            buf.append("( ");
            
            for (int i=0; i < getSubsessionRoles().size(); i++) {
                RoleInstantiation role= getSubsessionRoles().get(i);
                
                if (i > 0) {
                    buf.append(", ");
                }
               
                if (role.isNew()) {
                    buf.append("new ");
                }
                buf.append(role.getName());
                buf.append(" ");
                if (role.getAlias() != null) {
                    buf.append(" as ");
                    buf.append(role.getAlias());
                }
            }
            
            buf.append(");");
        }    	
		buf.append("\n");
	}
}
