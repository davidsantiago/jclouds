/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
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
package org.jclouds.cloudstack;

import java.util.concurrent.TimeUnit;

import org.jclouds.cloudstack.features.AddressClient;
import org.jclouds.cloudstack.features.AsyncJobClient;
import org.jclouds.cloudstack.features.ConfigurationClient;
import org.jclouds.cloudstack.features.FirewallClient;
import org.jclouds.cloudstack.features.GuestOSClient;
import org.jclouds.cloudstack.features.HypervisorClient;
import org.jclouds.cloudstack.features.LoadBalancerClient;
import org.jclouds.cloudstack.features.NATClient;
import org.jclouds.cloudstack.features.NetworkClient;
import org.jclouds.cloudstack.features.OfferingClient;
import org.jclouds.cloudstack.features.SecurityGroupClient;
import org.jclouds.cloudstack.features.TemplateClient;
import org.jclouds.cloudstack.features.VirtualMachineClient;
import org.jclouds.cloudstack.features.ZoneClient;
import org.jclouds.concurrent.Timeout;
import org.jclouds.rest.annotations.Delegate;

/**
 * Provides synchronous access to CloudStack.
 * <p/>
 * 
 * @see CloudStackAsyncClient
 * @see <a href="http://download.cloud.com/releases/2.2.0/api/TOC_User.html" />
 * @author Adrian Cole
 */
@Timeout(duration = 60, timeUnit = TimeUnit.SECONDS)
public interface CloudStackClient {
   /**
    * Provides synchronous access to Zone features.
    */
   @Delegate
   ZoneClient getZoneClient();

   /**
    * Provides synchronous access to Template features.
    */
   @Delegate
   TemplateClient getTemplateClient();

   /**
    * Provides synchronous access to Service, Disk, and Network Offering features.
    */
   @Delegate
   OfferingClient getOfferingClient();

   /**
    * Provides synchronous access to Network features.
    */
   @Delegate
   NetworkClient getNetworkClient();

   /**
    * Provides synchronous access to VirtualMachine features.
    */
   @Delegate
   VirtualMachineClient getVirtualMachineClient();

   /**
    * Provides synchronous access to SecurityGroup features.
    */
   @Delegate
   SecurityGroupClient getSecurityGroupClient();

   /**
    * Provides synchronous access to AsyncJob features.
    */
   @Delegate
   AsyncJobClient getAsyncJobClient();

   /**
    * Provides synchronous access to Address features.
    */
   @Delegate
   AddressClient getAddressClient();

   /**
    * Provides synchronous access to NAT features.
    */
   @Delegate
   NATClient getNATClient();

   /**
    * Provides synchronous access to Firewall features.
    */
   @Delegate
   FirewallClient getFirewallClient();

   /**
    * Provides synchronous access to LoadBalancer features.
    */
   @Delegate
   LoadBalancerClient getLoadBalancerClient();

   /**
    * Provides synchronous access to GuestOS features.
    */
   @Delegate
   GuestOSClient getGuestOSClient();

   /**
    * Provides synchronous access to Hypervisor features.
    */
   @Delegate
   HypervisorClient getHypervisorClient();

   /**
    * Provides synchronous access to Configuration features.
    */
   @Delegate
   ConfigurationClient getConfigurationClient();
}
