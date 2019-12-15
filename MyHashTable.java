package com.dsa.project3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Hash table that resolves open addressing using linear probing
 * @author Mark Allen Weiss, Anshul Pardhi
 *
 * @param <AnyType>
 */
public class MyHashTable<AnyType> {
	
	private static final int DEFAULT_TABLE_SIZE = 10000;
	
	private HashEntry<AnyType>[] array;
	private int currentSize;
	
	/**
	 * Default constructor
	 */
	public MyHashTable() {
		
		this(DEFAULT_TABLE_SIZE);
	}
	
	/**
	 * Constructor that allocates size and initializes the hash table
	 * @param size size of the hash table
	 */
	public MyHashTable(int size) {
		
		allocateArray(size);
		makeEmpty();
	}
	
	/**
	 * Returns the current size of the hash table
	 * @return current size of the hash table
	 */
	public int size() {
		
		return currentSize;
	}
	
	/**
	 * Returns the total size of the hash table
	 * @return total size of the hash table
	 */
	public int capacity() {
		
		return array.length;
	}
	
	/**
	 * Initializes the hash table to null
	 */
	public void makeEmpty() {
		
		currentSize = 0;
		for(int i=0; i<array.length; i++) {
			array[i] = null;
		}
	}
	
	/**
	 * Checks of the hash table contains a particular value
	 * @param x the value to be checked
	 * @return true, if value present
	 */
	public boolean contains(AnyType x) {
		
		int currentPos = findPos(x);
		return isActive(currentPos);
	}
	
	/**
	 * Insert an entry into the hash table
	 * @param x value to insert
	 */
	public void insert(AnyType x) {
		
		int currentPos = findPos(x);
		if(isActive(currentPos)) {
			return; //Do nothing, since value is already present in the hash table
		}
		
		array[currentPos] = new HashEntry<>(x, true);
		
		//Rehash if hash table is more than half full
		if(++currentSize > array.length/2) {
			rehash();
		}
	}
	
	/**
	 * Remove an entry from the hash table
	 * @param x value to remove
	 */
	public void remove(AnyType x) {
		
		int currentPos = findPos(x);
		if(isActive(currentPos)) {
			array[currentPos].isActive = false;
			currentSize--;
		}
	}
	
	/**
	 * Allocate a new size to the hash table depending on the next available prime number
	 * @param arraySize current size of the hash table
	 */
	private void allocateArray(int arraySize) {
		
		array = new HashEntry[nextPrime(arraySize)];
	}
	
	/**
	 * Checks if a particular element is active
	 * @param currentPos the index of the element to be checked
	 * @return
	 */
	private boolean isActive(int currentPos) {
		
		return (array[currentPos] != null && array[currentPos].isActive);
	}
	
	/**
	 * Get the index of the passed value
	 * @param x passed value
	 * @return index of the passed value
	 */
	private int findPos(AnyType x) {
		
		int currentPos = myHash(x);
		
		while(array[currentPos] != null && !array[currentPos].element.equals(x)) {
			currentPos++; //Linear probing
			if(currentPos >= array.length) {
				currentPos -= array.length; //Overflow condition, take mod
			}
		}
		
		return currentPos;
	}
	
	/**
	 * Allocate a bigger size to the hash table in case the hash table is more than half full
	 */
	private void rehash() {
		
		HashEntry<AnyType>[] oldArray = array;
		
		allocateArray(nextPrime(2*oldArray.length)); //Allocate new size to the hash table
		currentSize = 0;
		
		//Copy the values from the old hash table to the new one
		for(int i=0; i<oldArray.length; i++) {
			if(oldArray[i] != null && oldArray[i].isActive) {
				insert(oldArray[i].element);
			}
		}
	}
	
	/**
	 * Uses Java's hashCode method to generate the hash value for a particular element, and bounds it in between the hash table size
	 * @param x the value to hash
	 * @return hashed value
	 */
	private int myHash(AnyType x) {
		
		int hashVal = x.hashCode(); //Java's in-built hashCode method to retrieve the hash value
		hashVal %= array.length;
		
		if(hashVal < 0) {
			hashVal += array.length; //Bound the value in between the hash table size
		}
		
		return hashVal;
	}
	
	/**
	 * Finds the next prime number
	 * @param n the current number
	 * @return the next prime number
	 */
	private static int nextPrime(int n) {
		
		if(n%2 == 0) {
			n++;
		}
		
		while(!isPrime(n)) {
			n += 2;
		}
		
		return n;
	}
	
	/**
	 * Checks if a number is prime
	 * @param n the passed number
	 * @return true, if prime
	 */
	private static boolean isPrime(int n) {
		
		if(n%2 == 0 || n%3 == 0) {
			return true;
		}
		
		if(n == 1 || n%2 == 0) {
			return false;
		}
		
		for(int i=3; i*i <= n; i+=2) {
			if(n%i == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * A custom class that represents the hash table data structure, element stores the value, isActive indicates if the position is 
	 * @author Anshul Pardhi
	 *
	 * @param <AnyType>
	 */
	private static class HashEntry<AnyType> {
		
		public AnyType element;
		public boolean isActive;
		
		public HashEntry(AnyType e) {
			this(e, true);
		}
		
		public HashEntry(AnyType e, boolean i) {
			element = e;
			isActive = i;
		}
	}

}
