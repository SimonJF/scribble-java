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

import org.scribble.context.ModuleContext;
import org.scribble.logging.IssueLogger;
import org.scribble.model.ModelObject;
import org.scribble.model.ProtocolDecl;
import org.scribble.model.Role;
import org.scribble.model.RoleInstantiation;
import org.scribble.model.global.GActivity;
import org.scribble.model.global.GBlock;
import org.scribble.model.global.GCallBlock;
import org.scribble.model.global.GChoice;
import org.scribble.model.global.GInitiates;
import org.scribble.model.global.GMessageTransfer;
import org.scribble.model.global.GMultiPathActivity;
import org.scribble.model.global.GProtocolDefinition;
import org.scribble.model.global.GSinglePathActivity;

/**
 * This class implements the validation rule for the GInitiates
 * component.
 * 
 * TODO FIXME etc: This is a clone of the GInitiates validation rule.
 * Realistically I should refactor this at some point.
 *
 */
public class GInitiatesValidationRule implements ValidationRule {

	/**
	 * {@inheritDoc}
	 */
	public void validate(ModuleContext context, ModelObject mobj, IssueLogger logger) {
		GInitiates elem=(GInitiates)mobj;
		
		// Check if initiator role has been declared
		if (elem.getRole() != null) {
			GProtocolDefinition gpd=elem.getParent(GProtocolDefinition.class);
			
			if (gpd.getRoleDeclaration(elem.getRole().getName()) == null) {
				logger.error(MessageFormat.format(ValidationMessages.getMessage("UNKNOWN_ROLE"),
						elem.getRole().getName()), elem.getRole());				
			}
		} else {
			logger.error(ValidationMessages.getMessage("UNDEFINED_ROLE"), elem);				
		}

        // Next check that all non-dynamically-introduced roles are declared
        GProtocolDefinition gpd = elem.getParent(GProtocolDefinition.class);
        for (RoleInstantiation ri : elem.getRoleInstantiationList()) {
            if (!ri.isNew()) {
                if (gpd.getRoleDeclaration(ri.getName()) == null) {
                    logger.error(MessageFormat.format(ValidationMessages.getMessage("UNKNOWN_ROLE"),
                            elem.getRole().getName()), elem.getRole());				
                }
            } 
        }

        // Check subprotocol def we're referencing is in the module, and that the kind
        // is correct.
        Object subprotocolDeclObj = context.getMember(elem.getSubsessionName());
        if (subprotocolDeclObj != null) {
            ProtocolDecl subprotocolDecl = (ProtocolDecl) subprotocolDeclObj;
            // Check kind
            if (subprotocolDecl.getRoleDeclarations().size() !=
                    elem.getRoleInstantiationList().size()) {
                logger.error(MessageFormat.format(ValidationMessages.getMessage("BAD_SUBPROTOCOL_KIND"),
                    elem.getSubsessionName()), elem.getRole());				

            }
        } else {
            // For now, we're insisting that the subprotocol is in the same module.
            // For the sake of this project, this will be fine, but in future this
            // should be done properly
            logger.error(MessageFormat.format(ValidationMessages.getMessage("UNKNOWN_PROTOCOL"),
                    elem.getSubsessionName()), elem.getRole());				

        }


		// TODO: Should the number of choice paths be validated?
		
		// TODO: Need to check each path to ensure that the 'decision is communicated
		// to each receiving role'
		
		java.util.Set<Role> roles=null;
		
		for (GBlock subelem : elem.getPaths()) {
			ValidationRule rule=ValidationRuleFactory.getValidationRule(subelem);
			
			if (rule != null) {
				rule.validate(context, subelem, logger);
			}
			
			// Check role set for block is same as others
			java.util.Set<Role> subroles=new java.util.HashSet<Role>();
			
			subelem.identifyInvolvedRoles(subroles);

			if (roles == null) {
				roles = subroles;
			} else {
				// Check list of roles is the same
				if (!roles.equals(subroles)) {
					logger.error(ValidationMessages.getMessage("ROLES_MISMATCH"), elem.getRole());				
				}
			}
		}
		
		if (roles != null) {
			// Wellformedness - Check each path has an initial message receiver for each non-choice role,
			// before the roles are used in any other activity
			java.util.Set<Role> otherRoles=new java.util.HashSet<Role>(roles);
			
			// Remove choice role
			otherRoles.remove(elem.getRole());
			
			for (GBlock block : elem.getPaths()) {
				checkReceiverBeforeOtherActivity(context, block,
						new java.util.HashSet<Role>(otherRoles), logger);
			}
			
			// Check that initial message receivers for each 'other role' are distinct
			// in each path - check both operators and signatures
			java.util.Map<Role, java.util.Set<String>> ops=new java.util.HashMap<Role, java.util.Set<String>>();
			java.util.Map<Role, java.util.Set<String>> sigs=new java.util.HashMap<Role, java.util.Set<String>>();
			
			for (GBlock block : elem.getPaths()) {
				checkReceiverOpSigDistinct(context, block,
						new java.util.HashSet<Role>(otherRoles), ops, sigs, logger);
			}
		}
	}

	/**
	 * This method checks that the first activity for a role is a message transfer where the
	 * role is the receiver.
	 * 
	 * @param context The context
	 * @param block The block
	 * @param roles The roles to check
	 * @param logger The logger
	 */
	protected void checkReceiverBeforeOtherActivity(ModuleContext context, GBlock block,
								java.util.Set<Role> roles, IssueLogger logger) {
		for (GActivity act : block.getContents()) {
			
			if (act instanceof GMessageTransfer) {
				roles.removeAll(((GMessageTransfer)act).getToRoles());
				
				if (roles.contains(((GMessageTransfer)act).getFromRole())) {
					logger.error(MessageFormat.format(ValidationMessages.getMessage("ROLE_NOT_RECEIVER"),
							((GMessageTransfer)act).getFromRole().getName()), act);				

					roles.remove(((GMessageTransfer)act).getFromRole());
				}
				
			} else if (act instanceof GCallBlock) {
                GCallBlock callBlock = (GCallBlock) act;
                Role caller = callBlock.getCaller();
                Role callee = callBlock.getCaller();
                roles.remove(callee);

                if (roles.contains(caller)) {
                   logger.error(MessageFormat.format(ValidationMessages.getMessage("ROLE_NOT_RECEIVER"),
							caller.getName()), act);				

					roles.remove(caller);
                }
            } else if (act instanceof GChoice) {
				// Check if target role is in the list
				if (roles.contains(((GChoice)act).getRole())) {					
					logger.error(MessageFormat.format(ValidationMessages.getMessage("ROLE_NOT_RECEIVER"),
							((GChoice)act).getRole().getName()), act);				

					roles.remove(((GChoice)act).getRole());
				}
				
				for (GBlock b : ((GChoice)act).getPaths()) {
					checkReceiverBeforeOtherActivity(context, b,
							new java.util.HashSet<Role>(roles), logger);
				}
			} else if (act instanceof GInitiates) {
				// Check if target role is in the list
				if (roles.contains(((GInitiates)act).getRole())) {					
					logger.error(MessageFormat.format(ValidationMessages.getMessage("ROLE_NOT_RECEIVER"),
							((GInitiates)act).getRole().getName()), act);				

					roles.remove(((GInitiates)act).getRole());
				}
				
				for (GBlock b : ((GInitiates)act).getPaths()) {
					checkReceiverBeforeOtherActivity(context, b,
							new java.util.HashSet<Role>(roles), logger);
				}
			} else if (act instanceof GMultiPathActivity) {
				for (GBlock b : ((GMultiPathActivity)act).getPaths()) {
					checkReceiverBeforeOtherActivity(context, b,
							new java.util.HashSet<Role>(roles), logger);
				}

			} else if (act instanceof GSinglePathActivity) {
				checkReceiverBeforeOtherActivity(context, ((GSinglePathActivity)act).getBlock(),
						roles, logger);

			} else {
				java.util.Set<Role> involved=new java.util.HashSet<Role>();
				
				act.identifyInvolvedRoles(involved);
				
				for (Role r : involved) {
					if (roles.contains(r)) {
						logger.error(MessageFormat.format(ValidationMessages.getMessage("ROLE_NOT_RECEIVER"),
								r.getName()), act);				
						
						roles.remove(r);
					}
				}
			}
		}
	}
	/**
	 * This method checks that the initial receiver for each non-choice role has a distinct operator
	 * or signature.
	 * 
	 * @param context The context
	 * @param block The block
	 * @param roles The roles to check
	 * @param logger The logger
	 */
	protected void checkReceiverOpSigDistinct(ModuleContext context, GBlock block,
							java.util.Set<Role> roles, java.util.Map<Role, java.util.Set<String>> operators,
							java.util.Map<Role, java.util.Set<String>> signatures, IssueLogger logger) {
		for (int i=0; roles.size() > 0 && i < block.getContents().size(); i++) {
			GActivity act=block.getContents().get(i);
		
			if (act instanceof GMessageTransfer) {
				
				for (Role r : ((GMessageTransfer)act).getToRoles()) {
					if (roles.contains(r)) {
						GMessageTransfer mt=(GMessageTransfer)act;
						
						if (mt.getMessage() == null) {
							continue;
						}
						
						if (mt.getMessage().getMessageSignature() != null &&
								mt.getMessage().getMessageSignature().getOperator() != null) {
							java.util.Set<String> ops=operators.get(r);
							
							if (ops == null) {
								ops = new java.util.HashSet<String>();
								operators.put(r,  ops);
							}
							
							if (ops.contains(mt.getMessage().getMessageSignature().getOperator())) {
								logger.error(MessageFormat.format(ValidationMessages.getMessage("ROLE_OPERATOR_NOT_DISTINCT"),
										mt.getMessage().getMessageSignature().getOperator(), r.getName()), act);											
							}
							
							ops.add(mt.getMessage().getMessageSignature().getOperator());
						} else if (mt.getMessage().getParameter() != null) {
							java.util.Set<String> sigs=signatures.get(r);
							
							if (sigs == null) {
								sigs = new java.util.HashSet<String>();
								signatures.put(r,  sigs);
							}
							
							if (sigs.contains(mt.getMessage().getParameter())) {
								logger.error(MessageFormat.format(ValidationMessages.getMessage("ROLE_SIGNATURE_NOT_DISTINCT"),
										mt.getMessage().getParameter(), r.getName()), act);											
							}
							
							sigs.add(mt.getMessage().getParameter());
						}
						
						roles.remove(r);
					}
				}
            } else if (act instanceof GCallBlock) {
                GCallBlock callBlock = (GCallBlock) act;
                Role calleeRole = callBlock.getCallee();
                if (roles.contains(calleeRole)) {
                    java.util.Set<String> ops=operators.get(calleeRole);
                    
                    if (ops == null) {
                        ops = new java.util.HashSet<String>();
                        operators.put(calleeRole, ops);
                    }
                    
                    if (ops.contains(callBlock.getRequestName())) {
                        logger.error(MessageFormat.format(ValidationMessages.getMessage("ROLE_OPERATOR_NOT_DISTINCT"),
                                callBlock.getRequestName(), calleeRole.getName()), act);
                    }
                    ops.add(callBlock.getRequestName());
                } 
			} else if (act instanceof GMultiPathActivity) {
				for (GBlock b : ((GMultiPathActivity)act).getPaths()) {
					checkReceiverOpSigDistinct(context, b,
							new java.util.HashSet<Role>(roles), operators, signatures, logger);
				}

			} else if (act instanceof GSinglePathActivity) {
				checkReceiverOpSigDistinct(context, ((GSinglePathActivity)act).getBlock(),
						roles, operators, signatures, logger);
			}
		}
	}
}
