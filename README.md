# MarkovMonkeys

This program will take in a text file and will return output that follows the 
frequency of letter patterns.

For example, say a file contains "the man, the plan, the pie" and the user specifies a depth of 4. 
This program would go through every 4-length string and record what letter comes next. 

This means that the program will record three instances of the string "the ", and in this, it will 
record that it was followed by "m" once and "p" twice.

Once the file is completely parsed, it will take a random K-length string (in the above case, K=4) 
and then pick a letter to go after it. If the starting string is "the ", it has a 33% chance of being 
followed by an "m" and 67% by a "p". If "p" is chosen, then the program will see what is likely to come 
after "he p".

It'll keep adding characters in this manner until it reaches a user-specified limit. Results will be 
in the specified output file.

compile: javac Markov.java

run: java Markov [K] [length of output] [src text file] [output file]

K = The number of character to analyze at a time.
