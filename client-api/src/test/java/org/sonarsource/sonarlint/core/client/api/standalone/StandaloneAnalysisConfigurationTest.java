/*
 * SonarLint Core - Client API
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
package org.sonarsource.sonarlint.core.client.api.standalone;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonarsource.sonarlint.core.client.api.TestClientInputFile;
import org.sonarsource.sonarlint.core.client.api.common.analysis.ClientInputFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class StandaloneAnalysisConfigurationTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void testToString() throws Exception {
    Map<String, String> props = new HashMap<>();
    props.put("sonar.java.libraries", "foo bar");

    final Path srcFile1 = temp.newFile().toPath();
    final Path srcFile2 = temp.newFile().toPath();
    final Path srcFile3 = temp.newFile().toPath();
    ClientInputFile inputFile = new TestClientInputFile(srcFile1, false, StandardCharsets.UTF_8, null);
    ClientInputFile inputFileWithLanguage = new TestClientInputFile(srcFile2, false, StandardCharsets.UTF_8, "java");
    ClientInputFile testInputFile = new TestClientInputFile(srcFile3, true, StandardCharsets.UTF_8, "php");
    Path baseDir = temp.newFolder().toPath();
    Path workDir = temp.newFolder().toPath();
    StandaloneAnalysisConfiguration config = new StandaloneAnalysisConfiguration(baseDir, workDir, Arrays.asList(inputFile, inputFileWithLanguage, testInputFile), props);
    assertThat(config.toString()).isEqualTo("[\n" +
      "  baseDir: " + baseDir.toString() + "\n" +
      "  workDir: " + workDir.toString() + "\n" +
      "  extraProperties: {sonar.java.libraries=foo bar}\n" +
      "  inputFiles: [\n" +
      "    " + srcFile1.toString() + " (UTF-8)\n" +
      "    " + srcFile2.toString() + " (UTF-8) [java]\n" +
      "    " + srcFile3.toString() + " (UTF-8) [test] [php]\n" +
      "  ]\n" +
      "]\n");
    assertThat(config.baseDir()).isEqualTo(baseDir);
    assertThat(config.workDir()).isEqualTo(workDir);
    assertThat(config.inputFiles()).containsExactly(inputFile, inputFileWithLanguage, testInputFile);
    assertThat(config.extraProperties()).containsExactly(entry("sonar.java.libraries", "foo bar"));
  }
}
