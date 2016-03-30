/*
 * SonarLint Core - Client API
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarsource.sonarlint.core.client.api.common;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.concurrent.Immutable;

/**
 * To use SonarLint in connected mode please provide a server id that will identify the storage.
 * To use in standalone mode please provide list of plugin URLs.  
 *
 */
@Immutable
public abstract class AbstractGlobalConfiguration {

  public static final String DEFAULT_WORK_DIR = "work";

  private final Path sonarLintUserHome;
  private final Path workDir;

  public AbstractGlobalConfiguration(AbstractBuilder<?> builder) {
    this.sonarLintUserHome = builder.sonarlintUserHome != null ? builder.sonarlintUserHome : findHome();
    this.workDir = builder.workDir != null ? builder.workDir : this.sonarLintUserHome.resolve(DEFAULT_WORK_DIR);
  }

  public Path getSonarLintUserHome() {
    return sonarLintUserHome;
  }

  public Path getWorkDir() {
    return workDir;
  }

  private static Path findHome() {
    String path = System.getenv("SONARLINT_USER_HOME");
    if (path == null) {
      // Default
      path = System.getProperty("user.home") + File.separator + ".sonarlint";
    }
    return Paths.get(path);
  }

  public static class AbstractBuilder<G extends AbstractBuilder> {
    private Path sonarlintUserHome;
    private Path workDir;

    /**
     * Override default user home (~/.sonarlint)
     */
    public G setSonarLintUserHome(Path sonarlintUserHome) {
      this.sonarlintUserHome = sonarlintUserHome;
      return (G) this;
    }

    /**
     * Override default work dir (~/.sonarlint/work)
     */
    public G setWorkDir(Path workDir) {
      this.workDir = workDir;
      return (G) this;
    }

  }

}