package com.suresh.algo;

import com.suresh.algo.Exceptions.InvalidCapacityException;
import com.suresh.algo.Exceptions.InvalidNodeException;
import com.suresh.algo.Graph.Graph;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*

The following code will be used to test the data structure which I've created to simulate the network graph

Please run the program and executuve the commands and check.

T.S.M.Peiris (w1742124)

2018435

 */

public class MaxFlow {

    private Graph graph;

    public static void main(String[] args) {

        MaxFlow maxFlow = new MaxFlow();
        maxFlow.newGraph();

        int option = 0;
        
        do {
            StdOut.println("\nPlease make a selection...");
            StdOut.println("Press 1 to add new edges");
            StdOut.println("Press 2 to print matrix");
            StdOut.println("Press 3 to print max flow");
            StdOut.println("Press 4 to update an edge");
            StdOut.println("Press 5 to delete an edge");
            option = StdIn.readInt();
            switch (option){
                case 1:
                    maxFlow.addEdges();
                    break;
                case 2:
                    maxFlow.printNetworkGraph();
                    break;
                case 3:
                    try {
                        maxFlow.getMaxFlow();
                    } catch (InvalidNodeException | InvalidCapacityException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                    break;
                case 4:
                    maxFlow.updateEdge();
                    break;
                case 5:
                    maxFlow.deleteEdge();
                    break;
            }
        } while (option > 0 && option < 6);
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
                graph.setCapacity(node1, node2, bandwidth);
            } catch (InvalidNodeException e) {
                e.printStackTrace();
            } catch (InvalidCapacityException e) {
                e.printStackTrace();
            }
        }
    }

    // Delete edge
    public void deleteEdge() {
        StdOut.println("Enter the node 1: ");
        int node1 = StdIn.readInt();

        StdOut.println("Enter the node 2: ");
        int node2 = StdIn.readInt();

        try {
            graph.setCapacity(node1, node2, 0);
            System.out.println("Successfully deleted.");
        } catch (InvalidNodeException e) {
            e.printStackTrace();
        } catch (InvalidCapacityException e) {
            e.printStackTrace();
        }
    }

    // Update edge
    public void updateEdge() {
        StdOut.println("Enter the node 1: ");
        int node1 = StdIn.readInt();

        StdOut.println("Enter the node 2: ");
        int node2 = StdIn.readInt();

        StdOut.println("Enter the updated capacity: ");
        int bandwidth = StdIn.readInt();

        try {
            graph.setCapacity(node1, node2, bandwidth);
            System.out.println("Successfully updated.");
        } catch (InvalidNodeException e) {
            e.printStackTrace();
        } catch (InvalidCapacityException e) {
            e.printStackTrace();
        }
    }


    private List<Integer> tempPath = new ArrayList<>();
    private List<Integer> tempCapacities = new ArrayList<>();

    private void getMaxFlow() throws InvalidNodeException, InvalidCapacityException {
        Stopwatch timer = new Stopwatch();
        int maxFlow = 0;
        System.out.println("Paths:\n==========================================");

        tempPath.clear();
        tempCapacities.clear();
        List<Integer> path = getPath(0);

        do {
            Collections.sort(tempCapacities);
            int lowest = tempCapacities.get(0);
            System.out.println("Path: " + path + ", Flow: " + lowest);
            maxFlow += lowest;
            sendFlow(path, lowest);
            tempPath.clear();
            tempCapacities.clear();
            path = getPath(0);

        } while( path.get(path.size()-1) != -1 );

        System.out.println("\nMax flow is: " + maxFlow+"\n");
        StdOut.println("Elapsed time = " + timer.elapsedTime());
    }

    private void sendFlow(List<Integer> nodes, int flow) throws InvalidNodeException, InvalidCapacityException {
        for(int i=1; i<nodes.size(); i++){
            updateGraph(nodes.get(i-1), nodes.get(i), flow);
        }
    }

    private void updateGraph(int n1, int n2, int capacity) throws InvalidNodeException, InvalidCapacityException {
        //Reduce capacity from forward side
        int edgeCapacityTW = graph.getCapacity(n1, n2);
        graph.setCapacity(n1, n2, (edgeCapacityTW-capacity));

        //Add capacity to back side ;)
        int edgeCapacityBW = graph.getCapacity(n2, n1);
        graph.setCapacity(n2, n1, (edgeCapacityBW+capacity));
    }

    private List<Integer> getPath(int vertex) throws InvalidNodeException, InvalidCapacityException {
        tempPath.add(vertex);
        if(vertex == (graph.getVertices()-1) || vertex == -1){
            return tempPath;
        }
        List<Integer> connections = graph.getPositiveConnections(vertex);
        List<Integer> chosenConnections = new ArrayList<>();
        int highestCapCon = 0;
        int chosenCon = 0;
        for(Integer connection : connections){
            //choose if it doesnt contain in the current path
            if(!tempPath.contains(connection)){
                //choose if it has connections towards positive side
                if(graph.getPositiveConnections(connection).size() != 0 || connection == (graph.getVertices()-1)){
                    chosenConnections.add(connection);
                }
            }
        }
        for(Integer connection : chosenConnections){
            int capacity = graph.getCapacity(vertex, connection);
            if(capacity > highestCapCon){
                highestCapCon = capacity;
                chosenCon = connection;
            }
        }
        tempCapacities.add(graph.getCapacity(vertex, chosenCon));
        if(chosenConnections.size() > 0){
            return getPath(chosenCon);
        } else {
            //The terminator
            return getPath(-1);
        }

    }

    // Print the network graph
    public void printNetworkGraph(){
        graph.printMatrix();
    }

}