
import java.util.Random;
import java.util.*;

/** A Lemonade Stand game player that picks moves uniformly at random and then stick.
 *  There are two defensive mechanisms, against other sticky players and sandwich attacks.
  * 
  * @author RR
  */
public class JoseZiBot2 implements Bot {

    private ArrayList<Integer> player1Moves = new ArrayList<Integer>(); 
	private ArrayList<Integer> player2Moves = new ArrayList<Integer>();
    private ArrayList<Integer> myMoves = new ArrayList<Integer>();
    private ArrayList<Integer> myScores = new ArrayList<Integer>();
    private int roundNum = 0;
    private static double threshhold = 7.8;
    
    private static Random generator = new Random();

    // Initialize a random int to stick with
    static int randomStick = generator.nextInt(12) + 1;
    
    private boolean belowThreshhold(double myCurrentAvg){
        if(myCurrentAvg <= threshhold){
            return true;
        }
        return false;
    } 
    
    // Detected whether we are suffering sandwich attack
    private boolean sandwiched(int player1LastMove, int player2LastMove){
        return false;
    }

    /*
        for (int i = 0; i < numRounds; i++){
            if(args[i] == "JoseZiBot.java"){}
            int whichPlayerAreWe = i; // Player 0, 1, or 2
        }
    */

    /** Scores the current round from the perspective of the first player.
      * 
      * @param action1 the action taken by the first player.
      * @param action2 the action taken by the second player.
      * @param action3 the action taken by the third player.
      * 
      * @return the payoff for player 1 on this round.
      */
    public int scoreRound(int action1, int action2, int action3) {
        if ((action1 == action2) && (action1 == action3))
            return 8; // three-way tie
        else if ((action1 == action2) || (action1 == action3)) {
            return 6; // two-way tie
        }
        else {
            int score = 0;
            int i = action1;
            while ((i != action2) && (i != action3)) { // score clockwise
                i = (i % 12) + 1;
                score += 1;
            }
            i = action1;
            while ((i != action2) && (i != action3)) { // score anti-clockwise
                i = (i-1 > 0) ? i-1 : 12;
                score += 1;
            }
            return score;
        }
    }

    public Double myAvgScore(int lastNumOfRounds){
        
        if(roundNum <= lastNumOfRounds){
            lastNumOfRounds = roundNum;
        }
        
        int totalScore = 0;
        Double myAvgScore = 0.0;
        for(int i = roundNum; i >= roundNum - lastNumOfRounds; i--){
            totalScore = totalScore + myScores.get(i);
        }

        System.out.println("My avg score over the last" + lastNumOfRounds + " is : " + myAvgScore);
        return myAvgScore;
    }

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
        player1Moves.add(player1LastMove);
        player2Moves.add(player2LastMove);
        roundNum++;

        int lastRoundScore = scoreRound(this.myMoves.get(this.myMoves.size()-1), player1LastMove, player2LastMove);
        myScores.add(lastRoundScore);

        // Avg utility over the last 200 rounds
        Double myCurrentAvg = myAvgScore(200); 

        // Defensive mechanism
        if(belowThreshhold(myCurrentAvg) || sandwiched(player1LastMove, player2LastMove)){
            randomStick = generator.nextInt(12) + 1;
        }

        System.out.println("The current stick is " + randomStick);
        
        myMoves.add(randomStick);
        
        return randomStick;
    }
    
}