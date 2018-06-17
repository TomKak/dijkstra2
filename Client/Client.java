package Client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.*;
import java.util.concurrent.*;

import Shared.*;

public class Client
{
    public static void main( String args[] ) throws Exception
    {
        System.out.println("Client started... \n " +
                "The following number of arguments have been entered: " + args.length);

        //In the same way as in the example from lesson 11. Java RMI / JNI

        if (args.length < 3)
        {
            System.out.println("You should enter the following arguments:\n" +
                    " 'testcaseIssue' & 'hostName' &  more then one 'serverPort'\n" +
                    "For example: make runclient Testcase=4 RegistryIP=127.0.0.1 Ports=\"9997 9998\" \n");
            return;
        }

        String testcaseIssue = args[0];
        String hostName = args[1];
        String[] ports = new String[args.length - 2];//all arguments minus testcaseIssue & hostName

        for(int i=2; i<args.length; ++i)
            ports[i-2] = args[i];

        try
        {
            System.out.println("Loading the graph...");
            Graph myCase = Graph.fromFile("testcases/" + testcaseIssue);//handle a test case from a file
            myCase.printAdjacencyMatrix();

            System.out.println("We're just starting the Dijkstra algorithm...\n");
            new Dijkstra(myCase, hostName, ports).run();

            System.out.println("End of main function in class 'Client'.");
        }
        catch(Exception e)
        {
            System.out.println("Error! An exception was thrown ...\n");
            e.printStackTrace();
        }
    }
}
