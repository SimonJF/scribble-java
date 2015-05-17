/*
 * Copyright 2009-11 www.scribble.org
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
package org.scribble.validation.rules;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.scribble.context.ModuleContext;
import org.scribble.logging.IssueLogger;
import org.scribble.model.ModelObject;
import org.scribble.model.Role;
import org.scribble.model.global.DefaultGVisitor;
import org.scribble.model.global.GBlock;
import org.scribble.model.global.GCallBlock;
import org.scribble.model.global.GMessageTransfer;
import org.scribble.model.global.GParallel;

/**
 * This class implements the validation rule for the GParallel
 * component.
 *
 */
public class GParallelValidationRule implements ValidationRule {

	/**
	 * {@inheritDoc}
	 */
	public void validate(ModuleContext context, ModelObject mobj, IssueLogger logger) {
		GParallel elem=(GParallel)mobj;
		
		java.util.List<GMessageTransfer> mts=new ArrayList<GMessageTransfer>();
        Set<Role> callParticipants = new HashSet<Role>();
		
		for (GBlock subelem : elem.getPaths()) {
			ValidationRule rule=ValidationRuleFactory.getValidationRule(subelem);
			
			if (rule != null) {
				rule.validate(context, subelem, logger);
			}
			
			// Build up list of message transfers in this path
			// NOTE: This only checks for message transfers contained in this path,
			// and therefore would not deal with message transfers in an invoked protocol
			final List<GMessageTransfer> pathMTs=new ArrayList<GMessageTransfer>();
            final Set<Role> pathCallParticipants = new HashSet<Role>();
            final List<GCallBlock> pathCallBlocks = new ArrayList<GCallBlock>();
			
			subelem.visit(new DefaultGVisitor() {
				
			    /**
			     * {@inheritDoc}
			     */
			    public void accept(GMessageTransfer elem) {
			    	if (!pathMTs.contains(elem)) {
			    		pathMTs.add(elem);
			    	}
			    }

                @Override
                public boolean start(GCallBlock elem) {
                    Role caller = elem.getCaller();
                    Role callee = elem.getCaller();
                    if (!pathCallParticipants.contains(caller)) {
                        pathCallParticipants.add(caller);
                    }
                    if (!pathCallParticipants.contains(callee)) {
                        pathCallParticipants.add(callee);
                    }
                    pathCallBlocks.add(elem);

                    return true;
                }

                
			});

			// Check for overlap with previously discovered message transfers
			if (!Collections.disjoint(pathMTs, mts)) {
				
				for (GMessageTransfer mt : pathMTs) {
					if (mts.contains(mt)) {
						// Report message transfer in multiple concurrent paths
						logger.error(MessageFormat.format(ValidationMessages.getMessage("INTERACTION_IN_CONCURRENT_PATHS"),
								mt), elem);
					}
				}
			}

            // Check that the call participants in each block are disjoint
            if (!Collections.disjoint(pathCallParticipants, callParticipants)) {
                for (GCallBlock block : pathCallBlocks) {
                    if (callParticipants.contains(block.getCaller()) ||
                            callParticipants.contains(block.getCallee())) {
						logger.error(MessageFormat.format(ValidationMessages.getMessage("CALL_PARTICIPANTS_IN_CONCURRENT_PATHS"),
								block.getCaller()), elem);
                    }
                }
            }
			
			mts.addAll(pathMTs);
            callParticipants.addAll(pathCallParticipants);
            
		}
	}

}
