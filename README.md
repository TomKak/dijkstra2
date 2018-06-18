# Running || Cleaning

To compile by: `make` command.

java -jar -Djava.rmi.server.hostname=127.0.1.1 Server/Server.jar

java -jar -Djava.rmi.server.hostname=127.0.1.1 Client/DijkstraClient.jar

By .jar:

    javac MyApp.java
	
    jar -cf myJar.jar myApp


To run server by: 

make server RegistryIP=127.0.1.1 Ports="1132 1133 1134" 

make runserver RegistryIP=127.0.1.1 Ports="1132 1133 1134""


To run client by: 

make client Testcase=0 RegistryIP=127.0.0.1 Ports="1132 1133 1134" 

make runclient Testcase=0  RegistryIP=127.0.1.1 Ports="1132 1133 1134"


To clean by: 'make clean'

"# dijkstra2"

Problem:
https://stackoverflow.com/questions/15685686/java-rmi-connectexception-connection-refused-to-host-127-0-1-1