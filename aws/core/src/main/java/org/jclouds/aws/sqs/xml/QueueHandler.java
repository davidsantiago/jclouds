/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
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
package org.jclouds.aws.sqs.xml;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.jclouds.aws.domain.Region;
import org.jclouds.aws.sqs.domain.Queue;
import org.jclouds.http.functions.ParseSax;

import com.google.common.collect.ImmutableBiMap;

/**
 * 
 * @see <a href="http://docs.amazonwebservices.com/AWSSimpleQueueService/latest/APIReference/Query_QueryListQueues.html"
 *      />
 * @author Adrian Cole
 */
public class QueueHandler extends ParseSax.HandlerWithResult<Queue> {

   private StringBuilder currentText = new StringBuilder();
   Queue queue;

   private final ImmutableBiMap<Region, URI> regionBiMap;

   @Inject
   QueueHandler(Map<Region, URI> regionMap) {
      this.regionBiMap = ImmutableBiMap.copyOf(regionMap);

   }

   public Queue getResult() {
      return queue;
   }

   public void endElement(String uri, String name, String qName) {

      if (qName.equals("QueueUrl")) {
         String uriText = currentText.toString().trim();
         String queueName = uriText.substring(uriText.lastIndexOf('/') + 1);
         URI location = URI.create(uriText);
         URI regionURI = UriBuilder.fromUri(location).replacePath("").build();
         Region region = regionBiMap.inverse().get(regionURI);
         this.queue = new Queue(region, queueName, location);
      }
      currentText = new StringBuilder();
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }
}