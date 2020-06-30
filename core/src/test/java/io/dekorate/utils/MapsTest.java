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
package io.dekorate.utils;

import io.dekorate.project.BuildInfoBuilder;
import io.dekorate.project.Project;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.*;

class MapsTest {
  private static final String APPLICATION_PROPERTIES = "application.properties";
  private static final String APPLICATION_YAML = "application.yaml";
  private static final String NON_EXISTENT_PROPERTIES = "nonExistent.properties";

  @Test
  public void testMapFromProperties() throws Exception {
    try (InputStream is = MapsTest.class.getClassLoader().getResourceAsStream("simple.properties")) {
      Map<String, Object> map = Maps.fromProperties(is);
      checkFlattenMap(map);
    }
  }

  @Test
  public void testMapFromPropertiesWithArrays() throws Exception {
    try (InputStream is = MapsTest.class.getClassLoader().getResourceAsStream("kebab.properties")) {
      Map<String, Object> map = Maps.fromProperties(is);
      Map<String, Object> result = Maps.kebabToCamelCase(map);
      Map<String, Object> kubernetes = (Map<String, Object>) result.get("kubernetes");
      Map<String, Object>[] envVars = (Map<String,Object>[]) kubernetes.get("envVars");
      assertEquals("FOO", envVars[0].get("name"));
      assertEquals("BAR", envVars[0].get("value"));
    }
  }


  @Test
  public void testMapFromYAML() throws Exception {
    try (InputStream is = MapsTest.class.getClassLoader().getResourceAsStream("simple.yml")) {
      Map<String, Object> map = Maps.fromYaml(is);
      checkFlattenMap(map);
    }
  }

  @Test
  public void testKebabToCamelCase() throws Exception {
    try (InputStream is = MapsTest.class.getClassLoader().getResourceAsStream("kebab.yml")) {
      Map<String, Object> map = Maps.fromYaml(is);
      Map<String, Object> result = Maps.kebabToCamelCase(map);
      Map<String, Object> kubernetes = (Map<String, Object>) result.get("kubernetes");
      Map<String, Object> readinesProbe = (Map<String, Object>) kubernetes.get("readinesProbe");
      List<Map<String, Object>> envVars = (List<Map<String, Object>>) kubernetes.get("envVars");
      System.out.println(envVars);
      assertEquals("KEY1", envVars.get(0).get("name"));
      assertEquals("VALUE1", envVars.get(0).get("value"));
      assertEquals("KEY2", envVars.get(1).get("name"));
      assertEquals("VALUE2", envVars.get(1).get("value"));
      assertEquals(10, readinesProbe.get("periodSeconds"));
    }
  }


  private void checkFlattenMap(Map<String, Object> map) {
    assertNotNull(map);
    Map kubernetes = (Map) map.get("kubernetes");
    assertNotNull(kubernetes);
    assertTrue(kubernetes.containsKey("group"));
    assertTrue(kubernetes.containsKey("name"));
    assertTrue(kubernetes.containsKey("version"));
    assertTrue(kubernetes.containsKey("labels"));
    Object labels = kubernetes.get("labels");
    assertTrue(labels instanceof Map);
    assertEquals("bar", ((Map) labels).get("foo"));

    Map openshift = (Map) map.get("openshift");
    assertNotNull(openshift);
    assertEquals("boo", openshift.get("name"));
    labels = openshift.get("labels");
    assertTrue(labels instanceof Map);
    assertEquals("bar", ((Map) labels).get("foo"));
  }

  @Test
  public void onlyPrefixShouldFailMapFromProperties() throws Exception {
    try (InputStream is = MapsTest.class.getClassLoader().getResourceAsStream("wrong.properties")) {
      try {
        Maps.fromProperties(is);
        fail();
      } catch (IllegalArgumentException expected) {
      }
    }
  }

  @Test
  void missingFile() throws IOException {
    try (InputStream is = MapsTest.class.getClassLoader().getResourceAsStream(NON_EXISTENT_PROPERTIES)) {
      Map<String, Object> result = Maps.parseResourceFile(is, NON_EXISTENT_PROPERTIES);
      fail("should not be executed");
    }catch (IllegalArgumentException expected){

    }

  }

  @Test
  void shouldParsePropertiesFile() throws Exception {
    InputStream is = MapsTest.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES);
    Map<String, Object> result = Maps.parseResourceFile(is,APPLICATION_PROPERTIES);
    assertThat(result).containsOnlyKeys("key1", "key2", "k1")
      .contains(entry("key1", "value1"), entry("key2", "value2"));
    assertThat((Map)result.get("k1")).containsOnly(entry("k2", "v"));
  }

  @Test
  void shouldParseYamlFile() throws Exception {
    InputStream is = MapsTest.class.getClassLoader().getResourceAsStream(APPLICATION_YAML);

    Map<String, Object> result = Maps.parseResourceFile(is,APPLICATION_YAML);
    assertThat(result).containsOnlyKeys("key1", "key2", "k1")
      .contains(entry("key1", "value1"), entry("key2", "value2"));
    assertThat((Map)result.get("k1")).containsOnly(entry("k2", "v"));
  }
}

