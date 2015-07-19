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

/**
 *
 * @author simon
 */
public class LHandleBlock extends LActivity {
    private String _failureName = null;
    private final LBlock _block = new LBlock();
    
    public LHandleBlock() {};

    public String getFailureName() {
        return _failureName;
    }

    public void setFailureName(String _failureName) {
        this._failureName = _failureName;
    }

    public LBlock getBlock() {
        return _block;
    }

    public void setBlock(LBlock block) {
        _block.getContents().addAll(block.getContents());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this._failureName != null ? this._failureName.hashCode() : 0);
        hash = 97 * hash + (this._block != null ? this._block.hashCode() : 0);
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
        final LHandleBlock other = (LHandleBlock) obj;
        if ((this._failureName == null) ? (other._failureName != null) : !this._failureName.equals(other._failureName)) {
            return false;
        }
        if (this._block != other._block && (this._block == null || !this._block.equals(other._block))) {
            return false;
        }
        return true;
    }


    @Override
    public void visit(LVisitor visitor) {
        _block.visit(visitor);
    }
    

    @Override
    public void toText(StringBuffer buf, int level) {
        buf.append(" handle (");
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
