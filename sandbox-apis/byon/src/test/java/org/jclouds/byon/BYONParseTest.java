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

package org.jclouds.byon;

import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.util.Map;
import java.util.Properties;

import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.ComputeServiceContextFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

//TODO: REMOVE
import com.google.common.base.Predicates;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.ssh.jsch.config.JschSshClientModule;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = "live")
public class BYONParseTest {
   private String provider = "byon";
   private String endpoint;
   private String identity;
   private String credential;

   @BeforeClass
   protected void setupCredentials() {
//      endpoint = System.getProperty("test." + provider + ".endpoint", "file://c:/test.txt");
      endpoint = System.getProperty("test." + provider + ".endpoint", "file:///Users/kelvin/pg/jclouds/sandbox-apis/byon/src/test/resources/config.yaml");
      // NOTE you may not care about identity/credential
//      identity = System.getProperty("test." + provider + ".identity", "FIXME_IDENTITY");
      identity = System.getProperty("test." + provider + ".identity", "kelvin");
//      credential = System.getProperty("test." + provider + ".credential", "FIXME_CREDENTIAL");
      credential = System.getProperty("test." + provider + ".credential", "~/.ssh/id_rsa");
   }

   @Test
   public void testNodesParse() throws Exception {
      ComputeServiceContext context = null;
      try {
         Properties contextProperties = new Properties();
         contextProperties.setProperty("byon.endpoint", endpoint);
         context = new ComputeServiceContextFactory().createContext("byon", identity, credential,
               ImmutableSet.<Module> of(new JschSshClientModule()), contextProperties);

         assertEquals(context.getProviderSpecificContext().getEndpoint(), URI.create(endpoint));

         @SuppressWarnings("unchecked")
         Supplier<Map<String, Node>> supplier = (Supplier<Map<String, Node>>) context.getProviderSpecificContext()
               .getApi();

         assertEquals(supplier.get().size(), context.getComputeService().listNodes().size());

         // TODO verify that the node list corresponds correctly to the content at endpoint
         context.getComputeService().listNodes();

//TODO: REMOVE
System.out.println(
context.getComputeService().runScriptOnNodesMatching(Predicates.<NodeMetadata>alwaysTrue(), "echo hello")
);

      } finally {
         if (context != null)
            context.close();
      }
   }

}
