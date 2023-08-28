package combat;

import java.util.ArrayList;

public class Graph<T> {

    int numberOfVertices;
    ArrayList<Vertex<T>> listOfVertices;
    int numberOfEdges;
    ArrayList<Edge<Vertex<T>>> listOfEdges;

    Graph() {
        listOfVertices = new ArrayList<>();
    }

    public void addVertex(T a) {
        listOfVertices.add(new Vertex<T>(a));
        numberOfVertices++;
    }

    public void addEdge(int a, int b) {
        Edge<Vertex<T>> newEdge1 = new Edge<>(listOfVertices.get(a), listOfVertices.get(b));
        Edge<Vertex<T>> newEdge2 = new Edge<>(listOfVertices.get(b), listOfVertices.get(a));
        if(a > listOfVertices.size() || b > listOfVertices.size() ||
                listOfEdges.contains(newEdge1) || listOfEdges.contains(newEdge2) ||
        newEdge1.equals(newEdge2)) {
            System.err.println("Wrong edge");
        } else {
            listOfEdges.add(newEdge1);
            numberOfEdges++;
        }
    }

}
