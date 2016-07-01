package com.svds.kafka.connect.opentsdb

import mockws.{MockWS, Route}
import org.apache.kafka.connect.data.{Schema, SchemaBuilder, Struct}
import org.apache.kafka.connect.sink.SinkRecord
import org.scalatest.{BeforeAndAfter, FlatSpec}
import play.api.mvc.Action
import play.api.mvc.Results._

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class OpenTsdbSinkTaskSpec extends FlatSpec with BeforeAndAfter {

  private val route = Route {
    case ("POST", "http://localhost:4242/api/put") => Action { Ok("")}
  }
  private val ws = MockWS(route)

  private val task = new OpenTsdbSinkTask(ws)

  private val topic = "eeg"

  private val tagsSchema = SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.STRING_SCHEMA).build()
  private val valueSchema = SchemaBuilder.struct()
    .field("metric", Schema.STRING_SCHEMA)
    .field("timestamp", Schema.INT64_SCHEMA)
    .field("value", Schema.FLOAT64_SCHEMA)
    .field("tags", tagsSchema)
    .build()

  before {
    val props = Map[String, String]()
    this.task.start(props)
  }

  it should "write records to OpenTSDB via the HTTP REST JSON API (when there are more than 0 records)" in {
    assert(!this.route.called)
    val emptyDataPoints = Seq()
    this.task.put(emptyDataPoints)
    assert(!this.route.called)

    val sinkRecordValue = new Struct(valueSchema)
      .put("metric", "af3")
      .put("timestamp", 0L)
      .put("value", 1.0)
      .put("tags", Map("testKey" -> "testValue").asJava)
    val sinkRecord = new SinkRecord(this.topic, 0, Schema.STRING_SCHEMA, "unused", valueSchema, sinkRecordValue, 0)
    val dataPoints = Seq(sinkRecord)
    this.task.put(dataPoints)
    assert(this.route.called)
  }

  after {
    this.task.stop()
  }
}
