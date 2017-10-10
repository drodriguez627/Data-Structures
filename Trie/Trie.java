
import java.util.ArrayList;

 // This class implements a compressed trie. Each node of the tree is a CompressedTrieNode, with fields for
 // indexes, first child and sibling.

public class Trie {
	
	/**
	 * Words indexed by this trie.
	 */
	ArrayList<String> words;
	
	/**
	 * Root node of this trie.
	 */
	TrieNode root;
	
	/**
	 * Initializes a compressed trie with words to be indexed, and root node set to
	 * null fields.
	 */
	public Trie() {
		root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
	}
	
	/**
	 * Inserts a word into this trie. Converts to lower case before adding.
	 * The word is first added to the words array list, then inserted into the trie.
	 */
	public void insertWord(String word) {

		
		word = word.toLowerCase();
		words.add(word);
		TrieNode current = root;
		String originalWord = word;

	
		
		//if the Trie is empty, makes word equal to first node
		if(root.firstChild == null) {
		
			TrieNode node = new TrieNode(new Indexes(0,(short)0, (short)0), null, null);
			root.firstChild = node;
			return;
		}
		while(current != null) {
			String currentString = makeString(current);
			int index = numberOfMatches(currentString, word);
		
			if(isMatch(current)) {
			
				String pathString = makeString(current);
		
		
			if(word.indexOf(pathString) != -1) {
				current.substr.startIndex = (short) ((short) pathString.length() -1);
				current = current.firstChild;
				continue;
		}
		
		if(index != -1) {
			
			String currentSub = makeString(current);
		
		 if(index < currentSub.length()) {
			TrieNode tmp = current;
			String tmpString = makeString(tmp);
			current.substr.startIndex = 0;
			current.substr.endIndex = (short) numberOfMatches(currentString, tmpString);
			current.firstChild = tmp;
			current.sibling = null;
			
			tmp.substr.startIndex = (short) ((short) index +1);
			tmp.substr.endIndex = (short) word.length();
			word = word.substring(0, numberOfMatches(word, currentString));
			current = current.firstChild;
			continue;
		 }
		}
		

		//no common letters
		if(index == -1) {
			if(current.sibling != null) {
				while(current.sibling != null) {
					current = current.sibling;
				}
				current.sibling = new TrieNode(new Indexes(words.size(),(short) numberOfMatches(originalWord, word),(short) originalWord.length()), null, null);
				return;
			}
			
		}
		}
		}
	}

	// returns number of characters that are similar in two Trie Nodes
	private int numberOfMatches(String current, String word)
	{
		int num = 0;

		for(int i = 0; i < current.length(); i++) {
			for(int j = 0; j < word.length(); j++) {
				if(current.charAt(i) == (word.charAt(j))) {
					num++;
				}
				else if(current.charAt(i) != (word.charAt(j))) {
					break;
				}
			}
		}

		return num;
	}
	//produces a string from the indices of a Trie Node
	private String makeString(TrieNode word) {

		return words.get(word.substr.wordIndex).substring(word.substr.startIndex, word.substr.endIndex);
	}
	private boolean isMatch(TrieNode word) {
		String subWord = makeString(word);
		String childString = makeString(root.firstChild);
		if(subWord.charAt(0) !=  childString.charAt(0)) {
			if(root.firstChild.sibling == null) {
			return false;
			}
			else if(root.firstChild.sibling != null) {
				String SiblingString = makeString(root.firstChild.sibling);
				if(subWord.charAt(0) !=  SiblingString.charAt(0)) {
					return false;
				}
				else return true;
				
			}
		}
		return true;
	}
	

	public ArrayList<String> completionList(String prefix) {
		/** COMPLETE THIS METHOD **/
		String result = "";
		TrieNode current = root.firstChild;
		ArrayList<String> completionList = new ArrayList<String>();
		
		
		if(root.firstChild == null) {
			return null;
		}
		
		while(current != null) {
			String currentString = makeString(current);
			
			if (currentString.startsWith(prefix)) {
				result += currentString;
				current = current.firstChild;
				
				if(current != null) {
					current = current.firstChild.sibling;
					result += currentString;
				}
				else completionList.add(result);
			}
			else if(current.sibling != null) {
				while(current.sibling != null) {
				
				String sibString = makeString(current.sibling);
				if(sibString.startsWith(prefix)) {
					result += sibString;
					current = current.sibling.firstChild;
					if(current != null) {
						current = current.sibling.firstChild;
						result += sibString;
					}
					else{
						completionList.add(result);
					}
					
				}
				else{
					completionList.add(result);
				}
									
				}
			}
		}
		

		return completionList;
	}
	
	
	public void print() {
		print(root, 1, words);
	}
	
	private static void print(TrieNode root, int indent, ArrayList<String> words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			System.out.println("      " + words.get(root.substr.wordIndex));
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }