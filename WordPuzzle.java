package com.dsa.project3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Scanner;

/**
 * Word puzzle to find words from a dictionary in a random character grid
 * @author Anshul Pardhi
 *
 */
public class WordPuzzle {
	
	private static MyHashTable<String> prefixHashTable;
	
	/**
	 * Reads a file to get dictionary words
	 * @param filePath the file path
	 * @return a BufferedReader object
	 * @throws FileNotFoundException
	 */
	public static BufferedReader readFile(String filePath) throws FileNotFoundException {
		
		InputStream fis = new FileInputStream(filePath);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        return new BufferedReader(isr);
	}
	
	/**
	 * Inserts words from the file to the hash tables (normal and prefix hash table)
	 * @param br BufferedReader object
	 * @return hash table
	 * @throws IOException
	 */
	public static MyHashTable<String> insert(BufferedReader br) throws IOException {
		
		MyHashTable<String> hashTable = new MyHashTable<>( );
		
		String line;
		while ((line = br.readLine()) != null) {
			//Split the word into characters, to add all the prefixes of the word to the prefix hash table
			char[] prefixArray = line.toCharArray();
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<prefixArray.length; i++) {
				sb.append(prefixArray[i]);
				prefixHashTable.insert(sb.toString());
			}
            
			hashTable.insert(line); //Add word to the hash table
        }
		
		return hashTable;
	}
	
	/**
	 * Generates a random grid of characters
	 * @param rows total rows
	 * @param columns total columns
	 * @return grid
	 */
	public static char[][] generateRamdomGrid(int rows, int columns) {
		
		Random rand = new Random();
		final String alphabets = "abcdefghijklmnopqrstuvwxyz"; //Grid contains only alphabets
		final int size = alphabets.length();
		
		char[][] grid = new char[rows][columns];
        for(int i=0; i<rows; i++) {
        	for(int j=0; j<columns; j++) {
        		grid[i][j] = alphabets.charAt(rand.nextInt(size));
        	}
        }
		
		return grid;
	}
	
	/**
	 * Prints the grid
	 * @param grid
	 * @param rows
	 * @param columns
	 */
	public static void printGrid(char[][] grid, int rows, int columns) {
		
		for (int i=0; i<rows; i++) {
        	for (int j=0; j<columns; j++) {
        		System.out.print(grid[i][j] + " ");
        	}
	        System.out.println();
	    }
	}
	
	/**
	 * Find all the words present in the dictionary in the grid
	 * @param hashTable mapped hash table
	 * @param grid random character grid
	 * @param rows total rows of grid
	 * @param columns total columns of grid
	 * @param isPrefix true, if checks for prefixes (enhancement)
	 * @param isPrint if true, prints the words found
	 * @return elapsed time in milliseconds to find all the words
	 */
	private static long findWords(MyHashTable<String> hashTable, char[][] grid, int rows, int columns, boolean isPrefix, boolean isPrint) {
		
		long startTime = System.nanoTime();
		
		StringBuilder sb = new StringBuilder();
		
		//For each character in the grid, traverse all the directions to retrieve words
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				
				//Check if the character is present in dictionary
				sb.setLength(0);
				sb.append(grid[i][j]);
				if(hashTable.contains(new String(sb)) && isPrint) {
					System.out.println("Found \"" + sb + "\" at " + "(" + i + "," + j + ")");
				}
				
				//Traverse in horizontally forward direction ->
				for(int k=j+1; k<columns; k++) {
					sb.append(grid[i][k]);
					String sbString = new String(sb);
					if(isPrefix && !prefixHashTable.contains(sbString)) {
						break;
					}
					if(hashTable.contains(sbString) && isPrint) {
						System.out.println("Found \"" + sb + "\" horizontally forward at " + "(" + i + "," + j + ")");
					}
				}
				
				sb.setLength(0);
				sb.append(grid[i][j]);
				
				//Traverse in horizontally backward direction <-
				for(int k=j-1; k>=0; k--) {
					sb.append(grid[i][k]);
					String sbString = new String(sb);
					if(isPrefix && !prefixHashTable.contains(sbString)) {
						break;
					}
					if(hashTable.contains(sbString) && isPrint) {
						System.out.println("Found \"" + sb + "\" horizontally backward at " + "(" + i + "," + j + ")");
					}
				}
				
				sb.setLength(0);
				sb.append(grid[i][j]);
				
				//Traverse in vertically downward direction |
				//											v
				for(int k=i+1; k<rows; k++) {
					sb.append(grid[k][j]);
					String sbString = new String(sb);
					if(isPrefix && !prefixHashTable.contains(sbString)) {
						break;
					}
					if(hashTable.contains(sbString) && isPrint) {
						System.out.println("Found \"" + sb + "\" vertically down at " + "(" + i + "," + j + ")");
					}
				}
				
				sb.setLength(0);
				sb.append(grid[i][j]);
				
				//                                        ^
				//Traverse in vertically upward direction |
				for(int k=i-1; k>=0; k--) {
					sb.append(grid[k][j]);
					String sbString = new String(sb);
					if(isPrefix && !prefixHashTable.contains(sbString)) {
						break;
					}
					if(hashTable.contains(sbString) && isPrint) {
						System.out.println("Found \"" + sb + "\" vertically up at " + "(" + i + "," + j + ")");
					}
				}
				
				sb.setLength(0);
				sb.append(grid[i][j]);
				
				//                                            ^
				//Traverse in diagonally top-right direction /
				for(int k=i-1, l=j+1; k>=0 && l<columns; k--, l++) {
					sb.append(grid[k][l]);
					String sbString = new String(sb);
					if(isPrefix && !prefixHashTable.contains(sbString)) {
						break;
					}
					if(hashTable.contains(sbString) && isPrint) {
						System.out.println("Found \"" + sb + "\" diagonally top-right at " + "(" + i + "," + j + ")");
					}
				}
				
				sb.setLength(0);
				sb.append(grid[i][j]);
				
				//Traverse in diagonally bottom-right direction \
				//                                               v
				for(int k=i+1, l=j+1; k<rows && l<columns; k++, l++) {
					sb.append(grid[k][l]);
					String sbString = new String(sb);
					if(isPrefix && !prefixHashTable.contains(sbString)) {
						break;
					}
					if(hashTable.contains(sbString) && isPrint) {
						System.out.println("Found \"" + sb + "\" diagonally bottom-right at " + "(" + i + "," + j + ")");
					}
				}
				
				sb.setLength(0);
				sb.append(grid[i][j]);
				
				//                                         ^
				//Traverse in diagonally top-left direction \
				for(int k=i-1, l=j-1; k>=0 && l>=0; k--, l--) {
					sb.append(grid[k][l]);
					String sbString = new String(sb);
					if(isPrefix && !prefixHashTable.contains(sbString)) {
						break;
					}
					if(hashTable.contains(sbString) && isPrint) {
						System.out.println("Found \"" + sb + "\" diagonally top-left at " + "(" + i + "," + j + ")");
					}
				}
				
				sb.setLength(0);
				sb.append(grid[i][j]);
				
				//Traverse in diagonally bottom-left direction /
				//                                            v
				for(int k=i+1, l=j-1; k<rows && l>=0; k++, l--) {
					sb.append(grid[k][l]);
					String sbString = new String(sb);
					if(isPrefix && !prefixHashTable.contains(sbString)) {
						break;
					}
					if(hashTable.contains(sbString) && isPrint) {
						System.out.println("Found \"" + sb + "\" diagonally bottom-left at " + "(" + i + "," + j + ")");
					}
				}
			}
		}
		
		long endTime = System.nanoTime();
	    return (endTime - startTime);
	}
	
	public static void main(String[] args) {
		
		MyHashTable<String> hashTable = null;
		prefixHashTable = new MyHashTable<>();
        try {
        	//Change the file path to your dictionary file location
        	String filePath = "dictionary.txt";
        	BufferedReader br = readFile(filePath);
        	hashTable = insert(br); //Insert words from dictionary into the hash tables
            br.close();
        } catch (FileNotFoundException e) {
        	System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("I/O Exception!");
		}
        
        if(hashTable == null || prefixHashTable == null) {
        	System.out.println("Error while inserting words in the hash table!");
        	return;
        }
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter total number of rows of grid: ");
        int rows = sc.nextInt();
        
        System.out.println("Enter total number of columns of grid: ");
        int columns = sc.nextInt();
        
        sc.close();
        
        char[][] grid = generateRamdomGrid(rows, columns);
        
        printGrid(grid, rows, columns);
        
        long normalTime = findWords(hashTable, grid, rows, columns, false, true);
        long enhancedTime = findWords(hashTable, grid, rows, columns, true, false);
        
        System.out.println("Elapsed time for normal hashing: " + normalTime + " nanoseconds or " + normalTime/1000000 + " milliseconds.");
        System.out.println("Elapsed time for enhanced prefix hashing: " + enhancedTime + " nanoseconds or " + enhancedTime/1000000 + " milliseconds.");
	}

}