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
import org.scribble.model.global.GInitiates1;
import org.scribble.model.local.LInitiates1;

/**
 * This class implements the GParallel projection rule.
 *
 */
public class GInitiates1ProjectionRule implements ProjectionRule {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object project(ModuleContext context, ModelObject mobj,
									RoleDecl role, IssueLogger logger) {
		LInitiates1 projected=null;
		GInitiates1 source=(GInitiates1) mobj;
        Role initiatorRole = source.getRole();
		
		if (source.isRoleInvolved(role)) {
            if (role.isRole(initiatorRole)) {
                // When projecting for the initiator role, we should make a
                // local initiation projection.
                return projectAsInitiates(context, source, role, logger);
            }
        }
        
        return projected;
	}

    private LInitiates1 projectAsInitiates(ModuleContext context, GInitiates1 source,
                                          RoleDecl role, IssueLogger logger) {
        LInitiates1 projected = new LInitiates1();
			
        projected.derivedFrom(source);
        
        projected.setRole(source.getRole());
        projected.setSubsessionName(source.getSubsessionName());
        projected.setRoleInstantiationList(source.getRoleInstantiationList());

        return projected;
    }

}
