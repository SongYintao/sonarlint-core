/*
 * SonarLint Language Server
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
package org.sonarlint.languageserver.contextful;

import java.util.Collections;
import java.util.List;
import org.sonarsource.sonarlint.core.client.api.common.analysis.ClientInputFile;
import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;

public class SimpleIssue implements Issue {
  private String message;
  private Integer startLine;
  private Integer startLineColumn;
  private ClientInputFile inputFile;

  public SimpleIssue(String message, Integer startLine, Integer startLineColumn, ClientInputFile inputFile) {
    this.message = message;
    this.startLine = startLine;
    this.startLineColumn = startLineColumn;
    this.inputFile = inputFile;
  }

  @Override
  public String getSeverity() {
    return "CRITICAL";
  }

  @Override
  public String getType() {
    return "BUG";
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public String getRuleKey() {
    return "ts:S4268";
  }

  @Override
  public String getRuleName() {
    return "SAMPLE RULE";
  }

  @Override
  public Integer getStartLine() {
    return this.startLine;
  }

  @Override
  public Integer getStartLineOffset() {
    return this.startLineColumn;
  }

  @Override
  public Integer getEndLine() {
    return null;
  }

  @Override
  public Integer getEndLineOffset() {
    return null;
  }

  @Override
  public List<Flow> flows() {
    return Collections.emptyList();
  }

  @Override
  public ClientInputFile getInputFile() {
    return this.inputFile;
  }
}
