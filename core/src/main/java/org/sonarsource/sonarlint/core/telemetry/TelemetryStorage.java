/*
 * SonarLint Core - Implementation
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
package org.sonarsource.sonarlint.core.telemetry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Base64;

/**
 * Serialize and deserialize telemetry data to persistent storage.
 */
class TelemetryStorage {
  private final Path path;

  TelemetryStorage(Path path) {
    this.path = path;
  }

  private void save(TelemetryData data) throws IOException {
    Files.createDirectories(path.getParent());
    Gson gson = createGson();
    String json = gson.toJson(data);
    byte[] encoded = Base64.getEncoder().encode(json.getBytes(StandardCharsets.UTF_8));
    Files.write(path, encoded);
  }

  void trySave(TelemetryData data) {
    try {
      save(data);
    } catch (Exception e) {
      // fail silently
    }
  }

  private static Gson createGson() {
    return new GsonBuilder()
      .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
      .create();
  }

  private TelemetryData load() throws IOException {
    Gson gson = createGson();
    byte[] bytes = Files.readAllBytes(path);
    byte[] decoded = Base64.getDecoder().decode(bytes);
    String json = new String(decoded, StandardCharsets.UTF_8);
    return TelemetryData.validateAndMigrate(gson.fromJson(json, TelemetryData.class));
  }

  TelemetryData tryLoad() {
    try {
      return load();
    } catch (Exception e) {
      return new TelemetryData();
    }
  }
}
