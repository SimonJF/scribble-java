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
package org.scribble.model.global;

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
public class GInitiates1 extends GActivity {
    private String _subsessionName = null;
    private final List<RoleInstantiation> _roleInstantiationList =
            new ArrayList<RoleInstantiation>();
    private Role _role=null;

    /**
     * This is the default constructor.
     * 
     */
    public GInitiates1() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRoleInvolved(RoleDecl role) {
    	boolean ret=false;
    	
    	if (_role != null) {
    		ret = role.isRole(_role);
    	}
    	
    	return (ret);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void identifyInvolvedRoles(java.util.Set<Role> roles) {
    	
    	if (_role != null) {
    		roles.add(_role);
    	}

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

    public List<RoleInstantiation> getRoleInstantiationList() {
        return _roleInstantiationList;
    }

    
    /**
     * This method visits the model object using the supplied
     * visitor.
     * 
     * @param visitor The visitor
     */
    @Override
    public void visit(GVisitor visitor) {

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
            
            for (int i=0; i < getRoleInstantiationList().size(); i++) {
                RoleInstantiation role= getRoleInstantiationList().get(i);
                
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
            
            for (int i=0; i < getRoleInstantiationList().size(); i++) {
                RoleInstantiation role= getRoleInstantiationList().get(i);
                
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
            
            buf.append("); ");

        }
		buf.append("\n");
	}
}
