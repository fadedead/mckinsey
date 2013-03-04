package challenge2;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author Rohit Mathew
 * 
 *	This class takes as input an array of integers and then sorts them based on frequency
 */

public class SortByFrequency {

	public static void main(String[] args) {
		int size = 0, key = 0, value = 0;
		Map<Integer,Integer> freqMap=new HashMap<Integer, Integer>();
		
		System.out.println("Please enter the size of the array");
		Scanner in = new Scanner(System.in);
		size = in.nextInt();
		
		/*
		 * Taking the input from the user and storing it in an associative array
		 * The time complexity for this step is O(n)
		 */
		System.out.println("Please enter "+ size +" integer(s) one by one");
		for(int i=0 ; i<size ; i++)
		{
			try
			{
				key = in.nextInt();
			}
			catch(InputMismatchException ime)
			{
				System.out.println("Sorry, you did not enter a valid integer. Please execute code again.");
				System.exit(0);
			}
			
			 if(freqMap.containsKey(key))
			 {
				 value = freqMap.get(key);
				 freqMap.put(key, ++value);
			 }
			 else
			 {
				 freqMap.put(key, 1);
			 }
		}
		
		/*
		 * Sorting the Associative Array wrt to the value of the Frequency
		 * The time complexity of this step is O(klog(k))
		 * Here, k is the number of unique elements 
		 */
		List<Map.Entry<Integer, Integer>> list =
	            new LinkedList<Map.Entry<Integer, Integer>>( freqMap.entrySet() );
		
        Collections.sort( list, new Comparator<Map.Entry<Integer, Integer>>()
        {
            public int compare( Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );
        
    	/*
    	 *  Print all values with respective counts
    	 */
        
        System.out.println("The integers on the left column shows the integers in increasing order of frequency");
        for(Map.Entry<Integer, Integer> entry : list)
        {
        	System.out.println(entry.getKey() + " occurs " + entry.getValue() + " times");
        }
	        
		in.close();
	}

}
