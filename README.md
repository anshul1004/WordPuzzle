# WordPuzzle

The program first generates a word puzzle consisting of random rows and columns, based on the number of rows and columns input by the user.
A hash table is created using linear probing to store all the words of a dictionary, for efficient access.

From the generated word puzzle, the program finds out all the meaningful words in all possible directions (horizontal, vertical, diagonal) and also outputs the starting indices of all the found words.

Another enhancement to the algorithm is made where prefixes of the words of the ditionary are stored. If the prefix is not found, the rest of the string can be ignored to check if it exists in the word puzzle.
