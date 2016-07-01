package com.svds.kafka.connect.opentsdb

import java.util.Map
import org.apache.kafka.common.config.ConfigDef.{Importance, Type}
import org.apache.kafka.common.config.{AbstractConfig, ConfigDef}

object OpenTsdbConnectorConfig {
  val OpenTsdbHost = "opentsdb.host"
  val OpenTsdbPort = "opentsdb.port"

  val config = new ConfigDef()
    .define(OpenTsdbHost, Type.STRING, "localhost", Importance.HIGH, "OpenTSDB host")
    .define(OpenTsdbPort, Type.INT, 4242, Importance.LOW, "OpenTSDB port")
}

class OpenTsdbConnectorConfig(originals: Map[String, String]) extends AbstractConfig(OpenTsdbConnectorConfig.config, originals.asInstanceOf[Map[String, Object]]) {

}
