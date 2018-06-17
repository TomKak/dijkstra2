# Dijkstra algorithm with Java RMI

all: compile
        @echo "Build completed"

compile: Client/*.java Server/*.java
        javac Client/*.java
        javac Server/*.java

server:
        java Server.Server ${RegistryIP} ${Ports}

client:
        java Client.Client ${Testcase} ${RegistryIP} ${Ports}

clean:
        @echo 'clearing files...'
        rm Client/*.class
        rm Server/*.class
        rm Shared/*.class



