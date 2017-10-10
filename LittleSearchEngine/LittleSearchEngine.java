
import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		
		HashMap<String, Occurrence> keyWords = new HashMap<String,Occurrence>();
			
		Scanner sc= new Scanner(new FileReader(docFile));
		while(sc.hasNext()) {
			String doc = sc.next();
			String word = getKeyWord(doc);
			
			if(getKeyWord(doc) != null) {
				if(!keyWords.containsKey(word)) {
				keyWords.put(word, new Occurrence (docFile, 1));
				}
				else {
					keyWords.get(word).frequency++;
				}
				
			}	
		}
		return keyWords;

	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		
		for(String key : kws.keySet()) {
			if(keywordsIndex.containsKey(key)){
				ArrayList<Occurrence> temp = keywordsIndex.get(key);
				
				temp.add(kws.get(key));
				insertLastOccurrence(temp);
				keywordsIndex.put(key, temp);
			}
			else{
				ArrayList<Occurrence> newList = new ArrayList<Occurrence>();
				newList.add(kws.get(key));
				keywordsIndex.put((key), newList);
			}
		}
		
			
		
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 */
	public String getKeyWord(String word) {
		word = word.toLowerCase();
		if(word.length() ==1) {
			return null;
		}
		
		
		
		//remove last character if punctuation
		while(word.charAt(word.length()-1) == '.'|| word.charAt(word.length()-1) == ','|| word.charAt(word.length()-1) ==
				'?'|| word.charAt(word.length()-1) == ':'|| word.charAt(word.length()-1) ==';'|| word.charAt(word.length()-1) =='!') {
			
			word = word.substring(0, word.length()-1);	
		}
		
		//check to see if word contains punctuation within it
		for(int i = 0; i < word.length(); i++) {
			if(!Character.isLetter(word.charAt(i))) {
				return null;
			}
		}
		
		if(noiseWords.containsKey(word)) {
			return null;
		}
		return word;
		
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 */

	
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) 
	{
		ArrayList<Integer> value = new ArrayList<Integer>();
		
		
		int low = 0;
		int high = occs.size()-2;
		int mid = 0;
		int freq = occs.get(occs.size() - 1).frequency;
		
		//perform binary search on occurrences
		while (low < high){
			
			mid = (low + high)/2;
			value.add(mid);
			
			if (occs.get(mid).frequency == freq){			
				low = mid;
				high = mid;
				break;
			}
			else if (occs.get(mid).frequency > freq){			
				low = mid + 1;
			}
			else{			
				high = mid;
			}
		}
		
		//insert last occurrence		
		if (freq > occs.get(low).frequency){		
			occs.add(low, occs.remove(occs.size() - 1));
		}
		else{		
			occs.add(low + 1, occs.remove(occs.size() - 1));
		}		
		return value;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		int x = 0;
		int y = 0;
		int count = 0;
		ArrayList<String> top5result = new ArrayList<String>(5);
		HashMap<String, Integer> num = new HashMap<String, Integer>();
		ArrayList<Occurrence> list1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> list2 = keywordsIndex.get(kw2);


		while(x < list1.size() && y < list2.size()){
			
			Occurrence keyWord1  = list1.get(x);
			Occurrence keyWord2 = list2.get(y);
			int numCompare = keyWord1.frequency - keyWord2.frequency;
			
			//if number of occurrences is the same
			if(numCompare==0){ 
				x++; 
				y++;
				String string1 = keyWord1.document;

				if(num.containsKey(string1) == false){
					num.put(string1, keyWord1.frequency);
					top5result.add(string1); count++;
				}
				//if 5 is already in the arraylist, return the list
				if(count>5){
					return top5result;
				}
				String string2 = keyWord2.document;

				
				if(num.containsKey(string2) == false){
					num.put(string2, keyWord2.frequency);
					top5result.add(string2); count++;
				}
				if(count>5){
					return top5result;
				}
			}
			//else, if there are more occurrences in list1
			else if(numCompare > 0){
				x++;
				String string1 = keyWord1.document;
				
				if(num.containsKey(string1) == false){
					num.put(string1, keyWord1.frequency);
					top5result.add(string1); count++;
				}
				if(count > 5){
					return top5result;
				}
			}
			//else if there are more occurrences in list2
			else if(numCompare<0){ y++;
				String string2 = keyWord2.document;
				if(num.containsKey(string2)==true){

				}
				if(num.containsKey(string2) == false){
					num.put(string2, keyWord2.frequency);
					top5result.add(string2); count++;
				}
				if(count>5){
					return top5result;
				}
			}
		}

		return top5result;
	}
}