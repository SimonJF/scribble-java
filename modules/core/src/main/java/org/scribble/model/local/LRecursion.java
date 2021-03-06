/*
 * Copyright 2009-10 www.scribble.org
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

/**
 * This class represents the Recur construct.
 * 
 */
public class LRecursion extends LActivity {

    private String _label=null;
    private LBlock _block=new LBlock();

    /**
     * This is the default constructor.
     * 
     */
    public LRecursion() {
        _block.setParent(this);
    }
    
    /**
     * This method returns the label associated with the labelled block construct.
     * 
     * @return The label
     */
    public String getLabel() {
        return (_label);
    }
    
    /**
     * This method sets the label associated with the labelled block construct.
     * 
     * @param label The label
     */
    public void setLabel(String label) {
        _label = label;
    }
        
    /**
     * This method returns the activities.
     * 
     * @return The block of activities
     */
    public LBlock getBlock() {
        return (_block);
    }
    
    /**
     * This method sets the block.
     * 
     * @param block The block
     */
    public void setBlock(LBlock block) {
        if (_block != null) {
            _block.setParent(null);
        }
        
        _block = block;
        
        if (_block != null) {
            _block.setParent(this);
        }
    }

    /**
     * This method visits the model object using the supplied
     * visitor.
     * 
     * @param visitor The visitor
     */
    public void visit(LVisitor visitor) {
        if (visitor.start(this)) {
        
            if (getBlock() != null) {
                getBlock().visit(visitor);
            }
        }
        
        visitor.end(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LRecursion that = (LRecursion) o;

        return !(_label != null
                ? !_label.equals(that._label)
                : that._label != null)
            && !(_block != null
                ? !_block.equals(that._block)
                : that._block != null);
    }

    @Override
    public int hashCode() {
        int result = _label != null ? _label.hashCode() : 0;
        return 31 * result + (_block != null ? _block.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "rec "+_label+" "+_block;
    }

	/**
	 * {@inheritDoc}
	 */
    public void toText(StringBuffer buf, int level) {
		
    	indent(buf, level);
    	
    	buf.append("rec ");
    	
    	buf.append(_label);
    	
    	buf.append(" ");
    	
    	if (_block != null) {
    		_block.toText(buf, level);
    	}
    	
		buf.append("\n");
	}
}
