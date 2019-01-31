package hangman;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
public class Main {


    public static char getValidNewGuess(EvilHangmanGame game)
    {
        // get a guess from the user

        String guessOrGuesses = "guesses";
        if(game.getNumGuessesTotal() == 1)
        {
            guessOrGuesses = "guess";
        }
        Scanner scanner = new Scanner(System.in);
        String lettersGuessed = game.getLettersGuessedString();
        System.out.println("You have " + game.getNumGuessesTotal() + " " + guessOrGuesses + " left");
        System.out.println("Used Letters: " + lettersGuessed);
        //show all the guess letters

        System.out.println("Word: " + game.getCurrentlyGuessedWordRepresentation());

        System.out.println("Enter guess: ");
        String letterGuessed = scanner.nextLine();

        boolean check = game.checkGuess(letterGuessed);

        while(!(letterGuessed.matches("[a-zA-Z]{1}")) || game.checkGuess(letterGuessed))
        {
            // if the letter guessed isn't 1 upper or lowercase letter, prompt again
            letterGuessed = letterGuessed.toLowerCase();
            //if it is a guess that has already been made
            if(game.checkGuess(letterGuessed))
            {
                //THIS IS NOT PRINTING
                System.out.println("You already used that letter");
            }
            else
            {
                System.out.println("Invalid input");
            }


            scanner = new Scanner(System.in);
            System.out.println("You have " + game.getNumGuessesTotal() + " " + guessOrGuesses +" left");
            System.out.println("Used Letters: " + lettersGuessed);
            //show all the guess letters

            System.out.println("Word: " + game.getCurrentlyGuessedWordRepresentation());

            System.out.println("Enter guess: ");
            letterGuessed = scanner.nextLine();
        }

        //here we do the logic
        //convert the single letter string guess into  a char
        letterGuessed = letterGuessed.toLowerCase();
        char letterGuess = letterGuessed.charAt(0);

        return letterGuess;
    }


    public static void main(String[] args)
    {
        String filepath = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        EvilHangmanGame game = new EvilHangmanGame(filepath, wordLength, guesses);

        File file  = new File(filepath);

        game.startGame(file, wordLength);

        /*
        Code to test the findRightMostPatter() method

         */
//        ArrayList<String> patterns = new ArrayList<>();
//
//        patterns.add("e--e---e");
//        patterns.add("e----e-e");
//        patterns.add("---e--ee");
//        patterns.add("ee----e-");
//        patterns.add("---e-ee-");
//
//       String rightMostPattern =  game.findRightMostPattern(patterns);
//
//        System.out.println("This is the right most pattern: \"" + rightMostPattern + "\"");

        while(game.getNumGuessesTotal() > 0)
        {

            boolean foundDash = false;
            //before you make a new guess, make sure the word isn't completely guessed
            for(int i = 0; i < game.getCurrentlyGuessedWordRepresentation().length(); i++)
            {
                if(game.getCurrentlyGuessedWordRepresentation().charAt(i) == '-')
                {
                    foundDash = true;
                }
            }

            if(!foundDash)
            {
                System.out.println("You win! The word was: " + game.getCurrentlyGuessedWordRepresentation());
                break;
            }

            char guess = getValidNewGuess(game);

            try
            {
                    Set<String> newWords =  game.makeGuess(guess);
                boolean lettersAdded = game.updateCurrentWordGuesses(game.getMostRecentPattern());
                if(!lettersAdded)
                {
                    System.out.println("Sorry, there are no " + guess + "'s");
                    game.decrementNumGuessesTotal();
                }
                    game.setNewWordsAsDictionary(newWords);


            }
            catch(IEvilHangmanGame.GuessAlreadyMadeException e)
            {
                e.printStackTrace();
            }

        }


        if(game.getNumGuessesTotal() == 0)
        {
            System.out.println("You lose!");
            System.out.println("The word was: " + game.getRandomWord());
        }





    }
}
