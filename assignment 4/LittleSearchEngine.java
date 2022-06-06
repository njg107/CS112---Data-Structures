package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}

	private static Occurrence Get(ArrayList<Occurrence> aList ,Occurrence Oc){

		Occurrence tempOc = null;

		for(int i = 0; i < aList.size(); i++){

			if(aList.get(i).document.equals(Oc.document)){if(aList.get(i).frequency>Oc.frequency){tempOc = aList.get(i);}}
		}

		return tempOc;
	}

	private static ArrayList<Occurrence> Sort(ArrayList<Occurrence> al){

		//Occurrence tempOc = al.get(0);
		//for(int i = 1; i < al.size(); i++){if(al.get(i).frequency > tempOc.frequency){tempOc = al.get(i);}}

		ArrayList<Occurrence> returnOc = new ArrayList<Occurrence>();
		//returnOc.add(tempOc);
		//al.remove(tempOc);
		for(int i = 0; i <= al.size() + 1; i++){

			Occurrence tempOc = al.get(0);
			if(!al.isEmpty()){for(int j = 1; j < al.size(); j++){if(al.get(j).frequency > tempOc.frequency){tempOc = al.get(j);}}}
			returnOc.add(tempOc);
			al.remove(tempOc);
		}

		return returnOc;
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {

		if(!new File(docFile).exists()){
			throw new FileNotFoundException();
		}


		Scanner scanner = new Scanner(new File(docFile));
		HashMap<String,Occurrence> hashMap = new HashMap<String,Occurrence>();

		while(scanner.hasNext()){

			String words = scanner.next().trim();
			String wordResult = getKeyword(words);

			if(wordResult != null && wordResult.length() != 0){

				if(hashMap.containsKey(wordResult)){

					Occurrence temp = hashMap.get(wordResult);
					temp.frequency = temp.frequency + 1;
				}
				else{
					
					hashMap.put(wordResult, new Occurrence(docFile,1)); 
				}
			}
		}

		return hashMap;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {

		for(String tempS : kws.keySet()){

			ArrayList<Occurrence> tempAl = keywordsIndex.get(tempS);
			Occurrence tempOc = kws.get(tempS);

			if(tempAl != null){

				tempAl.add(tempOc);
				keywordsIndex.put(tempS, tempAl);
				insertLastOccurrence(tempAl);
			}
			else{

				ArrayList<Occurrence> newAl = new ArrayList<Occurrence>();
				newAl.add(tempOc);
				keywordsIndex.put(tempS, newAl);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {

		String tempS = "";
		boolean check = false;

		for(int i = (word.length() - 1); i > -1; i--){

			char tempC = word.charAt(i);

			if(Character.isLetter(tempC)){
				
				tempS = tempC + tempS;
				check = true;
			}
			if(!Character.isLetter(tempC) && check){return null;}
		}

		if(tempS.length() == 0 || noiseWords.contains(tempS.toLowerCase())){return null;}
		else{return tempS.toLowerCase();}
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {

		int high = occs.size() - 2;
		int low = 0;

		if(occs.size() <= 1 || occs == null){return null;}

		Occurrence Oc = occs.get(occs.size() - 1);

		ArrayList<Integer> midV  = new ArrayList<Integer>();

		while(low <= high){

			int mid = (low + high) / 2;
			midV.add(mid);

			if(occs.get(mid).frequency != Oc.frequency){
				high = mid - 1;
			}
			else if(occs.get(mid).frequency > Oc.frequency){
				low = mid + 1;
			}
			else if(occs.get(mid).frequency == Oc.frequency){
				break;
			}
		}

		if(occs.get(occs.size() - 2).frequency > occs.get(occs.size() - 1).frequency){return midV;}
		occs.remove(occs.size() - 1);

		int midF = occs.get(midV.get(midV.size() - 1)).frequency;

		if(Oc.frequency >= midF){occs.add(midV.get(midV.size() - 1), Oc);}
		else{occs.add(midV.get(midV.size() - 1) + 1, Oc);}

		return midV;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {

		ArrayList<String> returnList  = new ArrayList<String>();
		
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();

		if(keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)){
			
			ArrayList<Occurrence> tempOc = new ArrayList<Occurrence>();
			ArrayList<String> tempS = new ArrayList<String>();

			ArrayList<Occurrence> OcL1 = keywordsIndex.get(kw1);
            ArrayList<Occurrence> OcL2 = keywordsIndex.get(kw2);

			for(int i = 0; i < OcL1.size(); i++){

				Occurrence x = Get(OcL2, OcL1.get(i));
				
				if(!tempS.contains(OcL1.get(i).document) && x == null){

					tempOc.add(OcL1.get(i));
					tempS.add(OcL1.get(i).document);
				}
				else if(!tempS.contains(x.document)){

					tempOc.add(x);
					tempS.add(x.document);
				}
			}

			for(int i = 0; i < OcL2.size(); i++){

				Occurrence x = Get(OcL2, OcL1.get(i));

				if(!tempS.contains(OcL2.get(i).document)){

					tempOc.add(OcL2.get(i));
					tempS.add(OcL2.get(i).document);
				}
			}

			tempOc = Sort(tempOc);

			for(int i = 0; i < tempOc.size(); i++){returnList.add(tempOc.get(i).document);}
		}

		if(!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)){return returnList;}

		if(!keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)){

			ArrayList<Occurrence> OcL = keywordsIndex.get(kw2);

			for(int i = 0; i < OcL.size() && i < 5; i++){returnList.add(OcL.get(i).document);}
		}

		if(keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)){

			ArrayList<Occurrence> OcL = keywordsIndex.get(kw1);

			for(int i = 0; i < OcL.size() && i < 5; i++){returnList.add(OcL.get(i).document);}
		}

		return returnList;
	}
}
