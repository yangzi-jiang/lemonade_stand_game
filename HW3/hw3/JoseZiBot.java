
import java.util.Random;

/** A Lemonade Stand game player that picks moves uniformly at random.
  * 
  * @author RR
  */
public class JoseZiBot implements Bot {
    private static Random generator = new Random();

    // Initialize a random int to stick with
    static int randomStick = generator.nextInt(12) + 1;
    
    // public boolean threshhold(){
    //     return Tournament.ThresholdReached;
    // } 
    
    // // Detected whether we are suffering sandwich attack
    // public boolean sandwiched(){
    //     return false;
    // }

    /*
        for (int i = 0; i < numRounds; i++){
            if(args[i] == "JoseZiBot.java"){}
            int whichPlayerAreWe = i; // Player 0, 1, or 2
        }
    */

    /** Returns an action according to the mixed strategy that picks among 
      * actions uniformly at random.
      * 
      * @param player1LastMove the action that was selected by Player 1 on the
      *                        last round.
      * @param player2LastMove the action that was selected by Player 2 on the
      *                        last round.
      * 
      * @return the next action to play.
      */
    public int getNextMove(int player1LastMove, int player2LastMove) {

        // // Defensive mechanism
        // if(threshhold() || sandwiched()){
        //     randomStick = generator.nextInt(12) + 1;
        // }

        return randomStick;
    }
    
}