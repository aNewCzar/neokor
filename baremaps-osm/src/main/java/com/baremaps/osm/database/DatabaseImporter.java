/*
 * Copyright (C) 2020 The Baremaps Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.baremaps.osm.database;

import com.baremaps.osm.geometry.NodeBuilder;
import com.baremaps.osm.geometry.RelationBuilder;
import com.baremaps.osm.geometry.WayBuilder;
import com.baremaps.osm.pbf.FileBlock;
import com.baremaps.osm.pbf.HeaderBlock;
import com.baremaps.osm.pbf.PrimitiveBlock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DatabaseImporter implements Consumer<FileBlock> {

  private final HeaderTable headerTable;

  private final NodeBuilder nodeBuilder;

  private final WayBuilder wayBuilder;

  private final RelationBuilder relationBuilder;

  private final NodeTable nodeTable;

  private final WayTable wayTable;

  private final RelationTable relationTable;

  public DatabaseImporter(
      HeaderTable headerTable,
      NodeBuilder nodeBuilder,
      WayBuilder wayBuilder,
      RelationBuilder relationBuilder,
      NodeTable nodeTable,
      WayTable wayTable,
      RelationTable relationTable) {
    this.headerTable = headerTable;
    this.nodeBuilder = nodeBuilder;
    this.nodeTable = nodeTable;
    this.wayBuilder = wayBuilder;
    this.wayTable = wayTable;
    this.relationBuilder = relationBuilder;
    this.relationTable = relationTable;
  }

  @Override
  public void accept(FileBlock block) {
    switch (block.getType()) {
      case OSMHeader:
        accept(block.toHeaderBlock());
        break;
      case OSMData:
        accept(block.toPrimitiveBlock());
        break;
      default:
        break;
    }
  }

  private void accept(HeaderBlock headerBlock) {
    try {
      headerTable.insert(headerBlock);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void accept(PrimitiveBlock primitiveBlock) {
    try {
      nodeTable.copy(
          primitiveBlock.getDenseNodes().stream()
              .map(node -> new NodeTable.Node(
                  node.getInfo().getId(),
                  node.getInfo().getVersion(),
                  node.getInfo().getTimestamp(),
                  node.getInfo().getChangeset(),
                  node.getInfo().getUserId(),
                  node.getInfo().getTags(),
                  nodeBuilder.build(node)))
              .collect(Collectors.toList()));
      nodeTable.copy(
          primitiveBlock.getNodes().stream()
              .map(node -> new NodeTable.Node(
                  node.getInfo().getId(),
                  node.getInfo().getVersion(),
                  node.getInfo().getTimestamp(),
                  node.getInfo().getChangeset(),
                  node.getInfo().getUserId(),
                  node.getInfo().getTags(),
                  nodeBuilder.build(node)))
              .collect(Collectors.toList()));
      wayTable.copy(
          primitiveBlock.getWays().stream()
              .map(way -> new WayTable.Way(
                  way.getInfo().getId(),
                  way.getInfo().getVersion(),
                  way.getInfo().getTimestamp(),
                  way.getInfo().getChangeset(),
                  way.getInfo().getUserId(),
                  way.getInfo().getTags(),
                  way.getNodes(),
                  wayBuilder.build(way)))
              .collect(Collectors.toList()));
      relationTable.copy(
          primitiveBlock.getRelations().stream()
              .map(relation -> new RelationTable.Relation(
                  relation.getInfo().getId(),
                  relation.getInfo().getVersion(),
                  relation.getInfo().getTimestamp(),
                  relation.getInfo().getChangeset(),
                  relation.getInfo().getUserId(),
                  relation.getInfo().getTags(),
                  relation.getMembers().stream().map(m -> m.getRef()).toArray(Long[]::new),
                  relation.getMembers().stream().map(m -> m.getType().name()).toArray(String[]::new),
                  relation.getMembers().stream().map(m -> m.getRole()).toArray(String[]::new),
                  relationBuilder.build(relation)))
              .collect(Collectors.toList()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
