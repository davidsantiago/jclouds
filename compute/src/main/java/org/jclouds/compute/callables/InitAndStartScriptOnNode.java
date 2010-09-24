/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.compute.callables;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.util.ComputeServiceUtils;
import org.jclouds.compute.util.ComputeServiceUtils.SshCallable;
import org.jclouds.io.Payloads;
import org.jclouds.logging.Logger;
import org.jclouds.scriptbuilder.domain.OsFamily;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.ssh.ExecResponse;
import org.jclouds.ssh.SshClient;

import com.google.common.collect.Iterables;

/**
 * 
 * @author Adrian Cole
 */
public class InitAndStartScriptOnNode implements SshCallable<ExecResponse> {
   protected SshClient ssh;
   protected final NodeMetadata node;
   protected final String scriptName;
   protected final Statement script;
   protected final boolean runAsRoot;
   protected Logger logger = Logger.NULL;

   public InitAndStartScriptOnNode(NodeMetadata node, String scriptName, Statement script, boolean runAsRoot) {
      this.node = checkNotNull(node, "node");
      this.scriptName = checkNotNull(scriptName, "scriptName");
      this.script = checkNotNull(script, "script");
      this.runAsRoot = runAsRoot;
   }

   @Override
   public ExecResponse call() {
      ssh.put(scriptName, Payloads.newPayload(script.render(OsFamily.UNIX)));
      ExecResponse returnVal = ssh.exec("chmod 755 " + scriptName);
      returnVal = ssh.exec("./" + scriptName + " init");
      logger.debug("<< initialized(%d)", returnVal.getExitCode());

      String command = (runAsRoot) ? startScriptAsRoot() : startScriptAsDefaultUser();
      returnVal = runCommand(command);
      logger.debug("<< start(%d)", returnVal.getExitCode());
      return returnVal;
   }

   protected ExecResponse runCommand(String command) {
      ExecResponse returnVal;
      logger.debug(">> running [%s] as %s@%s", command.replace(node.getCredentials().credential, "XXXXX"), node
               .getCredentials().identity, Iterables.get(node.getPublicAddresses(), 0));
      returnVal = ssh.exec(command);
      return returnVal;
   }

   @Override
   public void setConnection(SshClient ssh, Logger logger) {
      this.logger = checkNotNull(logger, "logger");
      this.ssh = checkNotNull(ssh, "ssh");
   }

   protected String startScriptAsRoot() {
      String command;
      if (node.getCredentials().identity.equals("root")) {
         command = "./" + scriptName + " start";
      } else if (ComputeServiceUtils.isKeyAuth(node)) {
         command = "sudo ./" + scriptName + " start";
      } else {
         command = String.format("echo '%s'|sudo -S ./%s", node.getCredentials().credential, scriptName + " start");
      }
      return command;
   }

   protected String startScriptAsDefaultUser() {
      return "./" + scriptName + " start";
   }

   @Override
   public NodeMetadata getNode() {
      return node;
   }
}