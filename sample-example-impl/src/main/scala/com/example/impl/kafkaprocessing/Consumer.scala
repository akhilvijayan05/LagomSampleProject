package com.example.impl.kafkaprocessing

import java.util.{Collections, Properties}

import com.example.models.Log
import com.fasterxml.jackson.databind.JsonNode
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import play.api.libs.json.Json

import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.io.Source.fromURL

trait Consumer {

  val reader = fromURL(getClass.getResource("/kafkaconsumer.properties")).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val consumer = new KafkaConsumer[String, JsonNode](props)

  def consumeData(topic: String): Map[String, Log] = {

    consumer.subscribe(Collections.singletonList(topic))
    val result = consumerPoll(topic, consumer.poll(5000), Map())
    consumer.close()
    result
  }

  @tailrec
  private def consumerPoll(topic: String, records: ConsumerRecords[String, JsonNode], data: Map[String, Log]): Map[String, Log] = {

    if (records.count() == 0) {
      data
    } else {
      val record = for {
        record <- records
      } yield {
        record.key() -> Json.parse(record.value().toString).as[Log]
      }
      consumerPoll(topic, consumer.poll(5000), data ++ record.toMap)
    }
  }
}

