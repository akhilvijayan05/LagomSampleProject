package com.example.impl.kafkaprocessing

import java.util.{Collections, Properties}

import com.example.models.Log
import com.fasterxml.jackson.databind.JsonNode
import org.apache.kafka.clients.consumer.KafkaConsumer
import play.api.libs.json.Json

import scala.collection.JavaConversions._
import scala.io.Source.fromURL

trait Consumer {

  private lazy val subscribe = (topic: String) => consumer.subscribe(Collections.singletonList(topic))

  val props = new Properties()
  val reader = fromURL(getClass.getResource("/kafkaconsumerprocessing.properties")).bufferedReader()
  props.load(reader)

  val consumer = new KafkaConsumer[String, JsonNode](props)

  def consumeData(topic: String): Map[String, Log] = {
    subscribe(topic)
    val consumerRecords = consumer.poll(5000)

    val result = for {
      record <- consumerRecords
    } yield {
      record.key() -> Json.parse(record.value().toString).as[Log]
    }

    println(result.toMap)
    result.toMap
  }

}

object ConsumerApplication extends App with Consumer {

  while (true) {
    consumeData("MyTopic")
  }
}
