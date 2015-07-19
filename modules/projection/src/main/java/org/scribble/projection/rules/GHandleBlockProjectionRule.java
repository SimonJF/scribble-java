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
package org.scribble.projection.rules;

import org.scribble.context.ModuleContext;
import org.scribble.logging.IssueLogger;
import org.scribble.model.ModelObject;
import org.scribble.model.RoleDecl;
import org.scribble.model.global.GHandleBlock;
import org.scribble.model.local.LBlock;
import org.scribble.model.local.LHandleBlock;

/**
 *
 * @author simon
 */
public class GHandleBlockProjectionRule implements ProjectionRule {

    @Override
    public Object project(ModuleContext context, ModelObject mobj, RoleDecl role, IssueLogger logger) {
        GHandleBlock ghb = (GHandleBlock) mobj;
        LHandleBlock lhb = new LHandleBlock();
        lhb.setFailureName(ghb.getFailureName());


        ProjectionRule blockProjectionRule =
                ProjectionRuleFactory.getProjectionRule(ghb.getBlock());

        LBlock localBlock =
                (LBlock) blockProjectionRule.project(context, ghb.getBlock(), role, logger);

        lhb.setBlock(localBlock);
        
        return lhb;
    }
    
}
