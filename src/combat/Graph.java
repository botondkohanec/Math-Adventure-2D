package combat;

import java.util.ArrayList;

public class Graph<T> {

    int numberOfVertices;
    ArrayList<Vertex> listOfVertices;
    int numberOfEdges;
    ArrayList<Edge> listOfEdges;

    Graph() {
        listOfVertices = new ArrayList<>();
        listOfEdges = new ArrayList<>();
    }

    public void addVertex(T a, int x, int y) {
        listOfVertices.add(new Vertex(a, x, y));
        numberOfVertices++;
    }

    public void addEdge(Vertex a, Vertex b) {
        Edge newEdge1 = new Edge(a, b);
        Edge newEdge2 = new Edge(b, a);
        if( !containsVertex(a) || !containsVertex(b) || (a.value.equals(b.value)) || !containsEdge(newEdge1)) {
            System.err.println("Wrong edge");
        } else {
            listOfEdges.add(newEdge1);
            numberOfEdges++;
        }
    }

    public boolean containsVertex(Vertex vertex) {
        for(int i = 0; i < listOfVertices.size(); i++) {
            if(vertex.value.equals(listOfVertices.get(i).value)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsEdge(Edge edge) {
        for(int i = 0; i < listOfEdges.size(); i++) {
            if( (edge.a.value.equals(listOfEdges.get(i).a.value) &&
                    edge.b.value.equals(listOfEdges.get(i).b.value)) ||
                    (edge.a.value.equals(listOfEdges.get(i).b.value) &&
                            edge.b.value.equals(listOfEdges.get(i).a.value))) {
                return true;
            }
        }
        return false;
    }


    public class Edge {

        Vertex a, b;

        public Edge(Vertex a, Vertex b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "(" + a + ", " + b + ")";
        }

    }

    public class Vertex {
        T value;
        int x = 0;
        int y = 0;

        public Vertex(T value, int x, int y) {
            this.value = value;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "value = " + value + ", x = " + x + ", y = " + y;
        }
    }
}
