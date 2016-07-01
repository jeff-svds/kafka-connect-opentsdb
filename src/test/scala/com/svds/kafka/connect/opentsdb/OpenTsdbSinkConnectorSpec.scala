package com.svds.kafka.connect.opentsdb

import org.scalatest.FlatSpec

import scala.collection.JavaConversions._
import scala.collection.mutable.Map

class OpenTsdbSinkConnectorSpec extends FlatSpec {

  private val connector = new OpenTsdbSinkConnector

  it should "create a list of the number of tasks configurations specified by maxTasks, even if each task configuration element of the list is empty" in {
    assert(this.connector.taskConfigs(2).size == 2)
  }

  it should "have default configuration with localhost and 4242 as the OpenTSDB host & port" in {
    val config = this.connector.config
    val props = Map[String, String]()
    val parsedProps = config.parse(props)
    assert(parsedProps(OpenTsdbConnectorConfig.OpenTsdbHost) == "localhost")
    assert(parsedProps(OpenTsdbConnectorConfig.OpenTsdbPort) == 4242)
  }

  it should "return OpenTsdbSinkTask as its task class" in {
    assert(this.connector.taskClass == classOf[OpenTsdbSinkTask])
  }
}
