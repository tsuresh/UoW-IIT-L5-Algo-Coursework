package com.suresh.algo;

import com.suresh.algo.Exceptions.InvalidCapacityException;
import com.suresh.algo.Exceptions.InvalidNodeException;
import com.suresh.algo.Graph.Graph;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.*;

public class MaxFlow {

    private Graph graph;
    private int maxFlow = 0;

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
        float[][] importedGraph = new float[graph.getVertices()][graph.getVertices()];

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

    private void getMaxFlow() throws InvalidNodeException, InvalidCapacityException {
        Stopwatch timer = new Stopwatch();

        System.out.println("\nMax flow is: " + fordFulk(0, graph.getVertices() - 1) + "\n");

        StdOut.println("Elapsed time = " + timer.elapsedTime());
    }

    // Update the graph when a flow is being sent
    private void updateGraph(int n1, int n2, float capacity) throws InvalidNodeException, InvalidCapacityException {
        //Reduce capacity from forward side
        float edgeCapacityTW = graph.getCapacity(n1, n2);
        graph.setCapacity(n1, n2, (edgeCapacityTW-capacity));
        //Add capacity to back side ;)
        float edgeCapacityBW = graph.getCapacity(n2, n1);
        graph.setCapacity(n2, n1, (edgeCapacityBW+capacity));
    }

    // Find child nodes
    private boolean bredthFirst(int source, int sink, int[] adjacents) {
        //Initiate a queue
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[graph.getVertices()];
        //Set visited array to default value : false
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
        //Add initial value to queue
        queue.add(source);
        //Set a adjacent value to a negative value, cuz there's no parent
        adjacents[source] = -1;
        //Mark the node as visited
        visited[source] = true;
        //Search in the path and find child nodes
        while (queue.size() != 0) {
            int u = queue.poll();
            for (int v = 0; v < graph.getVertices(); v++) {
                if (!visited[v] && graph.getGraph()[u][v] > 0) {
                    queue.add(v);
                    adjacents[v] = u;
                    visited[v] = true;
                }
            }
        }
        return (visited[sink]);
    }

    private int fordFulk(int source, int sink) throws InvalidNodeException, InvalidCapacityException {
        int[] adjacent = new int[graph.getVertices()];
        maxFlow = 0;
        while (bredthFirst(source, sink, adjacent)) {
            float minPathCapacity = Float.MAX_VALUE;
            //Get the minimum capacity of a path
            for (int i = sink; i != source; i = adjacent[i]) {
                int adjIndex = adjacent[i];
                minPathCapacity = Math.min(minPathCapacity, graph.getCapacity(adjIndex, i));
            }
            //Update the graph for each path iteration
            List<Integer> path = new ArrayList<>();
            for (int i = sink; i != source; i = adjacent[i]) {
                int adjIndex = adjacent[i];
                path.add(i);
                updateGraph(adjIndex, i, minPathCapacity);
            }

            //Print path for each iteration [Consumes memory]
            //Remove in order to make more efficient
            path.add(0);
            Collections.reverse(path);
            System.out.println(path + ", Capacity flow: " + minPathCapacity);

            //Increase the max flow variable
            maxFlow += minPathCapacity;
        }
        return maxFlow;
    }

    // Print the network graph
    public void printNetworkGraph(){
        graph.printMatrix();
    }

}