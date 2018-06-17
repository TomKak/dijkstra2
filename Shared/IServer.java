package Shared;

import java.rmi.*;

public interface IServer extends Remote {
    public void initialData(int workerId, int nodesCount, int[] ranges, int[][] weights) throws RemoteException;
    public int[] getWorkerPrevNodesPart() throws RemoteException;
    public int[] calculateDistances(Integer currentNode, int distToCurrentNode) throws RemoteException;
}

