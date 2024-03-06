/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.baremaps.openstreetmap.repository;

import static org.apache.baremaps.testing.OsmSample.SAMPLE_OSM_PBF;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.baremaps.openstreetmap.pbf.PbfBlockReader;
import org.apache.baremaps.openstreetmap.postgres.PostgresHeaderRepository;
import org.apache.baremaps.openstreetmap.postgres.PostgresNodeRepository;
import org.apache.baremaps.openstreetmap.postgres.PostgresRelationRepository;
import org.apache.baremaps.openstreetmap.postgres.PostgresWayRepository;
import org.apache.baremaps.testing.PostgresContainerTest;
import org.apache.baremaps.utils.PostgresUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class BlockImporterTest extends PostgresContainerTest {

  public DataSource dataSource;
  public PostgresHeaderRepository headerRepository;
  public PostgresNodeRepository nodeRepository;
  public PostgresWayRepository wayRepository;
  public PostgresRelationRepository relationRepository;

  @BeforeEach
  void init() throws SQLException, IOException {
    dataSource = PostgresUtils.createDataSource(jdbcUrl(), 1);
    headerRepository = new PostgresHeaderRepository(dataSource);
    nodeRepository = new PostgresNodeRepository(dataSource);
    wayRepository = new PostgresWayRepository(dataSource);
    relationRepository = new PostgresRelationRepository(dataSource);
    try (Connection connection = dataSource.getConnection()) {
      PostgresUtils.executeResource(connection, "queries/osm_create_extensions.sql");
      PostgresUtils.executeResource(connection, "queries/osm_drop_tables.sql");
      PostgresUtils.executeResource(connection, "queries/osm_create_tables.sql");
    }
  }

  @Test
  @Tag("integration")
  void test() throws RepositoryException, IOException {
    // Import data
    BlockImporter blockImporter =
        new BlockImporter(headerRepository, nodeRepository, wayRepository, relationRepository);

    try (InputStream inputStream = Files.newInputStream(SAMPLE_OSM_PBF)) {

      new PbfBlockReader().stream(inputStream).forEach(blockImporter);

      // Check node importation
      for (long i = 1; i <= 36; i++) {
        var exists = false;
        exists |= nodeRepository.get(i) != null;
        exists |= wayRepository.get(i) != null;
        exists |= relationRepository.get(i) != null;
        assertTrue(exists);
      }
    }
  }
}
