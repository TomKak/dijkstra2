# Dijkstra algorithm with Java RMI

all: compile
        @echo "Build completed"

compile: Client/*.java Server/*.java
        javac Client/*.java
        javac Server/*.java

server:
        #java -jar -Djava.rmi.server.hostname=${REGISTRY_IP} Server.Server/jar
        java Server.Server ${RegistryIP} ${Ports}

client:
        #java -jar -Djava.rmi.server.hostname=127.0.1.1 Client/DijkstraClient.jar
        java Client.Client ${Testcase} ${RegistryIP} ${Ports}

clean:
        @echo 'clearing files...'
        rm Client/*.class
        rm Server/*.class
        rm Shared/*.class



