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
 */
package io.dekorate.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import io.dekorate.WithProject;
import io.dekorate.utils.Maps;

public interface SpringPropertiesHolder extends WithProject {

  AtomicReference<Map<String, Object>> springProperties = new AtomicReference<>(null);

  default Map<String, Object> getSpringProperties() {
    if (springProperties.get() == null) {
      final Map<String, Object> properties = new HashMap<>();
      FileInputStream fileInputStream = null;
      try {
        fileInputStream = new FileInputStream(getProject().getBuildInfo().getResourceDir().resolve("application.properties").toFile());

      properties.putAll(Maps.parseResourceFile(fileInputStream,"application.properties"));
      FileInputStream fileInputStreamYaml = new FileInputStream(getProject().getBuildInfo().getResourceDir().resolve("application.yaml").toFile());
      properties.putAll(Maps.parseResourceFile(fileInputStreamYaml,"application.yaml"));
      FileInputStream fileInputStreamYml = new FileInputStream(getProject().getBuildInfo().getResourceDir().resolve("application.yml").toFile());
      properties.putAll(Maps.parseResourceFile(fileInputStreamYaml,"application.yml"));
      springProperties.set(properties);
      } catch (FileNotFoundException e) {
        return properties;
      }
    }
    return springProperties.get();
  }
}
