package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.HashSet;
import Shared.*;

public class Server extends UnicastRemoteObject implements IServer{
    public Server() throws RemoteException {
        super();
    }


    public static void main(String args[]) throws Exception {
        System.out.println("Server started...");
        if (args.length < 2) {
            System.out.println("You should enter the following arguments:\n" +
                    "'hostName' &  more then one 'serverPort'\n" +
                    "For example: make runserver RegistryIP=127.0.0.1 Ports=\"0001 0002\"\n");
            return;
        }

        String hostName = args[0];

        for(int i=1; i<args.length; ++i) {
            try {
                Server server = new Server();

                String port = args[i];
                System.setProperty("java.rmi.server.hostname", hostName);
                Registry reg = LocateRegistry.createRegistry(Integer.parseInt(port));
                reg.rebind("server", server);//"Server", server
                System.out.println("Server started on " + hostName + ":" + port);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("End of main function in class 'Server'.");
    }

    private int[][] weights;
    private int fromNode;
    private int toNode;
    private int workerId;
    private int nodesCount;
    private int[] distances;
    private int[] prevNodes;
    private HashSet<Integer> visitedNodes;
    
    public void initialData(int workerId, int nodesCount, int[] ranges, int[][] weights) throws RemoteException {
        this.weights = weights;
        this.workerId = workerId;
        this.fromNode = ranges[0];
        this.toNode = ranges[1];
        this.nodesCount = nodesCount;
        this.visitedNodes = new HashSet<>();
        this.distances = new int[nodesCount];
        this.prevNodes = new int[nodesCount];
        
        for(int i=0; i<nodesCount; ++i)
            this.distances[i] = this.prevNodes[i] = Integer.MAX_VALUE;
    }
    
    public int[] calculateDistances(Integer currentNode, int distToCurrentNode) throws RemoteException {
        System.out.println("The beginning of the 'calculateDistances' method");

        distances[currentNode] = distToCurrentNode;
        
        for(int node=this.fromNode; node<=this.toNode; ++node) {
            if (visitedNodes.contains(node)) {
                System.out.println("Worker '" + this.workerId + "'\n The node: '" + node + "'  was already visited.");
                continue;
            }
            
            if (connectionNodesExists(currentNode, node)) {
                int nodeDistance = this.weights[currentNode][node];
                int totalDistToNode = distances[currentNode] + nodeDistance;
                
                System.out.println("Case:\n " +
                        "Worker '" + workerId + "', node '" + currentNode + "' is connected to " + node + "\n" +
                        " distance = " + nodeDistance + " & totalDistToNode = " + totalDistToNode + ".");
                if (totalDistToNode < distances[node]) {
                    distances[node] = totalDistToNode;
                    prevNodes[node] = currentNode;
                    System.out.println("The total cost of distance to this vertex is smaller. We replace the value.");
                }
            }
        }
        
        visitedNodes.add(currentNode);
        System.out.println("End of the 'calculateDistances' method");
        return this.getWorkerDistancesPart();
    }

    public int[] getWorkerPrevNodesPart() throws RemoteException {
        return this.getWorkerMatrixPart(this.prevNodes);
    }

    private boolean connectionNodesExists(int fromNode, int toNode) {
        return this.weights[fromNode][toNode] != -1;
    }

    private int[] getWorkerDistancesPart() {
        return this.getWorkerMatrixPart(this.distances);
    }

    private int[] getWorkerMatrixPart(int[] array) {
        int[] result = new int[this.toNode - this.fromNode + 1];
        for(int i=this.fromNode; i<=this.toNode; ++i)
            result[i-this.fromNode] = array[i];
        return result;
    }
}
