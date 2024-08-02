package ram.learn.runner;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReader{
	
	@JmsListener(destination="Ram")//"${my.mq.desti-name}")
	public void readMsg(String message) {
		System.out.println("DATA AT CONSUMER===>"+message);
	}

}
