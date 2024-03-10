README

project compiled with:
> javac -cp "D:\JUnit\/*;D:\JDOM\jdom-2.0.6.1/*;." ObjectCreator.java Serializer.java Sender.java Receiver.java Deserializer.java Inspector.java
(substitute your own classpaths for Junit and Jdom of course)

project run with:
	Sender program (ObjectCreator.java) on one terminal/computer: 
		> java -cp "D:\JUnit\/*;D:\JDOM\jdom-2.0.6.1/*;." ObjectCreator 127.0.0.1 4321
			i.e
		> java -cp "D:\JUnit\/*;D:\JDOM\jdom-2.0.6.1/*;." ObjectCreator <receiver IP address> <receiver port number>
	
	Receiver program (Receiver.java) on another terminal/computer:
		> java -cp "D:\JUnit\/*;D:\JDOM\jdom-2.0.6.1/*;." Receiver 4321
			i.e
		> java -cp "D:\JUnit\/*;D:\JDOM\jdom-2.0.6.1/*;." Receiver <receiver port number>
		