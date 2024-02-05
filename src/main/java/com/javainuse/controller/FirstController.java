package com.javainuse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainuse.dto.EntityDto;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import javax.jms.*;
import java.io.IOException;

/**
 * @author ESSID Saria
 */

@RestController
@RequestMapping("/producer")
public class FirstController {

	@Autowired
	JmsTemplate jmsTemplate;

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Value("${spring.activemq.user}")
	private String user;

	@Value("${spring.activemq.password}")
	private String password;

	@GetMapping("/message")
	public String test() {
		return "Hello JavaInUse Called in First Service";
	}

	@GetMapping("/sendRequestM1")
	public EntityDto sendRequest_Method1() throws IOException, JMSException
	{

		EntityDto entityDto = new EntityDto(1, "Hello World");

		/**********************************************************************************************************/
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		Connection connection = connectionFactory.createConnection(user, password);
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("method1-ms");

		connection.start();

		ObjectMapper mapper = new ObjectMapper();
		String studentAsJson = mapper.writeValueAsString(entityDto);
		jmsTemplate.convertAndSend(queue, studentAsJson);

		System.out.println("Method 1 ########################## S E N D     R E Q U E S T     BY  ACTIVEMQ " + entityDto);

		connection.close();
		/**********************************************************************************************************/

		return entityDto;

	}

	@GetMapping("/sendRequestM2")
	public EntityDto sendRequest_Method2() throws IOException, JMSException
	{

		EntityDto entityDto = new EntityDto(1, "Hello World");

		try
		{
			/**********************************************************************************************************/

			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
			((ActiveMQConnectionFactory) connectionFactory).setTrustAllPackages(true);
			Queue queue = new ActiveMQQueue("method2-ms");

			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);

            /*TextMessage message = session.createTextMessage("Hello, JMS!");
            producer.send(message);*/

            /*ObjectMapper mapper = new ObjectMapper();
            String studentAsJson = mapper.writeValueAsString(entityDto1);
            jmsTemplate.convertAndSend(queue, studentAsJson);*/

			ObjectMessage message = session.createObjectMessage(entityDto);
			producer.send( message );

			producer.close();
			session.close();
			connection.close();

			System.out.println("Method 2 ########################## S E N D     R E Q U E S T     BY  ACTIVEMQ " + entityDto);

			/**********************************************************************************************************/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return entityDto;
	}

}
