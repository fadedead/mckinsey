package challenge1;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Rohit Mathew
 * 
 * This class takes a hashtag as an input and then retrieves the 100 latest queries that
 * contain that hashtag. On the fly, it checks if there are any urls persent in the text
 * of the hashtag. If there are, they are parsed and retrieved using a regex and stored in
 * an ArrayList.
 * After all records are checked, the contents of the ArrayList are printed to the console.
 *
 */

public class TwitterRetrieval {
	static final ObjectMapper mapper = new ObjectMapper();
	
	/* Regex for finding the URL in the text of the tweet */
	static final Pattern urlPattern = Pattern.compile(
	            "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" + 
	            "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" + 
	            "|mil|biz|info|mobi|name|aero|jobs|museum" + 
	            "|travel|[a-z]{2}))(:[\\d]{1,5})?" + 
	            "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" + 
	            "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + 
	            "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" + 
	            "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + 
	            "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" + 
	            "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");
	//Regex for checking the input given by the user
	static final Pattern inputPattern = Pattern.compile("^[a-zA-Z0-9]*$");
    
	
	public static void main(String[] args) {		
		int itr = 0;
		ArrayList<String> urlList = new ArrayList<String>(); //ArrayList to store the list of urls
		String hashTag = "";
		Matcher mat;
		
		while(true)
		{
			//Asking the user for the input hashtag
			System.out.println("Please enter hashtag. (No digits or special characters). Enter 99 to quit");
			Scanner sc = new Scanner(System.in);
			hashTag = sc.nextLine();
			if(hashTag.length() == 0)
			{
				//The user has not entered anything to console
				System.out.println("Sorry, please enter a valid input.");
				continue;
			}
			if(hashTag.equalsIgnoreCase("99"))
			{
				//The user has chosen to quit
				System.out.println("You have chosen to quit. Thank you. Bye!");
				sc.close();
				System.exit(0);
			}
			else
			{
				//INPUT VALIDATION
				hashTag.trim();
				/*Remove starting hashtag if it exists any hashtags that exist after 
				 *the first one is treated as a special character and will be removed by
				 *the latter part of the code
				 */
				if(hashTag.startsWith("#"))
				{
					hashTag = hashTag.substring(1);
				}
				//Reject hashtags with a space
				if(hashTag.contains(" "))
				{
					System.out.println("Sorry, the hashTag contains a space. Please try again");
					continue;
				}
				//Reject hashtags that contain special characters using pattern matching
				mat = inputPattern.matcher(hashTag);
				if(!mat.matches())
				{
					System.out.println("Sorry, the hashTag contains a special character. Please try again");
					continue;
				}
				//Reject hashtags that start with a number
				if(Character.isDigit(hashTag.charAt(0)))
				{
					System.out.println("Sorry, the hashTag starts with a number. Please try again");
					continue;
				}
				
			}
			try
			{
				//Invoking the Twitter API
				InputStream is = new URL("http://search.twitter.com/search.json?rpp=150&q=%23"+hashTag).openStream();
				//Reading the return from the Twitter API into a JsonNode
				JsonNode rootNode = mapper.readTree(is);
				JsonNode results = rootNode.path("results"); 
				itr = 1;
				//Iterating through the records
				while(results.has(itr))
				{
					String text = results.get(itr).get("text").asText();
					mat = urlPattern.matcher(text); //Pattern matching to see if there are any urls in the text
					while (mat.find())
					{
						//URL was found. This handles the case when there is more than one URL in the text
						if(!urlList.contains(mat.group()))
						{
							urlList.add(mat.group());
						}
					}
						
					itr++;
					
				}
				//If there were no records returned
				if(itr == 1)
				{
					System.out.println("Sorry, Twitter did not return any records for #" + hashTag + ". Please try again");
					continue;
				}
				//Printing the output to the console
				else
				{
					System.out.println("\n");
					System.out.println("In the " + itr +" records received from Twitter for the hashtag : #" + hashTag + ", there was/were "+urlList.size() +" unique url(s).");
					System.out.println("Please find them below \n");
					
					for(String str:urlList) 
					{
			            System.out.println(str);
			        }
					
					System.out.println("\n");
					System.out.println("In the " + itr +" records received from Twitter for the hashtag : #" + hashTag + ", there were "+urlList.size() +" unique urls.");
					System.out.println("End of records \n\n");
					
				}	
				
			}
			catch(MalformedInputException mue)
			{
				System.out.println("URL seems to be corrupt. Please try again");
				continue;
			}
			catch(IOException ioe)
			{
				System.out.println("IO Excetion has occurred. Please try again");
				continue;
			}
		}
	}

}
