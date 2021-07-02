package com.baremaps.osm.domain;

import com.baremaps.osm.handler.EntityConsumer;
import com.baremaps.osm.handler.EntityFunction;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents the bounds of an OpenStreetMap dataset.
 */
public class Bound implements Entity {

  private final double maxLat;

  private final double maxLon;

  private final double minLat;

  private final double minLon;

  public Bound(double maxLat, double maxLon, double minLat, double minLon) {
    this.maxLat = maxLat;
    this.maxLon = maxLon;
    this.minLat = minLat;
    this.minLon = minLon;
  }

  public double getMaxLat() {
    return maxLat;
  }

  public double getMaxLon() {
    return maxLon;
  }

  public double getMinLat() {
    return minLat;
  }

  public double getMinLon() {
    return minLon;
  }

  @Override
  public void visit(EntityConsumer consumer) throws Exception {
    consumer.match(this);
  }

  @Override
  public <T> T visit(EntityFunction<T> function) throws Exception {
    return function.match(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Bound)) {
      return false;
    }
    Bound bound = (Bound) o;
    return Double.compare(bound.maxLat, maxLat) == 0 &&
        Double.compare(bound.maxLon, maxLon) == 0 &&
        Double.compare(bound.minLat, minLat) == 0 &&
        Double.compare(bound.minLon, minLon) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxLat, maxLon, minLat, minLon);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Bound.class.getSimpleName() + "[", "]")
        .add("maxLat=" + maxLat)
        .add("maxLon=" + maxLon)
        .add("minLat=" + minLat)
        .add("minLon=" + minLon)
        .toString();
  }

}
