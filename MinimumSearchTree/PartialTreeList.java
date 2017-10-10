import java.util.Iterator;
import java.util.NoSuchElementException;



public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
     * Removes the tree that is at the front of the list.
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    	
    	Node ptr = this.rear.next;
    	
    	//throws error if partial tree is empty
    	if(this.rear == null) {
    		throw new NoSuchElementException();
    	}
    	
    	if(size == 1) {
    		size--;
    		return rear.tree;
    	}
    	else{
    	rear.next = rear.next.next;
    	ptr.next = null;
    	size--;
    	return ptr.tree;
    	
    	}

    }

    /**
     * Removes the tree in this list that contains a given vertex.
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    	
    	
    	
    	while(this.iterator().hasNext()) {
    		for(PartialTree i : this) {
    		//for(PartialTree i = this.rear.tree; i != rear.tree; i = i.rear.next.tree  ) {
    			if(i.getRoot() == vertex.getRoot()) {
    				PartialTree tree = i;
    				this.remove();
    				return tree;
    			}
    			this.append(this.remove());
    		}
    	}
    	throw new NoSuchElementException();

     }
    
    /*private boolean lookFor(PartialTree tree, Vertex vertex) {
    	
    	Vertex ptr = vertex;
    	
    	
    	if(vertex.parent ==null) {
    		if
    	}
    } */
    /**
     * Gives the number of trees in this list
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}
