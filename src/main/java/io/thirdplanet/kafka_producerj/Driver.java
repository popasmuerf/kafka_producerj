package io.thirdplanet.kafka_producerj;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    static Logger logger = LoggerFactory.getLogger(Driver.class);
    static Config defaultConfig = ConfigFactory.parseResources("default.conf");

    public static void main(String args[]) throws IOException {
        logger.info("Running : " + Thread.currentThread().getName());
        Properties props = new Properties();
        props.put("metadata.broker.list",defaultConfig.getString("kafka.meta_brokers"));
        props.put("bootstrap.servers", defaultConfig.getString("kafka.brokers"));
        props.put("acks", defaultConfig.getString("kafka.acks"));
        props.put("retries", defaultConfig.getString("kafka.retries"));
        props.put("batch.size", defaultConfig.getString("kafka.batch_size"));
        props.put("linger.ms", defaultConfig.getString("kafka.linger_ms"));
        props.put("buffer.memory", defaultConfig.getString("kafka.buffer_memory"));
        props.put("key.serializer", defaultConfig.getString("kafka.key_serializer"));
        props.put("value.serializer", defaultConfig.getString("kafka.value_serializer"));
        Producer<String,String>  producer = new KafkaProducer<String, String>(props);

        String topic = defaultConfig.getString("kafka.topic");

        try {
            runSendLogsFromTextFileStatic (producer,topic);
        } catch (IOException e) {
            e.printStackTrace();
        }

        producer.close();

    }

    public static void testMethod (Producer<String,String> producer,String topic){
        for(int i = 0; i < 100000 ; i++){
            Random rnd = new Random();
            long runtime = new Date().getTime();
            String ip = "192.168.1";
            int rndInt = rnd.nextInt(255);
            String rndIntStr = new Integer(rndInt).toString();
            String msg = defaultConfig.getString("kafka.test_msg");
            ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>(topic,ip,msg);
            producer.send(producerRecord);
            try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e ){}
        }
    }
    public static void runSendLogsFromTextFileStatic (Producer<String,String> producer,String topic) throws IOException {
        String ffpath = defaultConfig.getString("file_paths.pfsense_log");
        BufferedReader br = new BufferedReader(new FileReader(ffpath));
        if(br == null){System.out.println("br is null"); System.exit(-1);};
        String record;
        for(int i = 0; i < 100000 ; i++){
            //----------------------
            if((record = br.readLine()) != null) {
                ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>(topic,record);
                producer.send(producerRecord);
                logger.info(record);
            }
            try{ TimeUnit.SECONDS.sleep(0);}catch(InterruptedException e ){}
        }
        br.close();
    }
}
