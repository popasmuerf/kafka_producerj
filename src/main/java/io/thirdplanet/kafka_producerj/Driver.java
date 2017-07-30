package io.thirdplanet.kafka_producerj;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by mikeyb on 7/29/17.
 * https://kafka.apache.org/0100/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
 */
public class Driver {
    private static String topicName = "jproducer";
    private static String fpath = "/home/mikeyb/data/text_files/logs/pfsense";
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
           Random rnd = new Random();
           long runtime = new Date().getTime();
           String ip = "192.168.1";
           int rndInt = rnd.nextInt(255);
           String rndIntStr = new Integer(rndInt).toString();
           String msg = "Jul 13 17:46:51 pfSense filterlog: 5,16777216,,1000000103,xn0,match,block,in,4,0x0,,47,0,0,DF,17,udp,129,37.134.157.194,10.0.0.56,63105,25148,109";
           //String msg = "http://www.example.com:" + ip  + rndIntStr;
           ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>(topic,ip,msg);
           producer.send(producerRecord);
           try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e ){}

       }
       producer.close();
    }
}
