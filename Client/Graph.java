package Client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class Graph
{
    private int nodesCount;
    private String[] nodesNames;
    private int[][] weights;
    static final int noConnection = -1;
    final static String separator = ",";

    private Graph(int verticesCount)
    {
        nodesNames = new String[verticesCount];
        weights = new int[verticesCount][verticesCount];
        this.nodesCount = verticesCount;
    }

    public int getNodesCount() {
        return nodesCount;
    }

    int[][] getWeights() {
        return weights;
    }

    String[] getNodesNames() {
        return nodesNames;
    }

    public static Graph fromFile(String filename) throws Exception
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String readVerticesCount = br.readLine();
            //validator of the number of vertices in the adjacency matrix,
            //take only the number, remove other characters

            int verticesCount = Integer.parseInt(readVerticesCount.replaceAll("[^0-9]", ""));
            Graph graph = new Graph(verticesCount);

            for (int i = 0; i < verticesCount; ++i) {
                String line = br.readLine();
                String[] cases = line.split(separator);

                for (int j = 0; j < verticesCount; ++j) {
                    String numstr = cases[j].trim();
                    if (numstr.contains("-"))
                        graph.weights[i][j] = noConnection;
                    else
                        graph.weights[i][j] = Integer.parseInt(numstr);
                }
            }

            for (int i = 0; i < verticesCount; ++i) {
                String nodeName = String.valueOf((char)('A' + i));
                System.out.println("A new node has been added: " + nodeName);
                graph.nodesNames[i] = nodeName;
            }

            return graph;
        }
        catch (Exception e) {
            System.out.println("Error! An exception was thrown ...\n" + e.getMessage());
            throw e;
        }
    }

    void printAdjacencyMatrix()
    {
        System.out.println("\nAdjacencyMatrix:");
        for (int i = 0; i < nodesCount; ++i) {
            for (int j = 0; j < nodesCount; ++j) {
                if (weights[i][j] != noConnection)
                    System.out.print(weights[i][j] + " ");
                else
                    System.out.print("-" + " ");
            }
            System.out.println();
        }
    }
}

