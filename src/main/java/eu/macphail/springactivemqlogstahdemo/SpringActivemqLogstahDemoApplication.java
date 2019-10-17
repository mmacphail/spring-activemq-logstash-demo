package eu.macphail.springactivemqlogstahdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
@SpringBootApplication
public class SpringActivemqLogstahDemoApplication implements CommandLineRunner {

	@Autowired
	private JmsTemplate jmsTemplate;

	private static Logger LOG = LoggerFactory.getLogger(SpringActivemqLogstahDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringActivemqLogstahDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for(int i=0; i<1000; i++) {
			jmsTemplate.send("hello-world", session -> session.createTextMessage("Hello world"));
		}
		LOG.info("message was sent using JMS");
	}

	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@JmsListener(destination = "other topic")
	public void receiveMessage(String text) {
		LOG.info("Received message " + text + " by jms");
	}
}
