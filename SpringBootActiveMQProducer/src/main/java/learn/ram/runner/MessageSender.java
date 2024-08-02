package learn.ram.runner;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

@Component
public class MessageSender implements CommandLineRunner {

	
	@Autowired
	private JmsTemplate jt;
    
	@Value("${my.mq.desti-name}")
    private String destinartionName;	
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		jt.send(destinartionName,
				//message creator object   
				(session)->session.createTextMessage("RAM FROM PRODUCER=>"+new Date()));
	/*	jt.send(destinartionName,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage tm=session.createTextMessage("RAM FROM PRODUCER=>"+new Date());
				return tm;
			}
		}  );*/
	}

}
