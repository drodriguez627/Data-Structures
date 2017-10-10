
import java.util.ArrayList;



public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 */
	public static PartialTreeList initialize(Graph graph) {
		if(graph == null) {
			return null;
		}
	
		PartialTreeList list = new PartialTreeList();
		
		for(int i = 0; i < graph.vertices.length; i++) {
			Vertex vertex = graph.vertices[i];
			//initialize new partial tree
			PartialTree tree = new PartialTree(graph.vertices[i]);
			//initialize heap of tree
			MinHeap<PartialTree.Arc> arcs = new MinHeap<PartialTree.Arc>();
			//create new neighbors of the neighbors of the vertex we are adding 
			 Vertex.Neighbor neighbors = graph.vertices[i].neighbors;
			 //for every neighbor of the vertex, create an arc from the vertex to the neighbor
			 while(neighbors != null) {
				 PartialTree.Arc arc = new PartialTree.Arc(vertex, neighbors.vertex,neighbors.weight);
				 arcs.insert(arc);
				 neighbors = neighbors.next;
			 }
			 //add the heap to the tree, and the tree to the partialTreeList
			 tree.getArcs().merge(arcs);
			list.append(tree);
			
		}
		
		return list;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		ArrayList<PartialTree.Arc> mst = new ArrayList<PartialTree.Arc>();
		if(ptlist == null) {
			return null;
		}
		
		do {
		PartialTree tree = ptlist.remove();
		tree.getArcs().siftDown(0);
		PartialTree.Arc arc = tree.getArcs().deleteMin();
		Vertex v2 = arc.v2;
		
		if(v2.getRoot() == tree.getRoot()) {
			while(v2.getRoot() == tree.getRoot()) {
				arc = tree.getArcs().deleteMin();
				v2 = arc.v2;
			}
		}
		mst.add(arc);
		PartialTree temp = ptlist.removeTreeContaining(v2);
		if(temp == null) {
			break;
		}
		tree.merge(temp);
		ptlist.append(tree);
		
		
		}
		while(ptlist != null);

		return mst;
	}
}