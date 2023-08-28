package combat;

import java.util.ArrayList;

public class Edge<T> {

    Vertex<T> a, b;

    public Edge(T a, T b) {
        this.a = new Vertex<>(a);
        this.b = new Vertex<>(b);
    }

}
