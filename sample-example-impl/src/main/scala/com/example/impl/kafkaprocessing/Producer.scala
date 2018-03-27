package com.example.impl.kafkaprocessing

import java.util.Properties

import com.example.impl.utils.constant.Constants
import com.example.models.{Log, Request, Response}
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source.fromURL

trait Producer {

  val reader = fromURL(getClass.getResource(Constants.KAFKA_CONFIG_FILE)).bufferedReader()
  val props = new Properties()
  val mapper = new ObjectMapper()

  props.load(reader)
  val producer = new KafkaProducer[String, JsonNode](props)

  def produceData(log: Log, topic: String): String = {

    val rootJson: ObjectNode = mapper.createObjectNode()
    val requestJson = mapper.createObjectNode()
    val responseJson = mapper.createObjectNode()

    requestJson.put("id", log.request.id)
    requestJson.put("message", log.request.message)

    responseJson.put("message", log.response.message)

    rootJson.put("timestamp", log.timestamp)
    rootJson.set("request", requestJson)
    rootJson.set("response", responseJson)

    val root: JsonNode = mapper.readTree(rootJson.toString)

    val producerRecord = new ProducerRecord[String, JsonNode](topic, log.timestamp, root)
    val recordMetaData = producer.send(producerRecord)
    producer.close()
    recordMetaData.get().topic()
  }
}

object MyProducer extends App with Producer {
  produceData(Log("123", Request("9999","New Json Mesaage"), Response("Json Message")), "NewTopic")
}