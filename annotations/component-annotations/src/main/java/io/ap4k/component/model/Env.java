/**
 * Copyright 2018 The original authors.
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
**/
package io.ap4k.component.model;
import io.ap4k.deps.jackson.annotation.JsonInclude;
import io.ap4k.deps.jackson.annotation.JsonPropertyOrder;
import io.ap4k.deps.jackson.databind.annotation.JsonDeserialize;
import io.ap4k.deps.kubernetes.api.model.Doneable;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.Inline;
/**
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
      "value"
      })
      @JsonDeserialize(using = io.ap4k.deps.jackson.databind.JsonDeserializer.None.class)
      @Buildable(editableEnabled = false, validationEnabled = false, generateBuilderPackage = false, builderPackage = "io.ap4k.deps.kubernetes.api.builder", inline = @Inline(type = Doneable.class, prefix = "Doneable", value = "done"))
      public class Env {
        private String name;
        private String value;

        public Env() {
        }

        public Env(String name, String value) {
          this.name = name;
          this.value = value;
        }

        public String getName() {
          return name;
        }

        public void setName(String name) {
          this.name = name;
        }

        public String getValue() {
          return value;
        }

        public void setValue(String value) {
          this.value = value;
        }
      }
