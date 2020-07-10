package br.com.alura.ecommerce;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		SpringApplication.run(EcommerceApplication.class, args);
		
		// cria
		try(var dispatcher = new KafkaDispatcher()){
			for (int i = 0; i < 100; i++) {
				// Envia
				var key = UUID.randomUUID().toString();
				var value = key + ",CACHORRINHO-FIA-DA-PUTA,707070";
				dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);
	
				var email = "Thank you  for your order.. we are processing your order!";
				dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
			}
		}
	}

}
