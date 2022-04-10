
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GenreGuesser {

	public static void main(String[] args) throws IOException{

		String[] filesDirectoryList = getFilesForSupervisedLearning();

		String[] filesDirectoryListFullPath = new String[filesDirectoryList.length];

		for (int i = 0; i < filesDirectoryList.length; i++) {
			filesDirectoryListFullPath[i] = "C:\\Users\\Liam\\eclipse-workspace\\Video Game Genre Guesser\\IGDB Guess The Genre\\Genre Storylines + Names + IDs\\" + filesDirectoryList[i];
		}

		String[] trainingDatabase = StoreGenreStorylineAndGameTitleAsText(filesDirectoryListFullPath);

		String[] cleanedTrainingDatabase = CleanTrainingDatabase(trainingDatabase);

		for (int i = 0; i < cleanedTrainingDatabase.length; i++) {
			System.out.println(cleanedTrainingDatabase[i]);
			System.out.println(filesDirectoryList[i]);
			CreateUnigramCountFileForEachGenre(cleanedTrainingDatabase[i], filesDirectoryList[i]);
		}
		
		doTheMaths();

	}

	public static String[] getFilesForSupervisedLearning() throws IOException {
		String filesLocationAsString = "C:\\Users\\Liam\\eclipse-workspace\\Video Game Genre Guesser\\IGDB Guess The Genre\\Genre Storylines + Names + IDs";

		// Creates an array in which we will store the names of files and directories
		String[] pathnames;

		// Creates a new File instance by converting the given pathname string
		// into an abstract pathname
		File f = new File(filesLocationAsString);

		// Populates the array with names of files and directories
		pathnames = f.list();


		/* For each pathname in the pathnames array
		for (String pathname : pathnames) {
			// Print the names of files and directories
			System.out.println(pathname); */

		return pathnames;
	}

	public static String[] StoreGenreStorylineAndGameTitleAsText(String [] fileList) throws IOException {

		String [] trainingDatabase = new String [fileList.length];

		for (int i = 0; i < fileList.length; i++) {

			InputStream is = new FileInputStream(fileList[i]);
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));

			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();

			while(line != null){
				sb.append(line).append("\n");
				line = buf.readLine();
			}

			String fileAsString = sb.toString();
			trainingDatabase[i] = fileAsString;
		}

		return trainingDatabase;

	}

	public static String[] CleanTrainingDatabase(String[] trainingDB) throws IOException {

		for (int i = 0; i < trainingDB.length; i++) {
			trainingDB[i] = trainingDB[i].replaceAll("\"id\": [0-9]{1,10},\n" + 
					"    \"genres\": \\[\n" + 
					"      [0-9]{1,2}\n" + 
					"    ],\n" + 
					"    \"name\": \"", "");

			trainingDB[i] = trainingDB[i].replaceAll("\",\n" + 
					"    \"storyline\": \"", " ");

			trainingDB[i] = trainingDB[i].replaceAll("  \\},\n" + 
					"  \\{", "");

			trainingDB[i] = trainingDB[i].replaceAll("  \\}\n" + 
					"]", "");

			trainingDB[i] = trainingDB[i].replaceAll("\\[\n" + 
					"  \\{","");

			trainingDB[i] = trainingDB[i].replaceAll("\n","");

			trainingDB[i] = trainingDB[i].replaceAll("    ","");

			trainingDB[i] = trainingDB[i].replaceAll("  "," ");

			trainingDB[i] = trainingDB[i].replaceAll("   "," ");

		}

		return trainingDB;
	}

	public static void CreateUnigramCountFileForEachGenre(String content, String fileName) throws IOException {

		ArrayList<String> unigramsCharacter = new ArrayList<String>(); 
		ArrayList<Integer> unigramsCount = new ArrayList<Integer>(); 

		int ngramSize = 2;
		
		//System.out.println(content.length());
		//System.out.println(content);

		for (int i = 0; i < content.length()-ngramSize; i = i + 1) {

			String currentChar = content.substring(i, i+ngramSize);
			//System.out.println(currentChar);
			if (unigramsCharacter.contains(currentChar)){
				int indexOfChar = unigramsCharacter.indexOf(currentChar);
				unigramsCount.set(indexOfChar, (unigramsCount.get(indexOfChar) + 1));
			}
			else {
				unigramsCharacter.add(currentChar);
				int indexOfChar = unigramsCharacter.indexOf(currentChar);
				unigramsCount.add(0);
				unigramsCount.set(indexOfChar, (unigramsCount.get(indexOfChar) + 1));
			}

		}

		//System.out.println(unigramsCharacter.size());

		PrintWriter outputUnigramFile = new PrintWriter (new FileWriter("C:\\Users\\Liam\\eclipse-workspace\\Video Game Genre Guesser\\Bigrams\\" + fileName));

		for (int i = 0; i <unigramsCharacter.size(); i++) {		
			outputUnigramFile.println(unigramsCharacter.get(i).toString() + "LiamIsTheBestAround1997" + unigramsCount.get(i).toString());
		}

		outputUnigramFile.close();

	}




	public static void doTheMaths () throws IOException {

		boolean firstFile = false;
		boolean secondFile = false;
		
		
		Scanner scanner1 = new Scanner( new File("C:\\Users\\Liam\\eclipse-workspace\\Video Game Genre Guesser\\Bigrams\\Your StoryLine And Game Title To Guess.txt") );
		String bigram1 = scanner1.useDelimiter("\\A").next();
		scanner1.close();

		// Get every other file and compare
		String filesLocationAsString = "C:\\Users\\Liam\\eclipse-workspace\\Video Game Genre Guesser\\Bigrams";

		// Creates an array in which we will store the names of files and directories
		String[] pathnames;

		// Creates a new File instance by converting the given pathname string
		// into an abstract pathname
		File f = new File(filesLocationAsString);

		// Populates the array with names of files and directories
		pathnames = f.list();
		
		for (int i = 0; i < pathnames.length; i++) {
			pathnames[i] = "C:\\Users\\Liam\\eclipse-workspace\\Video Game Genre Guesser\\Bigrams\\" + pathnames[i];
		}
		
		

		for (int i = 0; i < pathnames.length; i++) {
			System.out.print("Your Game & " + pathnames[i] + " = ");
			Scanner scanner2 = new Scanner( new File(pathnames[i]) );
			String bigram2 = scanner2.useDelimiter("\\A").next();
			scanner2.close();


			String[]t1 = bigram1.split("LiamIsTheBestAround1997|\r");
			String[]t2 = bigram2.split("LiamIsTheBestAround1997|\r");


			double s1 = 0;
			double s2 = 0;
			double s3 = 0;
			double s4 = 0;

			for(int a = 1; a < t1.length; a=a+2) {
				s1 = s1 + Double.parseDouble(t1[a]);
			}

			// counting total tokens as opposed to total unique tokens 
			for(int b = 1; b < t2.length; b=b+2) {
				s2 = s2 + Double.parseDouble(t2[b]);
			} 
			// counting total unique tokens as opposed to total tokens 
			for(int c = 1; c < t1.length; c=c+2) {
				s3++;
			}

			for(int d = 1; d < t2.length; d=d+2) {
				s4++;
			}

			//System.out.println(s1);
			//System.out.println(s2);

			double totaltokens = s1 + s2; //all token total
			double ttt = s3 + s4; //unique token total
			double answerTotal = 0; //final answer

			//get first token from file one
			for(int e = 0; e < t1.length-1; e=e+2) {
				//loop through each token in file two to find exact match if any exist
				for (int j = 0; j < t2.length-1; j=j+2) {
					//if token i1 matches token j2
					if (t1[e].equals(t2[j]))
					{
						double occurances1 = Integer.parseInt(t1[e+1]); //occurrences of token i in file one
						double occurances2 = Integer.parseInt(t2[j+1]); //occurrences of token i in file two
						double occurancesCombined = occurances1 + occurances2; // combined occurrences of token i in both files
						double eval1 = occurancesCombined * (s1/totaltokens); // estimated value of i in file one
						double eval2 = occurancesCombined * (s2/totaltokens); // estimated value of i in file two
						double occurrences1MinusEval1ThenSquared = (occurances1 - eval1) * (occurances1 - eval1); //see name
						double occurrences2MinusEval2ThenSquared = (occurances2 - eval2) * (occurances2 - eval2); //see name
						double final1 = occurrences1MinusEval1ThenSquared / eval1; //part one of TTT sum equation
						double final2 = occurrences2MinusEval2ThenSquared / eval2; //part two of TTT sum equation
						double final3 = final1 + final2; // add part1 and part2
						answerTotal = answerTotal + final3; // sum for each k
						/*if(final3 > 100) {
						System.out.print(filename1 + " " + filename2 + " = " + t1[i] + " - ");
						System.out.println(final3);

					} */
						//System.out.println(t1[i] + " = " + final3); 
					}
				}
			} 
			System.out.print((answerTotal = answerTotal/(ttt-1)) + "\n"); //sum divided by degrees of freedom


		}
	} 

}




