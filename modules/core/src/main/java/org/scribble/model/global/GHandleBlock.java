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

import java.util.Set;
import org.scribble.model.Role;
import org.scribble.model.RoleDecl;

/**
 *
 * @author simon
 */
public class GHandleBlock extends GSinglePathActivity {
    private String _failureName = null;
    private final GBlock _block = new GBlock();
    
    public GHandleBlock() {};

    public String getFailureName() {
        return _failureName;
    }

    public void setFailureName(String _failureName) {
        this._failureName = _failureName;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this._failureName != null ? this._failureName.hashCode() : 0);
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
        final GHandleBlock other = (GHandleBlock) obj;
        if ((this._failureName == null) ? (other._failureName != null) : !this._failureName.equals(other._failureName)) {
            return false;
        }
        return true;
    }

    @Override
    public GBlock getBlock() {
        return _block;
    }

    @Override
    public boolean isRoleInvolved(RoleDecl role) {
        return _block.isRoleInvolved(role);
    }

    @Override
    public void identifyInvolvedRoles(Set<Role> roles) {
        _block.identifyInvolvedRoles(roles);
    }

    @Override
    public void visit(GVisitor visitor) {
        _block.visit(visitor);
    }


    
    @Override
    public void toText(StringBuffer buf, int level) {
        buf.append("handle (");
        buf.append(_failureName);
        buf.append(") ");
        _block.toText(buf, level);
    }

    @Override
    public String toString() {
        String ret = "handle (";
        ret += _failureName;
        ret += ") ";
        ret += _block.toString();
        return ret;
    }
}
