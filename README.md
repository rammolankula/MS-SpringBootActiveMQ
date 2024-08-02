Message Queues(MQS)
===================
<pre>
MQs used to send data from one system(Application) to another system (Application) in continous flow.
=>Examples

a.Live Train Status
b.Food Delivery status
c.cab status
d.Cricket score/math score details..etc

 &#8594; Mqs can be implemented using basic protocol and advanced protocols
BMQP &#8594; Basic message queue protocol (Apache ActiveMQ)
AMQP &#8594;Advanced Message Queue protocol (Apache Kafka)
</pre>
==============================================================================
##### Spring Boot -JMS (Java Message Services)
<pre>
 &#8594; JMS is used to transfer data between two java applications
 &#8594; JMS depends on TCP protocol.
 &#8594; Here data transfer is called as producer, data receiver is called as consumer
 &#8594; Producer and consumer will communicate using MOM (Message Oriented Middleware) mediator
  software. EX:Apache ActiveMQ
  
 &#8594; JMS supports two types of MQS communication(2).
a) Peer-To-Peer (P2P) communication.
  one message from provider is given to only one consumer.
 
b) Publish and subscribe(PUB/SUB) communication
   one message from provider is given to multiple consumer.
   
 &#8594; MOM contains one destination that holds messages given by producer app.

 &#8594; Destination are two types based on communication types:
  a.Queue destination(P2P communication)
  b.Topic Destination(for Pub/Sub communication)
  
</pre>
MOM-SETUP
=========
<pre>
->Go to download link:
   https://activemq.apache.org/component/classic/download/
   ->Download one option (For windows: active-activemq-5.18.2-bin.zip
   ->Extract the folder
   ->open folder
   ->Double click on batch file : ativemq.batch
   ->it will be started on port :8671
   ->Goto browser and enter URL :http://127.0.0.1:8161/
   ->Enter un/pwd: admin/admin
   ->Click on queues and topic links
   ->To stop server :ctr+C > press y
</pre>
 ======================================================================
 
<b> Spring boot has provided on starter : Spring-boot-starter-activemq to do MQs programming using JMS</b>
 
 =>JmsTemplate (c) depends on connectionfactory(I) and connectionfactory has Impl class
   ActiveMqConnectionfactory(c)
 =>Spring boot has provided AutoConfiguration for above beans
<pre>
@Configuration
public class AppConfig{

   @Bean
   public connectionFcatory connectionfactory(){
   ActiveMQconnectionFactory af=new ActiveMQconnectionFactory();
   af.setBrokerURL("tcp://localhost:61616");
   return af;
   }
   public JmsTemplate jmsTemplate(){
   JmsTemplate jt=new JmsTemplate();
   jt.setConnectionFactory(connectionFactory));
   return jt;
   }
}
</pre>
----------------------------------------------------------------
 &#8594; <b>Here we use JmsTemplate(c) to send data from producer to MOM and @JmsListener Annotation is used to read data from MOM to consumer ny using one destination type with name.</b>
  
 &#8594; <b>To Do any communication, first there must be a connection created between applivcations(Producer/Consumer) and MOM. we need to provide below details in properties file</b>
 <pre>
 ------application.properties------------------
 spring.activemq.broker-url=tcp://localhost:61616
 spring.activemq.user=admin
 spring.activemq.password=admin
 --------------------------------------------------------------------
</pre>
<pre>
 JmsTemplate(c) has a method send(MessageCreator).
 Here MessageCreatot is functional interface. so we can write Lambda Expression:
 
 MessageCreator om=(session)->{
          return session.creatTextMessage("Hello");
};
</pre>
PART-2
======
<pre>
 &#8594; ActiveMQ5 Download ->Extract->start
 &#8594; To work Producer/Consumer application using spring boot JMS and ActiveMQ choose spring for apache ActiveMQ Dependency in pom.xml
 &#8594; In application.properties file(at both consumer and producer applications)
  &#8594; spring.activemq.broker-url=tcp://localhost:61616
  &#8594; spring.activemq.user=admin
  &#8594; spring.activemq.password=admin
 #P2P
 spring.jms.pub-sub-domain=false
</pre>
 Note:&#8594; <b>spring.jms.pub-sub-domain default value is false. if the value is true then
   communication type is Pub/Sub, else if value id false, then type is P2P.</b>
   
===================================================================================
<pre>
 &#8594; Use JmsTemplate(c) object as autowired. this is auto-configured by spring boot.
 
 &#8594; This template class is having a method send(destinationName,messageCreator);
     jt.send(String destinationName,MessageCreator messageCreator);	 
     
 &#8594; For Type either Queue/Topic, we must provide one name. ie destination name.
   ex: my-queue-data=Ram

 &#8594; MessageCreator Object can be created using below
</pre>
<pre>
a)Annoymous Inner class

/*	jt.send(destinartionName,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage tm=session.createTextMessage("RAM FROM PRODUCER=>"+new Date());
				return tm;
			}
		}  );*/
b)Lambda Expression
 jt.send(destinartionName,
				//message creator object   
				(session)->session.createTextMessage("RAM FROM PRODUCER=>"+new Date()));
 </pre>
Code Flow
=========
<pre>
Create a Starter project 
Name: SpringBootActiveMQProducerApplication
Dep: Spring For Apache ActiveMQ
===================
MessageSender class
===================
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
Consumer Application
==================== 
=>Destination name,must be same as producer application name.
=>Define one class and having one method, at method level apply

  @JmsListener(destination="")

=>On Receiving message to MOM, it will be sent to its consumer application by using destination-type and name

=>****  At consumer application side, we should apply @EnableJms at starter class
      @EnableJms not required at producer.
	  
=>Incase of producer app, we are using JMStemplate(C) that activates JMS internally

</pre>
CODE
====
<pre>
1.create one starter project
Name:SpringActiveMQProducer
Dep: Spring boot active mq

Message Reader Class
====================

@Component
public class MessageReader{
	
	@JmsListener(destination="Ram")//"${my.mq.desti-name}")
	public void readMsg(String message) {
		System.out.println("DATA AT CONSUMER"+message);
	}

application.properties
======================
spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
 #P2P
spring.jms.pub-sub-domain=false
my.mq.desti-name=Ram

======Execution================
 1.Start ActiveMQ
 2.Start Consumer Application
 3.Start Producer Application
 </pre>
 
 *)Note: we can define producer  application using scheduler
   (USE COMMAND LINE RUNNER ONLY FOR TEST PURPOSE) 
 #### Flow Of Execution
 =======================
<pre>
<b>a.start ActiveMQ(MOM server)
 &#8594; If we run activemq batch file, MQ is started and allows Producer/Consumer applications
         for connection and data transfer</b>

<b>b.start consumer application
 &#8594; When we start consumer application
  i.Creates connection between consumer and MOM
  ii.Register consumer as queue Type/Topic based on key
    spring.jms.pub-sub-domain=
	Note: spring.jms.pub-sub-domain is false then queue, else if true then topic
  iii.Create a Queue/Topic at MOM using @JmsListener destination-name.
  iv.waiting for messages Enqueued.Once they received, then they will be read
     by consumer ie message dequeued</b>
	 
<b>C. Start Producer Application
 &#8594; When we start Producer application
  i.Creates Connection between consumer and MOM
  ii.For producer app no register required,but need to confirm type:
  Queue type/topic type based on key spring.jms.pub-sub-domain.
  Note: spring.jms.pub-sub-domain is false then queue, else if true then topic
  iii.Link with a queue/Topic at MOM using JmsTemplate destination name.
  iv.sending messages to MOM using one Destination name.</b>
  
Note:
 &#8594; if no.of consumers are zero, then enqueued messages are stored in MOM Buffer
  (Supports upto RAM size3 available) and they are given count as no.of pending messages

 &#8594; Once Consue=mer is connected (after some time, now no.of pending messages=4)
   then it will read all pending messages. (They are available until system restart).
   
   =>**if MOM is not responding , there might be message loss(Data loss may occure).
   =>** if no.of Producers/Consumers are more then MOM works very slow.
   =>** Not recommanded to use heavy data transfer.
=======================================================================
 &#8594; Can we use P2P concept to send same data to multiple consumers?
A)No. use P2P only for one consumer concept.

 &#8594; ** Can we use Pub/Sub concept to send message to one consumer?
A)Yes. we can use Pub/Sub type for bother 1 message to one consumer and
  1.Message to Multiple consumers.
</pre>

Active MQ view of Enqueued and Dequeued Msgs
![Screenshot (9)](https://github.com/user-attachments/assets/d9cfd468-6c7e-4f79-87cc-02b656d3a07b)


Consumer Application
![Screenshot (11)](https://github.com/user-attachments/assets/4dd52bdb-3d93-4dbe-80b1-e5f1c2e3cfdc)


Producer Application

![Screenshot (10)](https://github.com/user-attachments/assets/4c884615-442e-4f7a-bdc4-9a3625233034)

