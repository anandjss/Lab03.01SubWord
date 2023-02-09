import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 */
public class SubWordFinder implements WordFinder     {
    private ArrayList<ArrayList<String>> dictionary;
    private String alpha = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Initiates the  dictionary and setting up param
     */
    public SubWordFinder()  {
        dictionary = new ArrayList<>();
        for(int i =0; i < 26; i++)
            dictionary.add(new ArrayList<String>());
        populateDictionary();

    }





    /**
     * Populates the dictionary from the text file contents
     * The dictionary object should contain 26 buckets, each
     * bucket filled with an ArrayList<String>
     * The String objects in the buckets are sorted A-Z because
     * of the nature of the text file words.txt
     */
    @Override
    public void populateDictionary() {
        try{
            Scanner in = new Scanner(new File("new_scrabble.txt"));
            while(in.hasNext()) {
                String word = in.nextLine();
                //System.out.println("DEBUG: " + word);
                //System.out.println("DEBUG: " + alpha.indexOf(word.substring(0,1)));
                dictionary.get(alpha.indexOf(word.substring(0,1))).add(word);
            }
            in.close();
            for(int i = 0; i < dictionary.size(); i++){
                Collections.sort(dictionary.get(i));
            }


            //for(ArrayList<String> bucket : dictionary)
            //System.out.println(bucket);
        }
        catch(Exception e){
            System.out.println("Error here: " + e);
        }

    }



    /**
     * Retrieve all SubWord objects from the dictionary.
     * A SubWord is defined as a word that can be split into two
     * words that are also found in the dictionary.  The words
     * MUST be split evenly, e.g. no unused characters.
     * For example, "baseball" is a SubWord because it contains
     * "base" and "ball" (no unused characters)
     * To do this, you must look through every word in the dictionary
     * to see if it is a SubWord object
     *
     * @return An ArrayList containing the SubWord objects
     * pulled from the file words.txt
     */
    @Override
    public ArrayList<SubWord> getSubWords() {
        ArrayList<SubWord> subwords = new ArrayList<SubWord>();
        // look tru each bucket at ever word
        for (ArrayList<String> bucket : dictionary) {
            for (String word : bucket) {
                String front = "", back = "";
                for (int i = 2; i < word.length()-1; i++) {
                    front = word.substring(0, i);
                    back = word.substring(i);
                    if (inDictionary(front) && inDictionary(back)) {
                        subwords.add(new SubWord(word, front, back));
                    }
                }
            }
        }
        return subwords;
    }

    /**
     * Look through the entire dictionary object to see if
     * word exists in dictionary
     *
     * @param word The item to be searched for in dictionary
     * @return true if word is in dictionary, false otherwise
     * NOTE: EFFICIENCY O(log N) vs O(N) IS A BIG DEAL HERE!!!
     * You MAY NOT use Collections.binarySearch() here; you must use
     * YOUR OWN DEFINITION of a binary search in order to receive
     * the credit as specified on the grading rubric.
     */
    @Override
    public boolean inDictionary(String word) {
        ArrayList<String> bucket = dictionary.get(alpha.indexOf(word.substring(0,1)));
        int left = 0, right = bucket.size()-1;
        while(left <= right)    {
            int mid = (right + left) /2;
            if(bucket.get(mid).equals(word))
                return true;
            else if(bucket.get(mid).compareTo(word) < 0)
                left = mid + 1;
            else
                right = mid-1;
        }
        return false;

    }
/**
 * Main method for project 03.01 SubWordFinder
 * @param args Command line arguments, if needed
 */
    public static void main(String[] args) {
        SubWordFinder app = new SubWordFinder();
        ArrayList<SubWord> subwords = app.getSubWords();
        System.out.println("* List of SubWord objects in dictionary *");
        for(SubWord temp: subwords)
            System.out.println(temp);
        System.out.println(subwords.size() + " total SubWords");
    }
}
