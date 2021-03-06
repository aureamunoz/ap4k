/**
 * Copyright (C) 2019  The original authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package io.ap4k.component.configurator;

import io.ap4k.project.Project;
import io.ap4k.utils.Strings;
import io.ap4k.component.config.CompositeConfigFluent;
import io.ap4k.kubernetes.config.Configurator;

public class ApplyProject extends Configurator<CompositeConfigFluent<?>> {

  private static final String APP_NAME = "app.name";
  private final Project project;

  public ApplyProject(Project project) {
    this.project = project;
  }

  @Override
  public void visit(CompositeConfigFluent<?> fluent) {
    fluent.withProject(project)
      .withName(System.getProperty(APP_NAME, Strings.isNotNullOrEmpty(fluent.getName()) ? fluent.getName() : project.getBuildInfo().getName()));
  }
}
