package hangman;


public class Pattern {
    StringBuilder order;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    Pattern(char guess, String word)
    {
        //given the character and a word, generate a pattern for it.
        //loop through the string, where there is a instance of guess, leave it, and where there isn't, replace it with a dash
        StringBuilder wordPattern = new StringBuilder();

        for(int i = 0; i < word.length(); i++)
        {
            if(word.charAt(i) != guess)
            {
               wordPattern.append("-");
            }
            else
            {
                wordPattern.append(guess);
            }
        }
        order = wordPattern;
    }

    public String getOrderString()
    {
        return this.order.toString();
    }

    public void addPattern()
    {

    }
}
