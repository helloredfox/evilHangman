package hangman;

import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class EvilHangmanGame implements IEvilHangmanGame {

    @SuppressWarnings("serial")
    public static class GuessAlreadyMadeException extends Exception {

    }



    EvilHangmanGame(String dictionaryFilePath, int wordLength, int guesses)
    {
        //run start game
        File file  = new File(dictionaryFilePath);
        this.numGuessesTotal = guesses;
        startGame(file, wordLength);

    }

    /**
     * Looks through the set of current guesses and returns true if the given word
     * already exists in the set, and returns false otherwise.
     *
     * @param guess The letter guess to be looked for in the set.
     */

    public boolean checkGuess(String guess)
    {
        boolean guessAlreadyMade = false;

        Iterator<String> it = this.getLettersGuessed().iterator();
        while(it.hasNext())
        {
            if(guess == it.next())
            {
                //the guess has already been made
                guessAlreadyMade = true;
                break;
            }
        }
        return guessAlreadyMade;
    }




    /**
     * Starts a new game of evil hangman using words from <code>dictionary</code>
     * with length <code>wordLength</code>.
     *	<p>
     *	This method should set up everything required to play the game,
     *	but should not actually play the game. (ie. There should not be
     *	a loop to prompt for input from the user.)
     *
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     */
    public void startGame(File dictionary, int wordLength)
    {
        //read in the dictionary
        //store the words in a set, but only the words of the given word length
        try
        {
            Scanner scanner = new Scanner(dictionary);
            while(scanner.hasNext())
            {
                String word = scanner.next();
                if(word.length() == wordLength)
                {
                    //add it to the set of words we will use(lower case)
                    this.dictionary.add(word.toLowerCase());
                }
            }
        }
        catch(java.io.FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Make a guess in the current game.
     *
     * @param guess The character being guessed
     *
     * @return The set of strings that satisfy all the guesses made so far
     * in the game, including the guess made in this call. The game could claim
     * that any of these words had been the secret word for the whole game.
     *
     * @throws IEvilHangmanGame.GuessAlreadyMadeException If the character <code>guess</code>
     * has already been guessed in this game.
     */
    public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException
    {
        // RETURNS THE SET OF WORDS THAT SATISFY ALL THE GUESSES MADE SO FAR.

        //the guess is a character that is a valid guess(lowercase character) that hasn't been guessed yet


        //set to return
        Set<String> setOfNewPossibleWords = new HashSet<>();


                //logic to partition the remaining words into a set, based on their patterns.

                //go through words and generate patters
                Map <String, Set<String>> patternMap = new HashMap<String, Set<String>>();

                for(String word : this.dictionary)
                {
                    //generate a pattern for the given word, and store it in the map
                    Pattern pattern = new Pattern(guess, word);
                    Set<String> wordSet = new HashSet<>();
                    wordSet.add(word);
                    String patternStringKey = pattern.getOrderString();
                    if(patternMap.get(patternStringKey) == null)
                    {
                        patternMap.put(patternStringKey, wordSet);
                    }
                    else
                    {
                        patternMap.get(patternStringKey).add(word);
                    }

                }

                //now we have a map with the patterns and stuff. Choose the "best" one

         System.out.println(patternMap.keySet());

                int largestSetSize = 0;
                String largestSetSizeKey = "";

        //find a set that with the largest number of entries
        for(Map.Entry <String, Set<String>> entry : patternMap.entrySet())
        {
                String patternString = entry.getKey();
                Set<String> associatedWords = entry.getValue();

                //find largest set
            if(associatedWords.size() > largestSetSize)
            {
                //change largest set size
                largestSetSize = associatedWords.size();
                largestSetSizeKey = patternString;
            }
        }
//now see if there are any other sets of the same size

        Set<String> largestSetKeys = new HashSet<>();
        largestSetKeys.add(largestSetSizeKey);


        for(Map.Entry <String, Set<String>> entry : patternMap.entrySet())
        {
            String patternString = entry.getKey();
            Set<String> associatedWords = entry.getValue();

            //find largest set
            if(associatedWords.size() == largestSetSize)
            {
               //add to the largestSetKeys set
                largestSetKeys.add(patternString);
            }
        }

//if there are multiple sets of the same size, we have to check something else
        if(largestSetKeys.size() > 1)
        {
            //check the next condition
            //check number of instances of the guess in each possible key

            int largestNumInstances = 0;
            String largestInstancePattern = "";

            for(String s : largestSetKeys)
            {
                int currentNumInstances = getNumInstances(s, guess);

                if(currentNumInstances > largestNumInstances)
                {
                    largestNumInstances = currentNumInstances;
                    largestInstancePattern = s;
                }
            }

            //now we have a largest instance key, and we need to see if there are others

            Set<String> wordsWithSameNumInstances = new HashSet<>();

            for(String s : largestSetKeys)
            {
                int currentNumInstances = getNumInstances(s, guess);

                if(currentNumInstances == largestNumInstances)
                {
                    wordsWithSameNumInstances.add(s);
                }
            }

            //now we have a set of keys with the same number of instances of the guess and that have the same number of words in their corresponding set in the map

            if(wordsWithSameNumInstances.size() > 1)
            {
                //check for right-most guess



            }
            else
            {
                setOfNewPossibleWords = patternMap.get(largestInstancePattern);
            }

        }
        else
        {
            //use the largest set
            setOfNewPossibleWords = patternMap.get(largestSetSizeKey);

        }

        return setOfNewPossibleWords;
    }


    public Set <String> getLettersGuessed()
    {
        return this.lettersGuessed;
    }

    // counts anything that matches the given char as an instance
    public int getNumInstances(String s, char c)
    {
        int numInstances = 0;

        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == c)
            {
                numInstances++;
            }

        }
        return numInstances;
    }

    //data memebers
    private Set <String> dictionary = new HashSet<String>();
    private int numGuessesTotal = 0;
    private int numGuessesMade = 0;
    private Set <String> lettersGuessed = new HashSet<String>();





}


