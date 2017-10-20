package io.thirdplanet.kafka_producerj;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    //private static String fpath = "/home/mikeyb/data/text_files/logs/pfsense/pflogs.txt";
    //private static String fpath = "/Users/mdb/data/logs/pfsense/pflogs.txt" ;
    private static String fpath = "/Users/mdb/data/logs/pfsense/new.pfsense.txt" ;
    public static void main(String args[]) throws IOException {
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
        try {
            runSendLogsFromTextFileStatic (producer,topic);
        } catch (IOException e) {
            e.printStackTrace();
        }
        producer.close();
    }

    public static void runBasic (Producer<String,String> producer,String topic){
        for(int i = 0; i < 100000 ; i++){
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
    }
    public static void runSendLogsFromTextFileStatic (Producer<String,String> producer,String topic) throws IOException {
        //String ffpath = "/home/mikeyb/data/text_files/logs/pfsense/pflogs.txt";
        //String ffpath = "/Users/mdb/data/logs/pfsense/pflogs.txt" ;
         String ffpath = "/Users/mdb/data/logs/pfsense/new.pfsense.txt" ;
        BufferedReader br = new BufferedReader(new FileReader(ffpath));
        if(br == null){System.out.println("br is null"); System.exit(-1);};
        String record;
        for(int i = 0; i < 100000 ; i++){
            //----------------------
            if((record = br.readLine()) != null) {
                ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>(topic,record);
                producer.send(producerRecord);
                System.out.println(record);
            }
            try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e ){}
        }
        br.close();
    }
}
