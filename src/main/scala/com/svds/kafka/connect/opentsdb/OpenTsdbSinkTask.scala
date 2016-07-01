package com.svds.kafka.connect.opentsdb

import java.util.{Collection, Map}

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.connect.data.Struct
import org.apache.kafka.connect.errors.ConnectException
import org.apache.kafka.connect.sink.{SinkRecord, SinkTask}
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.libs.ws.ning.NingWSClient

import scala.collection.JavaConversions._

class OpenTsdbSinkTask(private val wsClient: WSClient) extends SinkTask with LazyLogging {

  private var request: WSRequest = null

  def this() = this(NingWSClient())

  override def start(props: Map[String, String]) = {
    logger.debug("start")
    val config = new OpenTsdbConnectorConfig(props)
    val host = config.getString(OpenTsdbConnectorConfig.OpenTsdbHost)
    val port = config.getInt(OpenTsdbConnectorConfig.OpenTsdbPort)
    val urlString = s"http://${host}:${port}/api/put"
    logger.debug(s"start, urlString == ${urlString}")
    this.request = this.wsClient.url(urlString)
  }

  @throws(classOf[ConnectException])
  override def put(records: Collection[SinkRecord]) = {
    //logger.debug("put")
    if (records.size > 0) {
      //logger.debug(s"records.size == ${records.size}")
      val jsons = records map { record =>
        //logger.debug(s"put: ${record.toString}, value schema - ${record.valueSchema()}")
        val struct = record.value().asInstanceOf[Struct]
        val metric = struct.getString("metric")
        val timestamp = struct.getInt64("timestamp")
        val value = struct.getFloat64("value")
        val tags: Map[String, String] = struct.getMap("tags")

        val tagKeysAndValues = tags map { case (k, v) =>
          (k, JsString(v))
        }
        Json.obj(
          "metric" -> metric,
          "timestamp" -> BigDecimal(timestamp),
          "value" -> BigDecimal(value),
          "tags" -> JsObject(tagKeysAndValues)
        )
      }
      //logger.debug(s"${JsArray(jsons.toSeq).toString()}")
      this.request.post(JsArray(jsons.toSeq))
      //response map { r => logger.debug(s"response body: ${r.body}")}
    }
  }

  override def flush(offsets: Map[TopicPartition, OffsetAndMetadata]) = {
    logger.debug("flush")
  }

  override def stop = {
    logger.debug("stop")
    this.wsClient.close()
  }

  override def version = "0.0.1"
}
