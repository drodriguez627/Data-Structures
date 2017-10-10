

public class PartialTree {
  
	/**
	 * Inner class - represents an edge in a weighted, undirected graph. 
	 */
	public static class Arc implements Comparable<Arc> {
	    
		/**
	     * A vertex at one end of this arc.
	     */
	    Vertex v1;

	    /**
	     * A vertex at one end of this arc.
	     */
	    Vertex v2;

	    /**
	     * Weight assigned to this arc.
	     */
	    int weight;

	    /**
	     * Constructs a new Arc object from an existing edge in a graph.
	     */
	    public Arc(Vertex v1, Vertex v2, int weight) {
	    	this.v1 = v1;
	    	this.v2 = v2;
	    	this.weight = weight;
	    }

	    public boolean equals(Object o) {
	    	if (o == null || !(o instanceof Arc)) {
	    		return false;
	    	}
	    	Arc other = (Arc)o;
	    	return weight == other.weight && 
	    			((v1.name.equals(other.v1.name) && v2.name.equals(other.v2.name)) ||
	    			 (v1.name.equals(other.v2.name) && v2.name.equals(other.v1.name)));
	    }
	    
	    /**
	     * Compares the weight of this arc with that of another.
	     */
	    public int compareTo(Arc other) {
	    	return weight - other.weight;
	    }

	    public String toString() {
	    	return "(" + v1 + " " + v2 + " " + weight + ")";
	    }
	}
	
	/**
	 * The root of the partial tree
	 */
	private Vertex root;
    
	/**
	 * The arcs included in this partial tree
	 */
	private MinHeap<Arc> arcs;

	/**
	 * Initializes this partial tree with given vertex
	 */
    public PartialTree(Vertex vertex) {
    	root = vertex;
    	arcs = new MinHeap<Arc>();
    }

    /**
     * Merges another partial tree into this partial tree
     */
    public void merge(PartialTree other)	{
    	other.root.parent = root;
    	arcs.merge(other.arcs);
    }
    
    /**
     * Returns the root of this tree.
     */
    public Vertex getRoot() {
    	return root;
    }
    
    /**
     * Returns the priority-ordered arc set of this tree. The lower the weight of an arc,
     * the higher its priority.
     */
    public MinHeap<Arc> getArcs() {
    	return arcs;
    }

    public String toString() {
    	String ret = "Vertices: " + root.toString();
    	ret += "  PQ: " + arcs;
    	return ret;
    }
}
