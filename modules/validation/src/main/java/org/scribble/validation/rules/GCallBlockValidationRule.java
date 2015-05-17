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
package org.scribble.validation.rules;

import java.text.MessageFormat;
import org.scribble.context.ModuleContext;
import org.scribble.logging.IssueLogger;
import org.scribble.model.ModelObject;
import org.scribble.model.Role;
import org.scribble.model.global.DefaultGVisitor;
import org.scribble.model.global.GBlock;
import org.scribble.model.global.GCallBlock;
import org.scribble.model.global.GInterruptible;
import org.scribble.model.global.GMessageTransfer;
import org.scribble.model.global.GProtocolDefinition;

/**
 *
 * @author simon
 */
public class GCallBlockValidationRule implements ValidationRule {

    @Override
    public void validate(ModuleContext context, ModelObject mobj, IssueLogger logger) {

        final GCallBlock callBlock = (GCallBlock) mobj;

        // Ensure the call block isn't contained in an interruptible block
        validateNotInInterruptible(callBlock, logger);
        if (callBlock.getBlock() != null) {
            // Ensure the instructions in the block conform to standard Scribble
            // rules
            validateBlock(context, callBlock, logger);
            // Ensure that the caller is not referenced within the interactions in
            // the block
            validateNoCallerInteractions(callBlock, logger);
        }
    }

    private void validateNotInInterruptible(final GCallBlock callBlock,
            IssueLogger logger) {

        // Call blocks can't be contained within interruptible blocks.
        // If a call block is in a parallel block, it should only be in the one
        // branch.

        ModelObject ancestor = callBlock;
        while (!(ancestor instanceof GProtocolDefinition || ancestor == null)) {
            if (ancestor instanceof GInterruptible) {
                // Error: call block in interruptible
				logger.error(ValidationMessages.getMessage("CALL_CONTAINED_IN_INTERRUPTIBLE"),
                        callBlock);
            }
            // Handle parallel case inside of parallel validation rule, methinks
            ancestor = ancestor.getParent();
        }
    }

    private void validateBlock(ModuleContext context,
            final GCallBlock callBlock, IssueLogger logger) {

        // Next, do a standard validation pass, checking that everything is
        // above board according to standard Scribble rules
        GBlock block = callBlock.getBlock();
        ValidationRule rule = ValidationRuleFactory.getValidationRule(block);
        if (rule != null) {
            rule.validate(context, block, logger);
        }
    }

    private void validateNoCallerInteractions(final GCallBlock callBlock,
            final IssueLogger logger) {
            
        // Next up, we need to make sure that there are no messages to the caller
        // in the block.
        GBlock block = callBlock.getBlock();
        block.visit(new DefaultGVisitor() {
            final Role caller = callBlock.getCaller();
           
            /**
             * {@inheritDoc}
             */
            @Override
            public void accept(GMessageTransfer elem) {
                // Caller can't participate within the block: async
                if (elem.getFromRole().equals(caller) || 
                        elem.getToRoles().contains(caller)) {
                    logger.error(MessageFormat.format(
                            ValidationMessages.getMessage("INTERACTION_WITH_CALLER"),
                            caller), elem);
                }
            }

            @Override
            public boolean start(GCallBlock elem) {
                // Caller can't participate within the block: sync
                if (elem.getCaller().equals(caller) ||
                        elem.getCallee().equals(caller)) {
                    logger.error(MessageFormat.format(
                            ValidationMessages.getMessage("INTERACTION_WITH_CALLER"),
                            caller), elem);
                }

                return true;
            }
        });
    }
    
}
