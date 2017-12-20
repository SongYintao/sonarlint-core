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

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.sonarlint.languageserver.DefaultClientInputFile;
import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;

public class ContextualAnalyzer {

  private Socket socket;

  public List<Issue> analyze(URI uri, String content, BiConsumer<String, Throwable> logger) {
    try {
      connect();
      final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
      writer.append(new Gson().toJson(new AnalysisRequest(uri.getPath(), content)));
      writer.flush();
      AnalysisResponse response = new Gson().fromJson(new JsonReader(new InputStreamReader(socket.getInputStream())), AnalysisResponse.class);
      return toIssues(response, uri, content);
    } catch (IOException e) {
      logger.accept("Failed to connect to sonarts server", e);
    }
    return Collections.emptyList();
  }

  private List<Issue> toIssues(AnalysisResponse response, URI uri, String content) {
    return Arrays.stream(response.issues).map(failure -> new SimpleIssue(failure.failure, failure.startPosition.line, failure.startPosition.character, new DefaultClientInputFile(uri, content, null, null))).collect(Collectors.toList());
  }

  private void connect() throws IOException {
    if (this.socket == null) {
      this.socket = new Socket("localhost",55555);
    }
  }

  private class AnalysisRequest {
    public String operation = "analyze";
    public String file;
    public String content;


    public AnalysisRequest(String file, String content) {
      this.file = file;
      this.content = content;
    }
  }

  private static class Failure {

    String failure;
    Position startPosition;
    Position endPosition;
    String name;
    String ruleName;
  }
  private static class Position {

    Integer line;
    Integer character;
  }
  public static class AnalysisResponse {

    String filepath;
    Failure[] issues = {};
    Highlight[] highlights = {};
    CpdToken[] cpdTokens = {};
    int[] ncloc = {};
    int[] commentLines = {};
    Integer[] nosonarLines = {};
    int[] executableLines = {};
    int functions = 0;
    int statements = 0;
    int classes = 0;
  }

  private static class Highlight {
    Integer startLine;
    Integer startCol;
    Integer endLine;
    Integer endCol;
    String textType;
  }

  private static class CpdToken {
    Integer startLine;
    Integer startCol;
    Integer endLine;
    Integer endCol;
    String image;
  }

}
