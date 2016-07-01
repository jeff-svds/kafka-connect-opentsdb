package com.svds.kafka.connect.opentsdb

import java.util.{List, Map}

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.connect.errors.ConnectException
import org.apache.kafka.connect.sink.SinkConnector

import scala.collection.JavaConversions._
import scala.collection.mutable

class OpenTsdbSinkConnector extends SinkConnector with LazyLogging {

  private val configProps: Map[String, String] = mutable.Map[String, String]()

  override def config: ConfigDef = {
    logger.debug("config")
    OpenTsdbConnectorConfig.config
  }

  @throws(classOf[ConnectException])
  override def start(props: Map[String, String]) = {
    logger.debug(s"start(props: ${props})")
    this.configProps.putAll(props)
  }

  override def taskClass: Class[OpenTsdbSinkTask] = {
    classOf[OpenTsdbSinkTask]
  }

  def taskConfigs(maxTasks: Int): List[Map[String, String]] = {
    logger.debug(s"taskConfigs(), this.configProps: ${this.configProps}")
    val range = (1 to maxTasks).toBuffer
    val configs = range map { _ : Int =>
      this.configProps
    }
    logger.debug(s"taskConfigs(), configs: ${configs}")
    configs
  }

  override def stop = {}

  override def version = "0.0.1"
}
