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

/**
 * This class represents the base class for all multi-path activities.
 */
public abstract class GMultiPathActivity extends GActivity {
    
    /**
     * The default constructor.
     */
    public GMultiPathActivity() {
    }
    
    /**
     * The copy constructor.
     * 
     * @param act The activity
     */
    public GMultiPathActivity(GMultiPathActivity act) {
        super(act);
    }
    
    /**
     * This method returns the list of paths.
     * 
     * @return The paths
     */
    public abstract java.util.List<GBlock> getPaths();
    
}
