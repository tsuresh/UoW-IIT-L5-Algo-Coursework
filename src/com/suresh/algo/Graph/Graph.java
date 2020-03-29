package com.suresh.algo.Graph;

import com.suresh.algo.Exceptions.InvalidCapacityException;
import com.suresh.algo.Exceptions.InvalidNodeException;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private int edges;
    private int vertices;
    private int[][] graph;

    // Generate graph (NxM)
    public Graph(int vertices) {
        this.graph = new int[vertices][vertices];
        this.vertices = vertices;
        /*
        STATIC TEST DATA
        this.vertices = 6;
        this.graph = new int[][]{
                {0, 10, 8, 0, 0, 0 },
                {0, 0, 5, 5, 0, 0},
                {0, 4, 0, 0, 10, 0},
                {0, 0, 7, 0, 6, 3},
                {0, 0, 0, 10, 0, 14},
                {0, 0, 0, 0, 0, 0}
        };
         */
    }

    // Print the graph (For debug purposes)
    public void printMatrix(){
        for(int i=0; i<graph.length; i++){
            for(int x=0; x<graph[i].length; x++){
                System.out.print( graph[i][x] + " " );
            }
            System.out.println();
        }
    }

    // Add new edge
    // N1 = Node 1 value
    // N2 = Node 2 value
    // bW = Capacity
    public void setCapacity(int n1, int n2, int bW) throws InvalidNodeException, InvalidCapacityException {
        validateNodes(n1, n2);
        if(bW < 0){
            throw new InvalidCapacityException("Your capacity value has to be greater than 0");
        }
        graph[n1][n2] = bW; //assumung there are two transmission rates for either sides
        this.edges++;
    }

    // Get the list of connected nodes for a given node
    public List<Integer> getConnections(int node) throws InvalidNodeException {
        List<Integer> nodes = new ArrayList<>();
        if(node >= vertices || node < 0){
            throw new InvalidNodeException("Your node value has to be within the range of the number of vertices");
        }
        for(int i=0; i<graph[node].length; i++){
            if(graph[node][i] > 0){
                nodes.add(i);
            }
        }
        return nodes;
    }

    // Get the list of positive connected nodes for a given node
    public List<Integer> getPositiveConnections(int node) throws InvalidNodeException {
        List<Integer> nodes = new ArrayList<>();
        if (node >= vertices || node < 0) {
            throw new InvalidNodeException("Your node value has to be within the range of the number of vertices");
        }
        for (int i = 0; i < graph[node].length; i++) {
            if (graph[node][i] > 0 && i > node) {
                nodes.add(i);
            }
        }
        return nodes;
    }

    // Get the capacity between two nodes
    public int getCapacity(int n1, int n2) throws InvalidNodeException {
        validateNodes(n1, n2);
        return graph[n1][n2];
    }

    // Get the number of vertices
    public int getVertices() {
        return vertices;
    }

    // Get the number of edges
    public int getEdges() {
        return edges;
    }

    // Validate user input for node values and throw exceptions
    public void validateNodes(int n1, int n2) throws InvalidNodeException {
        if(n1 >= vertices || n1 < 0){
            throw new InvalidNodeException("Your node value has to be within the range of the number of vertices");
        }
        if(n2 >= vertices || n2 < 0){
            throw new InvalidNodeException("Your node value has to be within the range of the number of vertices");
        }
    }

}
