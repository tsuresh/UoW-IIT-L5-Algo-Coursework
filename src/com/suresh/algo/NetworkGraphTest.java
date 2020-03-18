package com.suresh.algo;

import com.suresh.algo.Exceptions.InvalidCapacityException;
import com.suresh.algo.Exceptions.InvalidNodeException;
import com.suresh.algo.Graph.Graph;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;


/*

The following code will be used to test the data structure which I've created to simulate the network graph

Please run the program and executuve the commands and check.

T.S.M.Peiris (w1742124)

2018435

 */

public class NetworkGraphTest {

    private Graph graph;

    public static void main(String[] args) {

        NetworkGraphTest networkGraphTest = new NetworkGraphTest();

        // Initialize new graph
        networkGraphTest.newGraph();
        
        int option = 0;
        
        do {
            StdOut.println("Please make a selection...");
            StdOut.println("Press 1 to add new edges");
            StdOut.println("Press 2 to get a list of connections");
            StdOut.println("Press 3 to get capacities between two nodes");
            StdOut.println("Press 4 to print matrix");
            option = StdIn.readInt();
            switch (option){
                case 1:
                    networkGraphTest.addEdges();
                    break;
                case 2:
                    networkGraphTest.getConnections();
                    break;
                case 3:
                    networkGraphTest.getCapacities();
                    break;
                case 4:
                    networkGraphTest.printNetworkGraph();
                    break;
            }
        } while(option > 0 && option < 5);
    }

    // New network graph
    public void newGraph(){
        StdOut.println("Enter the number of vertices: ");
        int vertices = StdIn.readInt();
        graph = new Graph(vertices);
    }

    // Add edges
    public void addEdges(){
        StdOut.println("Enter the number of edges to add: ");
        int edges = StdIn.readInt();
        for(int i=0; i<edges; i++){
            StdOut.println("Enter the node 1: ");
            int node1 = StdIn.readInt();

            StdOut.println("Enter the node 2: ");
            int node2 = StdIn.readInt();

            StdOut.println("Enter the bandwidth: ");
            int bandwidth = StdIn.readInt();

            try {
                graph.addEdge(node1, node2, bandwidth);
            } catch (InvalidNodeException e) {
                e.printStackTrace();
            } catch (InvalidCapacityException e) {
                e.printStackTrace();
            }
        }
    }

    // Get all eonnections for a given node
    public void getConnections(){
        StdOut.println("Please enter the node value to get its connections: ");
        int node = StdIn.readInt();
        try {
            for(int i : graph.getConnections(node)){
                StdOut.println(i);
            }
        } catch (InvalidNodeException e) {
            e.printStackTrace();
        }

    }

    // Get the bandwidth capacity between two nodes
    public void getCapacities(){
        StdOut.println("Enter the node 1: ");
        int node1 = StdIn.readInt();
        StdOut.println("Enter the node 2: ");
        int node2 = StdIn.readInt();
        try {
            StdOut.println(graph.getCapacity(node1, node2));
        } catch (InvalidNodeException e) {
            e.printStackTrace();
        }
    }

    // Print the network graph
    public void printNetworkGraph(){
        graph.printMatrix();
    }


}