package br.com.alura.ecommerce;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService implements Closeable{

	private final KafkaConsumer<String, String> consumer;
	private final ConsumerFunction parse;

	public KafkaService( String groupId ,String topic, ConsumerFunction parse) {
		this.parse = parse;
		this.consumer = new KafkaConsumer<String, String>(properties(groupId));
		consumer.subscribe(Collections.singletonList(topic));

	}

	public void run() {
		// WHILE
		while (true) {
			var records = consumer.poll(Duration.ofMillis(100));
			if (!records.isEmpty()) {
				System.out.println("encontrei " + records.count() + "registros");

				for (var record : records) {
					parse.consume(record);

				}

			}

		} // WHILE END
		
	}

	
	private static Properties properties(String groupId ) {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId );
		//properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, "CarteiroPrateado-" + UUID.randomUUID().toString());
		properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
		return properties;
	}

	@Override
	public void close()  {
		
		consumer.close();
		System.out.println("fechando consumer");
	}
	
}
