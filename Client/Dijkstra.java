package Client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;
import java.util.*;
import java.rmi.*;

import Shared.*;

public class Dijkstra {
    public Dijkstra(Graph myTestCase, String hostName, String[] ports) throws Exception {
        System.out.println("The beginning of the 'Dijkstra' constructor.");
        serversCount = ports.length;
        workerServers = new IServer[serversCount];
        workerNodesCount = new int[serversCount];
        workerFromNodes = new int[serversCount];
        nodesVisited = new HashSet<>();
        
        this.myTestcase = myTestCase;
        
        for(int i = 0; i< serversCount; ++i) {
           
            //System.setProperty("java.rmi.server.hostname", hostName);

            Registry reg = LocateRegistry.getRegistry(hostName, Integer.parseInt(ports[i]));
            //Registry reg = LocateRegistry.getRegistry(Integer.parseInt(serverPorts[i]));
           
            System.out.println("dubug 1");
            workerServers[i] = (IServer) reg.lookup("server");//TODO: this such exception
            //local host 127.0.0.1
            //taurus host 127.0.1.1
            System.out.println("dubug 2");
        }
        executor = Executors.newFixedThreadPool(serversCount);
        System.out.println("End of the 'Dijkstra' constructor.");
    }
    
    private Graph myTestcase;
    private int serversCount;
    private ExecutorService executor;
    private IServer[] workerServers;
    private int[] workerNodesCount;
    private int[] workerFromNodes;
    private HashSet<Integer> nodesVisited;
    
    public void run() throws InterruptedException, RemoteException {
        System.out.println("The beginning of the 'run' method.");
        final int[][] weights = myTestcase.getWeights();
        //String[] nodesNames = myTestcase.getNodesNames();
        int nodesCount = myTestcase.getNodesCount();
        
        int[] distances = new int[nodesCount];
        int[] prevNodes = new int[nodesCount];
        
        for(int i=0; i<nodesCount; ++i)
            distances[i] = prevNodes[i] = Integer.MAX_VALUE;
        
        int initialNode = 0; // TODO
        PriorityQueue<Integer> nodesToVisitQ = new PriorityQueue<>();
        nodesToVisitQ.add(initialNode);

        System.out.println("Sending weights to workers...");
        List<Callable<Object>> calls = new ArrayList<>();
        for(int i = 0; i< serversCount; ++i) {
            final int workerId = i;
            calls.add(Executors.callable(() -> {
                System.out.println("Sending weights to worker " + workerId);
                try {
                    int[] nodeRanges = calculateWorkerNodeRanges(workerId);
                    int fromNode = nodeRanges[0];
                    int toNode = nodeRanges[1];
                    workerNodesCount[workerId] = toNode - fromNode + 1;
                    workerFromNodes[workerId] = fromNode;
                    workerServers[workerId].initialData(workerId, nodesCount, nodeRanges, weights);
                }
                catch(RemoteException e) {
                    e.printStackTrace();
                }
            }));
        }
        executor.invokeAll(calls);

        distances[initialNode] = 0;
        nodesVisited.add(initialNode);
        
        while(nodesToVisitQ.size() != 0) {
            Integer currentNode = nodesToVisitQ.poll();
            System.out.println("The node '" + currentNode + "' is in the path.");
            
            calls = new ArrayList<>();
            for(int i = 0; i< serversCount; ++i) {
                final int workerId = i;
                calls.add(Executors.callable(() -> {
                    System.out.println("Sending weight values to worker '" + workerId+"'.");
                    try {
                        int[] workerDistances = workerServers[workerId].calculateDistances(currentNode, distances[currentNode]);
                        System.arraycopy(workerDistances, 0, distances, workerFromNodes[workerId], workerNodesCount[workerId]);
                    }
                    catch(RemoteException e) {
                        e.printStackTrace();
                    }
                }));
            }
            executor.invokeAll(calls);
            
            for(int node=0; node<nodesCount; ++node)
                if (nodesVisited.contains(node) == false && connectionNodesExists(currentNode, node)) {
                    nodesToVisitQ.add(node);
                    nodesVisited.add(node);
                }
        }
        
        calls = new ArrayList<>();
        for(int i = 0; i< serversCount; ++i) {
            final int workerId = i;
            calls.add(Executors.callable(() -> {
                try {
                    int[] workerPrevNodes = workerServers[workerId].getWorkerPrevNodesPart();
                    System.out.println(workerId + ", fromNode=" + workerFromNodes[workerId] + ", count=" + workerNodesCount[workerId]);
                    System.arraycopy(workerPrevNodes, 0, prevNodes, workerFromNodes[workerId], workerNodesCount[workerId]);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        executor.invokeAll(calls);

        System.out.println("The result of the dijkstr algorithm implementation:\n");
        System.out.println("Started from node index = " + initialNode);
        System.out.print("Distances = [");
        for(int node=0; node<nodesCount; ++node) {
            if (distances[node] == Integer.MAX_VALUE)
                System.out.print("-, ");
            else
                System.out.print(distances[node] + ", ");
        }
        System.out.println("\b\b]");
        
        System.out.print("PrevNodes (X means initialNode) = [");
        for(int node=0; node<nodesCount; ++node) {
            if (node == initialNode)
                System.out.print("X, ");
            else
                System.out.print(prevNodes[node] + ", ");
        }
        System.out.println("\b\b]");
        
        executor.shutdown();
        System.out.println("End of the 'run' Dijkstra method.");
    }
    
    private boolean connectionNodesExists(int fromNode, int toNode) {
        return this.myTestcase.getWeights()[fromNode][toNode] != -1;
    }
    private int[] calculateWorkerNodeRanges(int workerServerId) {
        System.out.println("The beginning of the 'calculateWorkerNodeRanges' method.");
        int nodesCount = myTestcase.getNodesCount();
        int[] results = new int[2];
        
        int fromNode = (nodesCount / serversCount) * workerServerId;
        int toNode = (nodesCount / serversCount) * (workerServerId + 1) - 1;
        
        int restNodes = nodesCount % serversCount;
        
        if (workerServerId < restNodes) {
            fromNode += workerServerId;
            toNode += workerServerId + 1;
        }
        else {
            fromNode += restNodes;
            toNode += restNodes;
        }
        
        results[0] = fromNode;
        results[1] = toNode;
        
        return results;
    }
}
