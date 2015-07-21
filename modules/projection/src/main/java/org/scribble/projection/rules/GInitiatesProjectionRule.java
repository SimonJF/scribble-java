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
import org.scribble.model.Role;
import org.scribble.model.RoleDecl;
import org.scribble.model.global.GBlock;
import org.scribble.model.global.GChoice;
import org.scribble.model.global.GHandleBlock;
import org.scribble.model.global.GInitiates;
import org.scribble.model.local.LBlock;
import org.scribble.model.local.LChoice;
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
        Role initiatorRole = source.getRole();
		
		if (source.isRoleInvolved(role)) {
            if (role.isRole(initiatorRole)) {
                // When projecting for the initiator role, we should make a
                // local initiation projection.
                return projectAsInitiates(context, source, role, logger);
            } else {
                // Otherwise, the projection doesn't need to be aware of the
                // fact that a separate subsesison was spawned -- instead, we
                // just do an external choice projection.
                GChoice choiceBlock = transformToChoice(context, source,
                        role, logger);
                return projectAsChoice(context, choiceBlock, role, logger);
            }
        }
        
        return projected;
	}

    // Transforms an "initiates" block to a global choice block
    private GChoice transformToChoice(ModuleContext context, GInitiates source,
            RoleDecl role, IssueLogger logger) {
        GChoice ret = new GChoice();
        ret.setRole(source.getRole());

        // Add success block
        ret.getPaths().add(source.getBlock());

        // Add each handler block
        for (GHandleBlock hBlock : source.getHandleBlocks()) {
            ret.getPaths().add(hBlock.getBlock());
        }

        ret.setParent(source.getParent());

        return ret;
    }

    private LChoice projectAsChoice(ModuleContext context, GChoice source,
                                    RoleDecl role, IssueLogger logger) {

        ProjectionRule pr = ProjectionRuleFactory.getProjectionRule(source);
        return (LChoice) pr.project(context, source, role, logger);
    }

    private LInitiates projectAsInitiates(ModuleContext context, GInitiates source,
                                          RoleDecl role, IssueLogger logger) {
        LInitiates projected = new LInitiates();
			
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

        return projected;
    }

    private LBlock projectBlock(GBlock block, ModuleContext context, RoleDecl role,
                                IssueLogger logger) {
        ProjectionRule pr = ProjectionRuleFactory.getProjectionRule(block);
        return (LBlock) pr.project(context, block, role, logger);
        
    }
}
