# Dijkstra algorithm with Java  RMI

all: compile
        @echo "Build completed"

compile: Client/*.java Server/*.java
        javac Client/*.java
        javac Server/*.java

runserver:
        java Server.Server ${RegistryIP} ${Ports}

runclient:
        java Client.Client ${Testcase} ${RegistryIP} ${Ports}

clean:
        rm Client/*.class
        rm Server/*.class
        rm Shared/*.class
