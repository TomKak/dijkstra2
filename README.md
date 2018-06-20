# Running || Cleaning

To compile by: `make` command.

java -jar -Djava.rmi.server.hostname=127.0.1.1 Server/Server.jar

java -jar -Djava.rmi.server.hostname=127.0.1.1 Client/DijkstraClient.jar

By .jar:

    javac MyApp.java
	
    jar -cf myJar.jar myApp
next: 
	java -jar -Djava.rmi.server.hostname="ip localhost or taurus" myApp.jar

To run server by: 

make server hostIP=127.0.0.1 Ports="1132 1133 1334" 
or
make server hostIP=127.0.1.1 Ports="1132 1133 1134"
etc...

To run client by: 

make client Testcase=0 host=127.0.0.1 Ports="1132 1133 1334" 
or
make client Testcase=0  host=127.0.1.1 Ports="1132 1133 1134"
etc...

To clean by: 'make clean'


Problem solved:
https://stackoverflow.com/questions/15685686/java-rmi-connectexception-connection-refused-to-host-127-0-1-1