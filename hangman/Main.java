package hangman;

import java.util.Iterator;
import java.util.Scanner;

public class Main {


    public static char getValidNewGuess(EvilHangmanGame game)
    {
        // get a guess from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Guess a letter: ");
        String letterGuessed = scanner.nextLine();

        while(!(letterGuessed.matches("[a-zA-Z]{1}")))
        {
            // if the letter guessed isn't 1 upper or lowercase letter, prompt again

            //if it is a guess that has already been made
            if(game.checkGuess(letterGuessed))
            {
                System.out.println("You already used that letter");
            }
            else
            {
                System.out.println("Invalid input");
            }
            scanner = new Scanner(System.in);
            System.out.println("Guess a letter: ");
            letterGuessed = scanner.nextLine();
        }

        //here we do the logic
        //convert the single letter string guess into  a char
        char letterGuess = letterGuessed.charAt(0);

        return letterGuess;
    }


    public static void main(String[] args)
    {
        String filepath = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        EvilHangmanGame game = new EvilHangmanGame(filepath, wordLength, guesses);

        char guess = getValidNewGuess(game);

        try
        {
            game.makeGuess(guess);
        }
        catch(IEvilHangmanGame.GuessAlreadyMadeException e)
        {
            e.printStackTrace();
        }



    }
}
