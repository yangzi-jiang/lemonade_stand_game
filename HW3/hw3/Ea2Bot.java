
import java.util.Random;

/** A Lemonade Stand game player that picks moves uniformly at random.
  * 
  * @author Jose Balcazar and Yangzi Jiang
  */
public class Ea2Bot implements Bot {
    
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
        
        return 1;
    }

    public int calcStaticIndex(){
      return 1;
    }
    
}