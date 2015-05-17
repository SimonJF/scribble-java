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

import java.util.ArrayList;
import java.util.List;
import org.scribble.context.ModuleContext;
import org.scribble.logging.IssueLogger;
import org.scribble.model.Message;
import org.scribble.model.MessageSignature;
import org.scribble.model.ModelObject;
import org.scribble.model.PayloadElement;
import org.scribble.model.RoleDecl;
import org.scribble.model.global.GCallBlock;
import org.scribble.model.local.LActivity;
import org.scribble.model.local.LBlock;
import org.scribble.model.local.LCallReqReceive;
import org.scribble.model.local.LCallReqSend;
import org.scribble.model.local.LCallRespReceive;
import org.scribble.model.local.LCallRespSend;

/**
 * This class implements the GRecursion projection rule.
 *
 */
public class GCallBlockProjectionRule implements ProjectionRule {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object project(ModuleContext context, ModelObject mobj,
								RoleDecl role, IssueLogger logger) {

        GCallBlock callBlock = (GCallBlock) mobj;
        List<ModelObject> ret = new ArrayList<ModelObject>();
        MessageSignature sig = 
            new MessageSignature(callBlock.getRequestName(), 
                                 callBlock.getRequestTypes());
            Message msg = new Message();
            msg.setMessageSignature(sig);
 
        if (role.isRole(callBlock.getCaller())) {
            // Project a send call request
            LCallReqSend reqSendProj = new LCallReqSend();
            reqSendProj.setToRole(callBlock.getCallee());
            reqSendProj.setMessage(msg);
            ret.add(reqSendProj);
        } else if (role.isRole(callBlock.getCallee())) {
            // Project a receive call request
            LCallReqReceive reqReceiveProj = new LCallReqReceive();
            msg.setMessageSignature(sig);
            reqReceiveProj.setFromRole(callBlock.getCaller());
            reqReceiveProj.setMessage(msg);
            ret.add(reqReceiveProj);
        }


        if (callBlock.isRoleInvolved(role)) {
            ProjectionRule rule =
                    ProjectionRuleFactory.getProjectionRule(callBlock.getBlock());
            LBlock lb = (LBlock) rule.project(context, callBlock.getBlock(), role, logger);
            for (LActivity act : lb.getContents()) {
                ret.add(act);
            }
        }

        List<PayloadElement> replyTypes = new ArrayList<PayloadElement>();
        replyTypes.add(callBlock.getResponseType());
        MessageSignature respSig = 
            new MessageSignature(callBlock.getRequestName(), 
                                 replyTypes);
        Message respMsg = new Message();
        respMsg.setMessageSignature(respSig);
 

        if (role.isRole(callBlock.getCallee())) {
            // Project a send call response 
            LCallRespSend respSendProj = new LCallRespSend();
            respSendProj.setToRole(callBlock.getCaller());
            respSendProj.setMessage(respMsg);
            ret.add(respSendProj);
        } else if (role.isRole(callBlock.getCaller())) {
            // Project a receive call response
            LCallRespReceive respReceiveProj = new LCallRespReceive();
            msg.setMessageSignature(sig);
            respReceiveProj.setFromRole(callBlock.getCallee());
            respReceiveProj.setMessage(respMsg);
            ret.add(respReceiveProj);
        }
        return ret;
    }

}
