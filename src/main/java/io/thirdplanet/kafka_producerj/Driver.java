package io.thirdplanet.kafka_producerj;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by mikeyb on 7/29/17.
 * https://kafka.apache.org/0100/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
 */
public class Driver {
    private static String topicName = "jproducer";
    public static void main(String args[]) {
        System.out.println("Runs");
        Properties props = new Properties();
        props.put("metadata.broker.list", "broker1:9092,broker2:9092");
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String,String>  producer = new KafkaProducer<String, String>(props);
        String topic = "pfsense";

       for(int i = 0; i < 100 ; i++){
          // producer.send(new ProducerRecord<String, String>(topicName,
            //       Integer.toString(i), Integer.toString(i)));
           //System.out.println("Message sent succesfully ");
           //producer.close();
           Random rnd = new Random();

           long runtime = new Date().getTime();

           String ip = "192.168.1";
           int rndInt = rnd.nextInt(255);
           String rndIntStr = new Integer(rndInt).toString();
           String msg = "http://www.example.com:" + ip  + rndIntStr;
           ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>(topic,ip,msg);
           producer.send(producerRecord);
       }
       producer.close();
    }
}
