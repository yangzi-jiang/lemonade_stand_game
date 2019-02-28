
import java.util.Random;
import java.util.*;

/** A Lemonade Stand game player that picks moves uniformly at random and then stick.
 *  There are two defensive mechanisms, against other sticky players and sandwich attacks.
  * 
  * @author RR
  */
public class JoseZiBotDef implements Bot {

    private ArrayList<Integer> player1Moves = new ArrayList<Integer>(); 
	private ArrayList<Integer> player2Moves = new ArrayList<Integer>();
    private ArrayList<Integer> myMoves = new ArrayList<Integer>();
    private ArrayList<Integer> myScores = new ArrayList<Integer>();
    private int currentRoundNum = 0; // the number of round we are in
    private int roundsCounter = 0; // for internal use
    
    private static double threshold = 7.75; // Defensive threshold    

    private static Random generator = new Random();

    // Initialize a random int to stick with
    static int randomStick = generator.nextInt(12) + 1;
    
    private boolean belowthreshold(double myCurrentAvg){
        if(myCurrentAvg <= threshold){
            return true;
        }
        return false;
    }

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
        
        // if(currentRoundNum <= lastNumOfRounds){
        //     lastNumOfRounds = currentRoundNum;
        // }
        
        int totalScore = 0;
        Double myAvgScore = 0.0;
        for(int i = currentRoundNum - 1; i > currentRoundNum - lastNumOfRounds; i--){
            totalScore = totalScore + myScores.get(i);
        }

        myAvgScore = (double) totalScore/lastNumOfRounds;
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
        int numRoundsCheck = 400; // check, we stay at one place during the first 400 turns.

        if(this.myMoves.size() > 0){
            int lastRoundScore = scoreRound(this.myMoves.get(this.myMoves.size()-1), player1LastMove, player2LastMove);
            // System.out.println("My score last round is " + lastRoundScore);
            myScores.add(lastRoundScore);
        }
        
        Double myCurrentAvg = 0.0;

        // Defense Mechanism #1 - bad luck
        // Start calculating avg ultility once we have more than 400 records, ensure no overcalculating
        if((currentRoundNum % numRoundsCheck == 0) && roundsCounter > numRoundsCheck){       
            // Avg utility over the last 400 rounds
            myCurrentAvg = myAvgScore(numRoundsCheck); 
            // Defensive mechanism
            if(belowthreshold(myCurrentAvg)){
                int nextRandomStick = generator.nextInt(12) + 1; // Change stick number
                if(randomStick != nextRandomStick){
                    randomStick = nextRandomStick;
                }
                roundsCounter = 0;
            }
        }

        currentRoundNum++;
        roundsCounter++;

        // System.out.println("My avg score over the last 400 rounds is : " + myCurrentAvg);
        // System.out.println("The round is " + currentRoundNum + " with the current stick is " + randomStick);  
        myMoves.add(randomStick);
        return randomStick;

        // System.out.println("player 1 made # moves: " + player1Moves.size());
    }
    
}