# Running || Cleaning

To compile by: `make` command.

java -jar -Djava.rmi.server.hostname=127.0.1.1 Server/Server.jar
java -jar -Djava.rmi.server.hostname=127.0.1.1 Client/DijkstraClient.jar
By .jar:
    javac MyApp.java
    jar -cf myJar.jar myApp


To run server by: ' make server RegistryIP=127.0.0.1 Ports="9997 9998" '
make runserver REGISTRY_IP=127.0.1.1 PORTS="1132 1133 1134"

To run client by: ' make client Testcase=0 RegistryIP=127.0.0.1 Ports="9997 9998" '
make runclient TESTCASE=0 REGISTRY_IP=127.0.1.1 PORTS="1132 1133 1134"

To clean by: 'make clean'

"# dijkstra2"