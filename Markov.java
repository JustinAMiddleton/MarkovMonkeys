/**
* Written by Justin A. Middleton.
* Last updated early 2015.
*/

import java.util.*;
import java.io.*;

public class Markov {
  public static void main(String[] args) {
    int K;
    int length;
    File source;
    PrintWriter result;
    
    //Check number of arguments.
    if (args.length != 4) {
      System.out.println("Incorrect input. Try: \n\t<k> <output length> <src file> <output file>");
      return;
    }
    
    //Parse K and length.
    try {
      int K = Integer.parseInt(args[0]);
      int length = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println("K and output length must be integers.");
      return;
    }
    
    //Parse src and output files.
    source = new File(args[2]);
    try {
      result = new PrintWriter(args[3]);
    } catch (FileNotFoundException e) { 
      System.out.println("The result file cannot be created.");
      return; 
    }
    
    //Check to see if source file exists and if K and length are valid ints.
    if (!source.exists()) {
      System.out.println("Source file does not exist.");
      return;
    } else if (K > length) {
      System.out.println("K cannot be greater than the total output length.");
      return;
    } else if (K < 0 || length < 0) {
      System.out.println("K and length cannot be negative.");
      return;
    }
 
    LanguageModeler lm = new LanguageModeler(K, source);
    String start = lm.randomSeed();

    for (int i = 0; i < start.length(); ++i) 
      result.write(start.charAt(i));
    length -= start.length();
    
    StringBuilder sb = new StringBuilder(start);
    char next;
    while (length > 0) {
      next = lm.nextChar(sb.toString());
      
      //If the current string is not recorded with anything after it (as would happen
      //when it's at the end of the file), it tries a new random string.
      if ((char) -1 == next) {
        start = lm.randomSeed();
        for (int i = 0; i < start.length(); ++i) 
          result.write(start.charAt(i));
        sb = new StringBuilder(start);
        length -= start.length();
        continue;
      }
      
      result.write((char) next);
      sb.append(next);
      sb.deleteCharAt(0);
      length--;
    }
    
    result.close();
  }
}