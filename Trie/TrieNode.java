

class Indexes {
	
	/**
	 * Index into the word collection array.
	 */
	int wordIndex;
	
	/**
	 * Start index of substring in word.
	 */
	short startIndex;
	
	/**
	 * End index of substring in word.
	 */
	short endIndex;
	
	/**
	 * Initializes this instance with all indexes.

	 */
	public Indexes(int wordIndex, short startIndex, short endIndex) {
		this.wordIndex = wordIndex;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public String toString() {
		return "(" + wordIndex + "," + startIndex + "," + endIndex + ")";
	}
	

	public boolean equals(Object o) {
		if (o == null || !(o instanceof Indexes)) {
			return false;
		}
		Indexes oi = (Indexes)o;
		return wordIndex == oi.wordIndex &&
				startIndex == oi.startIndex &&
				endIndex == oi.endIndex;
	}
}


public class TrieNode {

	/**
	 * Substring held at this node (could be a single character)
	 */
	Indexes substr;
	
	/**
	 * First child of this node
	 */
	TrieNode firstChild;
	
	/**
	 * Sibling of this node
	 */
	TrieNode sibling;
	
	/**
	 * Initializes this trie node with substring, first child, and sibling
	 */
	public TrieNode(Indexes substr, TrieNode firstChild, TrieNode sibling) {
		this.substr = substr;
		this.firstChild = firstChild;
		this.sibling = sibling;
	}

	public String toString() {
		return substr.toString();
	}
	
}
