package io.gazetteer.tiles.postgis;

import io.gazetteer.tiles.postgis.PostgisConfig;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostgisConfigTest {

  @Test
  public void load() {
    InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.yaml");
    PostgisConfig config = PostgisConfig.load(input);
    assertNotNull(config);
    assertTrue(config.getLayers().size() == 2);
  }
}