package com.suresh.algo;

import com.suresh.algo.Exceptions.InvalidCapacityException;
import com.suresh.algo.Exceptions.InvalidNodeException;
import com.suresh.algo.Graph.Graph;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            StdOut.println("Press 6 import data set");
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
                case 6:
                    maxFlow.importDataset(args);
                    break;
            }
        } while (option > 0 && option < 7);
    }


    public void importDataset(String args[]) {
        In in = new In(args[0]);
        int[][] importedGraph = new int[graph.getVertices()][graph.getVertices()];

        String[] line = in.readAllLines();

        for (int i = 0; i < importedGraph.length; i++) {
            for (int x = 0; x < importedGraph.length; x++) {
                importedGraph[i][x] = Integer.valueOf(line[i].split(" ")[x]);
            }
        }
        graph.setGraph(importedGraph);
        System.out.println("Successfully imported!");

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

    // Send a data flow across the graph
    private void sendFlow(List<Integer> nodes, int flow) throws InvalidNodeException, InvalidCapacityException {
        for(int i = 1; i<nodes.size(); i++){
            updateGraph(nodes.get(i-1), nodes.get(i), flow);
        }
    }

    // Update the graph when a flow is being sent
    private void updateGraph(int n1, int n2, int capacity) throws InvalidNodeException, InvalidCapacityException {
        //Reduce capacity from forward side
        int edgeCapacityTW = graph.getCapacity(n1, n2);
        graph.setCapacity(n1, n2, (edgeCapacityTW-capacity));

        //Add capacity to back side ;)
        int edgeCapacityBW = graph.getCapacity(n2, n1);
        graph.setCapacity(n2, n1, (edgeCapacityBW+capacity));
    }

    // Get path from source to sink
    private List<Integer> getPath(int vertex) throws InvalidNodeException, InvalidCapacityException {
        tempPath.add(vertex);
        if(vertex == (graph.getVertices()-1) || vertex == -1){
            // Return the path
            return tempPath;
        }
        List<Integer> connections = graph.getPositiveConnections(vertex);
        List<Integer> chosenConnections = new ArrayList<>();
        int highestCapCon = 0;
        int chosenCon = 0;
        for(Integer connection : connections){
            // 1. Choose if it doesnt contain in the current path
            if(!tempPath.contains(connection)){
                // 2. choose if it has connections towards positive side
                if(graph.getPositiveConnections(connection).size() != 0 || connection == (graph.getVertices()-1)){
                    chosenConnections.add(connection);
                }
            }
        }
        // Having the filtered node in chosenConnections List
        for(Integer connection : chosenConnections){
            int capacity = graph.getCapacity(vertex, connection);
            // Choose the edge with highest capacity (Greedy approach)
            if(capacity > highestCapCon){
                highestCapCon = capacity;
                chosenCon = connection;
            }
        }
        // Adding the capacities to temporary array
        tempCapacities.add(graph.getCapacity(vertex, chosenCon));
        if(chosenConnections.size() > 0){
            // If the array contains children call the method recursively
            return getPath(chosenCon);
        } else {
            // If it doesnt contain terminate the method
            return getPath(-1);
        }

    }

    // Print the network graph
    public void printNetworkGraph(){
        graph.printMatrix();
    }

}