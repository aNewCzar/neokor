DROP INDEX IF EXISTS osm_ways_tags_index;
DROP INDEX IF EXISTS osm_ways_geom_index;

DROP VIEW IF EXISTS osm_ways_z20 CASCADE;
DROP VIEW IF EXISTS osm_ways_z19 CASCADE;
DROP VIEW IF EXISTS osm_ways_z18 CASCADE;
DROP VIEW IF EXISTS osm_ways_z17 CASCADE;
DROP VIEW IF EXISTS osm_ways_z16 CASCADE;
DROP VIEW IF EXISTS osm_ways_z15 CASCADE;
DROP VIEW IF EXISTS osm_ways_z14 CASCADE;
DROP VIEW IF EXISTS osm_ways_z13 CASCADE;

DROP MATERIALIZED VIEW IF EXISTS osm_ways_z12 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z11 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z10 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z9 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z8 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z7 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z6 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z5 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z4 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z3 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z2 CASCADE;
DROP MATERIALIZED VIEW IF EXISTS osm_ways_z1 CASCADE;

DROP MATERIALIZED VIEW IF EXISTS osm_ways_member CASCADE;