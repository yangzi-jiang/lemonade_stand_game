
import java.util.Random;
import java.util.*;
import java.math.*;

/** A Lemonade Stand game player that picks moves uniformly at random.
  * 
  * @author Jose Balcazar and Yangzi Jiang
  */
public class Ea2Bot implements Bot {
    
    public int N=3; // Number of players in game
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
      int currentRoundNum = Tournament.player1Moves.size() - 1;
      int y=0; 
      int staticIndex=0;
      for (int k=2; k< (currentRoundNum);k++){
        int num = y^(currentRoundNum-k);
        int denom=0;
        for (int l=2; l<currentRoundNum; l++){
          denom = y^(currentRoundNum-l) + denom;
        }
        int mininimumDistance = calcMinDistance(Tournament.player1Moves.get(k),
            Tournament.player1Moves.get(k-1));  
        staticIndex= staticIndex+ (num/denom) * mininimumDistance;
      }

      return staticIndex * (-1);
    }
    
    /**
     * The follow index f_(i,j) measures whether player i is following player j by looking
     * at the lag-one difference between their respective action sequences.
     * (Sykulski et. al pg 4 top right)
     * 
     */

    public int calcFollowingIndex(){
      int currentRoundNum = Tournament.player1Moves.size() - 1;
      int y=0; 
      int followingIndex=0;
      for (int k=2; k< (currentRoundNum);k++){
        int num = y^(currentRoundNum-k);
        int denom=0;
        for (int l=2; l<currentRoundNum; l++){
          denom = y^(currentRoundNum-l) + denom;
        }
        int oppositeLoc = getOppositeLoc(Tournament.player2Moves.get(k-1));
        int mininimumDistance = calcMinDistance(Tournament.player1Moves.get(k),oppositeLoc);  
        followingIndex= followingIndex+ (num/denom) * mininimumDistance;
      }

      return followingIndex * (-1);
    }

    /**
     * Calculateds the following index of each openent. A way to compare what openent is
     * better to follow
     */
    public int calcFollowIndex(){
      int currentRoundNum = Tournament.player1Moves.size() - 1;
      int y=0; 
      int followIndex=0;
      for (int k=2; k< (currentRoundNum);k++){
        int num = y^(currentRoundNum-k);
        int denom=0;
        for (int l=2; l<currentRoundNum; l++){
          denom = y^(currentRoundNum-l) + denom;
        }
        int oppositeLoc2 = getOppositeLoc(Tournament.player2Moves.get(k-1));
        int oppositeLoc3 = getOppositeLoc(Tournament.player3Moves.get(k-1));
        int minimumDistance2 = calcMinDistance(Tournament.player1Moves.get(k),oppositeLoc2);  
        int minimumDistance3 = calcMinDistance(Tournament.player1Moves.get(k),oppositeLoc3);   
        minimumDistance= Math.min(minimumDistance2, minimumDistance3);
        
        followIndex= followIndex + (num/denom) * mininimumDistance;
      }

      return followIndex * (-1);
    }
    

    public int calcMinDistance(int x, int y){
      return Math.min( 12-x+y , Math.abs(x-y));
    }

    public int getOppositeLoc(int x){
      if ((12- x) > 6){
        return 6 + x;
      }
      else{
        return x-6;
      }
    }

}