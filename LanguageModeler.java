/**
* Written by Justin A. Middleton.
* Last updated early 2015.
*/

import java.util.*;
import java.io.*;

public class LanguageModeler {
  private int K;
  private String first;
  private HashMap<String, ArrayList<Letter>> map;
  private Random rand;
  
  /**
  * Constructor. Takes in K and a source file.
  * Also reads the entire file and records all patterns.
  */
  public LanguageModeler(int K, File text) {
    this.K = K;
    this.map = new HashMap<String, ArrayList<Letter>>();
    this.rand = new Random();
    
    //Open up file.
    InputStream is;
    try {
      is = new FileInputStream(text);
    } catch (FileNotFoundException e) {
      System.out.println("File not found!");
      return;
    }
    
    StringBuilder sb = new StringBuilder();    
    try {
      int nextChar = -1;
      
      //First, read in the first string of K characters.
      while (sb.length() < K && (-1 != (nextChar = is.read())))
        sb.append((char) nextChar);
      this.first = sb.toString();
      
      //Record every K-length string and the character that comes after.
      while (-1 != (nextChar = is.read())) {
        recordPattern(sb.toString(), (char) nextChar);
        sb.append((char) nextChar);
        sb.deleteCharAt(0);
      }
    } catch (IOException e) {
      System.out.println("Something is WRONG!");
      return;
    }
  }

  /**
  * Takes in a string and the next character, and stores this pattern
  * in the language modeler's map.
  */
  private void recordPattern(String str, char next) {
    //If the map already contains the string, just add the new letter.
    //If not, add the string, then add the letter.
    if (map.containsKey(str)) {
      ArrayList<Letter> letters = map.get(str);
      boolean hasLetter = false;
      
      for (Letter letter : letters) {
        if (next == letter.getLetter()) {
          hasLetter = true;
          letter.increment();
          break;
        }
      }
      
      if (!hasLetter) 
        letters.add(new Letter(next));
    } else {
      map.put(str, new ArrayList<Letter>());
      map.get(str).add(new Letter(next));
    }
  }
  
  /**
  * Returns the first K-length string of the source, if necessary.
  */
  public String firstSeed() {
    return first;
  }
  
  /**
  * Returns a random K-length string from the source.
  */
  public String randomSeed() {
    Object[] seeds = map.keySet().toArray();
    int index = rand.nextInt(map.size());
    return (String) seeds[index];
  }
  
  /**
  * For a given string, returns a character that could follow after it, based
  * on probability in the source file.
  *
  * For example, if, in the source, the string "the " happened 3 times and it was
  * followed once by an "m" and twice by a "p", this function would have a 33%
  * chance of returning m and a 67% chance of returning "p".
  *
  * Returns -1 as a character if an error occurs.
  */
  public char nextChar(String seed) {
    ArrayList<Letter> possibilities = map.get(seed);
    if (null == possibilities) 
      return (char) -1;
    int r = rand.nextInt(sumFrequency(possibilities));
    
    //Since each letter records its frequency, we have to check if the random
    //number is less than the frequency of the current letter. If it is, we go
    //on, decrementing r with the frequency. If not, we return this letter.
    char next = (char) -1;
    for (Letter letter : possibilities) {
      if (r < letter.getFrequency()) {
        next = letter.getLetter();
        break;
      } else {
        r -= letter.getLetter();
      }        
    }
    
    //If, for whatever reason, the char is still -1, then just get the last letter.
    if ((char) -1 == next && possibilities.size() != 0) 
      next = possibilities.get(possibilities.size() - 1).getLetter();
    
    return next;
  }
  
  /**
  * Add up the number of letter occurrences in the given arraylist.
  */
  private int sumFrequency(ArrayList<Letter> letters) {
    int total = 0;
    for (Letter letter : letters) 
      total += letter.getFrequency();
    return total;
  }
  
  /**
  * Represents a single character the number of times it appeared after a
  * specific string.
  */
  private class Letter {
    private char letter;
    private int frequency;
    
    public Letter(char letter) {
      this.letter = letter;
      this.frequency = 1;
    }
    
    public int increment() {
      return ++frequency;
    }
    
    public char getLetter() {
      return letter;
    }
    
    public int getFrequency() {
      return frequency;
    }
  }
}