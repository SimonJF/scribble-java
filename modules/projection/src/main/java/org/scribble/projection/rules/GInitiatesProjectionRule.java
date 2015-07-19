/*
 * Copyright 2009-14 www.scribble.org
 *
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
package org.scribble.projection.rules;

import org.scribble.context.ModuleContext;
import org.scribble.logging.IssueLogger;
import org.scribble.model.ModelObject;
import org.scribble.model.RoleDecl;
import org.scribble.model.global.GBlock;
import org.scribble.model.global.GHandleBlock;
import org.scribble.model.global.GInitiates;
import org.scribble.model.local.LBlock;
import org.scribble.model.local.LHandleBlock;
import org.scribble.model.local.LInitiates;

/**
 * This class implements the GParallel projection rule.
 *
 */
public class GInitiatesProjectionRule implements ProjectionRule {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object project(ModuleContext context, ModelObject mobj,
									RoleDecl role, IssueLogger logger) {
		LInitiates projected=null;
		GInitiates source=(GInitiates) mobj;
		
		if (source.isRoleInvolved(role)) {
			projected = new LInitiates();
			
			projected.derivedFrom(source);
	        
            projected.setRole(source.getRole());
            projected.setSubsessionName(source.getSubsessionName());
            projected.setRoleInstantiationList(source.getRoleInstantiationList());

            LBlock localMainBlock = projectBlock(source.getBlock(), context, role,
                    logger);

            projected.setBlock(localMainBlock);
            for (GHandleBlock handleBlock : source.getHandleBlocks()) {
                ProjectionRule pr = ProjectionRuleFactory.getProjectionRule(handleBlock);
                LHandleBlock lhb =
                        (LHandleBlock) pr.project(context, handleBlock, role, logger);
                projected.addHandleBlock(lhb);
            }
        }
        
        return projected;
	}

    private LBlock projectBlock(GBlock block, ModuleContext context, RoleDecl role,
                                IssueLogger logger) {
        ProjectionRule pr = ProjectionRuleFactory.getProjectionRule(block);
        return (LBlock) pr.project(context, block, role, logger);
        
    }
}
