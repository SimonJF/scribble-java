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
import org.scribble.model.ImportDecl;
import org.scribble.model.ModelObject;
import org.scribble.model.Module;

/**
 * This class implements the validation rule for the ImportDecl
 * component.
 *
 */
public class ImportDeclValidationRule implements ValidationRule {

	/**
	 * {@inheritDoc}
	 */
	public void validate(ModuleContext context, ModelObject mobj, IssueLogger logger) {
		ImportDecl elem=(ImportDecl)mobj;
		
		if (elem.getModuleName() != null) {
			
			// Load imported module
			Module importedModule=context.importModule(elem.getModuleName());
			
			// Wellformedness - check module can be found
			if (importedModule == null) {
				logger.error(MessageFormat.format(ValidationMessages.getMessage("NOT_FOUND_MODULE"),
						elem.getModuleName()), elem);
			} else if (elem.getMemberName() != null) {
				
				// Check if member within module has been specified
				// Wellformedness - check member is found in module
				ModelObject imported=context.getMember(elem.getModuleName(),
						elem.getMemberName());
				
				if (imported == null) {					
					logger.error(MessageFormat.format(ValidationMessages.getMessage("NOT_FOUND_MEMBER"),
							elem.getMemberName(), elem.getModuleName()), elem);
					
				} else if (elem.getAlias() != null &&
						context.getMember(elem.getAlias()) != imported) {
					logger.error(MessageFormat.format(ValidationMessages.getMessage("EXISTS_ALIAS"),
							elem.getAlias()), elem);
				}				
			}
		} else {
			logger.error(ValidationMessages.getMessage("NO_MODULE"), elem);
		}
	}

}
