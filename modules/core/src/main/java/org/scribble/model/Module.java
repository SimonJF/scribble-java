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
package org.scribble.model;

/**
 * This class represents the base class for models associated with
 * specific notations. The details associated with the notation are
 * contained within derived classes.
 *
 */
public class Module extends ModelObject {
    
	private String _name=null;
    private java.util.List<ImportDecl> _imports=
            new ContainmentList<ImportDecl>(this, ImportDecl.class);
    private java.util.List<PayloadTypeDecl> _payloadTypes=
            new ContainmentList<PayloadTypeDecl>(this, PayloadTypeDecl.class);
    private java.util.List<ProtocolDecl> _protocols=
            new ContainmentList<ProtocolDecl>(this, ProtocolDecl.class);

    /**
     * The default constructor for the model.
     */
    public Module() {
    }
    
    /**
     * This method returns the fully qualified name.
     * 
     * @return The fully qualified name
     */
    public String getName() {
    	return (_name);
    }
    
    /**
     * This method sets the fully qualified name.
     * 
     * @param fqn The fully qualified name
     */
    public void setName(String fqn) {
    	_name = fqn;
    }
    
    /**
     * This method returns the last component of the fully
     * qualified name.
     * 
     * @return The local name
     */
    public String getLocalName() {
    	String ret=_name;
    	
    	if (_name != null) {
	    	int ind=_name.lastIndexOf('.');
	    	if (ind != -1) {
	    		ret = _name.substring(ind+1);
	    	}
    	}
    	
    	return (ret);
    }
    
    /**
     * This method returns the role associated with a local module.
     * If the module is a global module, then null is returned.
     * 
     * @return The located role, if the module is local
     */
    public Role getLocatedRole() {
    	Role ret=null;
    	
    	if (_protocols.size() > 0
    			&& _protocols.get(0) instanceof org.scribble.model.local.LProtocolDecl) {
    		ret = ((org.scribble.model.local.LProtocolDecl)_protocols.get(0)).getLocalRole();
    	}
    	
    	return (ret);
    }
    
    /**
     * This method returns the list of import definitions.
     * 
     * @return The import definitions
     */
    public java.util.List<ImportDecl> getImports() {
        return (_imports);
    }
    
    /**
     * This method returns the list of payload types.
     * 
     * @return The payload types
     */
    public java.util.List<PayloadTypeDecl> getPayloadTypeDeclarations() {
        return (_payloadTypes);
    }
    
    /**
     * This method returns the payload type associated
     * with the supplied name.
     * 
     * @param name The name
     * @return The payload type, or null if not found
     */
    public PayloadTypeDecl getTypeDeclaration(String name) {
    	for (PayloadTypeDecl ptd : _payloadTypes) {
    		if (ptd.getAlias() != null && ptd.getAlias().equals(name)) {
    			return (ptd);
    		}
    	}
    	
    	return (null);
    }
    
    /**
     * This method returns the list of protocols.
     * 
     * @return The protocols
     */
    public java.util.List<ProtocolDecl> getProtocols() {
        return (_protocols);
    }
    
    /**
     * This method returns the protocol associated
     * with the supplied name.
     * 
     * @param name The name
     * @return The protocol, or null if not found
     */
    public ProtocolDecl getProtocol(String name) {
    	for (ProtocolDecl p : _protocols) {
    		if (p.getName().equals(name)) {
    			return (p);
    		}
    	}
    	
    	return (null);
    }
    
    /**
     * This method visits the model object using the supplied
     * visitor.
     * 
     * @param visitor The visitor
     */
    public void visit(Visitor visitor) {
        
        for (ImportDecl imp : getImports()) {
            imp.visit(visitor);
        }
        
        for (PayloadTypeDecl ptd : getPayloadTypeDeclarations()) {
        	ptd.visit(visitor);
        }
        
        for (ProtocolDecl protocol : getProtocols()) {
        	protocol.visit(visitor);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
    	StringBuffer buf=new StringBuffer();
    	toText(buf, 0);
    	
    	return (buf.toString());
    }
    
    /**
     * {@inheritDoc}
     */
    public void toText(StringBuffer buf, int level) {
    	if (_name != null) {
    		indent(buf, level);
    		
    		buf.append("module ");
    		
    		buf.append(_name);
    		
    		buf.append(";\n\n");
    	}
    	
    	for (ImportDecl imp : getImports()) {
    		imp.toText(buf, level);
    	}
    	
    	if (getImports().size() > 0) {
    		buf.append("\n");
    	}
    	
    	for (PayloadTypeDecl ptd : getPayloadTypeDeclarations()) {
    		ptd.toText(buf, level);
    	}
    	
    	if (getPayloadTypeDeclarations().size() > 0) {
    		buf.append("\n");
    	}
    	
    	for (int i=0; i < getProtocols().size(); i++) {
    		ProtocolDecl protocol=getProtocols().get(i);
    		if (i > 0) {
        		buf.append("\n");
    		}
    		protocol.toText(buf, level);
    	}
    }

}
