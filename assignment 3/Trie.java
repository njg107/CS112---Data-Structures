package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }

	private static void straightTraverse(String a, String[] allWords,int lastIndex,String changedString, String originalString, TrieNode ptr){

		String temp = "";
		String stat = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, (ptr.substr.endIndex + 1));

		boolean check = false;

		for(int i = 0; i < changedString.length(); i++){

			char x = changedString.charAt(i);
			temp = temp + x;

			if (stat.startsWith(temp)){

				check = true;
				continue;
			}
			else if(check){

				temp = temp.substring(0, temp.length() - 1);
				break;
			}
			else if(!check){

				temp = "";
				break;
			}
		}

		a = a + temp;

		if(temp.length() == 0){

			if(ptr.sibling == null){
				Indexes tempIndexes = new Indexes((short)lastIndex, (short)originalString.indexOf(changedString,a.length()), (short)(originalString.indexOf(changedString, a.length()) + changedString.length() - 1));
				ptr.sibling = new TrieNode(tempIndexes, null, null);
			}
			else{
				straightTraverse(a, allWords, lastIndex, changedString, originalString, ptr.sibling);
			}
		}
		else if(temp.equals(stat)){

			String pos = changedString.substring(changedString.indexOf(stat) + stat.length());
			straightTraverse(a, allWords, lastIndex, pos, originalString, ptr.firstChild);
		}
		else{
			
			ptr.substr.startIndex = (short)(allWords[ptr.substr.wordIndex].indexOf(stat));
			ptr.substr.endIndex = (short)(allWords[ptr.substr.wordIndex].indexOf(stat) + temp.length() - 1);
			String s = allWords[ptr.substr.wordIndex].substring(ptr.substr.endIndex + 1);
			String t = originalString.substring(ptr.substr.endIndex + 1);

			if(ptr.firstChild == null){
				Indexes tempIndexes = new Indexes((short)ptr.substr.wordIndex, (short)allWords[ptr.substr.wordIndex].indexOf(s), (short)(allWords[ptr.substr.wordIndex].indexOf(s) + s.length() - 1));
				ptr.firstChild = new TrieNode(tempIndexes, null, null);
			}
			else{

				s = stat.substring(temp.length());

				Indexes tempIndexes = new Indexes((short)ptr.substr.wordIndex, (short)allWords[ptr.substr.wordIndex].indexOf(s, temp.length()), (short)(allWords[ptr.substr.wordIndex].indexOf(s, temp.length()) + s.length() - 1));
				TrieNode tempNode = new TrieNode(tempIndexes, null, null);

				tempNode.firstChild = ptr.firstChild;
				ptr.firstChild = tempNode;
			}

			straightTraverse(a, allWords, lastIndex, t, originalString, ptr.firstChild);
		}
	}
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {

		TrieNode ptr = null;

		for(int i = 0; i < allWords.length; i++){

			if (ptr == null){

				TrieNode temp = new TrieNode(new Indexes(0, (short)0, (short) (allWords[0].length() - 1)), null, null);
				ptr = new TrieNode(null, temp, null);
			}
			else{
				straightTraverse("", allWords, i, allWords[i], allWords[i], ptr.firstChild);
			}
		}

		return ptr;
	}

	private static boolean contains(ArrayList<TrieNode> list, String[] allWords, String a){
		
		for(int i = 0; i < list.size(); i++){
			if(allWords[list.get(i).substr.wordIndex].equals(a)){return true;}
        }
		
		return false;
	}

	private static void add(TrieNode root, ArrayList<TrieNode> list, String[] allWords){
		
		TrieNode ptr = root;

		if(ptr == null){return;}

		TrieNode tempNode = ptr;
		
		while(ptr != null){
			
			if(!contains(list, allWords, allWords[ptr.substr.wordIndex])){
				list.add(ptr);
			}

			add(ptr.firstChild, list, allWords);
			ptr = ptr.sibling;
		}
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		
		ArrayList<TrieNode> list  = new ArrayList<TrieNode>();

		if(root.substr == null){root = root.firstChild;}

		String stat = allWords[root.substr.wordIndex].substring(root.substr.startIndex, root.substr.endIndex + 1);

		if(stat.length() == prefix.length() || stat.length() > prefix.length()){

			if(!stat.startsWith(prefix)){

				if(root.sibling == null){
					return null;
				}
				else{
					return completionList(root.sibling, allWords, prefix);
				}
			}
		}

		String temp = "";
		boolean check = false;

		for(int i = 0; i < prefix.length(); i++){

			char x = prefix.charAt(i);
			temp = temp + x;

			if (stat.startsWith(temp)){

				check = true;
				continue;
			}
			else if(check){

				temp = temp.substring(0, temp.length() - 1);
				break;
			}
			else if(!check){

				temp = "";
				break;
			}
		}

		if(temp.length() == 0){
			if(root.sibling != null){return completionList(root.sibling, allWords, prefix);}
		}
		else if(prefix.length() > stat.length()){
			
			prefix = prefix.substring(temp.length());
			if(root.firstChild != null){return completionList(root.firstChild, allWords, prefix);}
		}
		else{
			if(root.firstChild == null){list.add(root);}
			else{add(root.firstChild, list, allWords);}
		}
		
		if(list.isEmpty()){return null;}

		return list;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
