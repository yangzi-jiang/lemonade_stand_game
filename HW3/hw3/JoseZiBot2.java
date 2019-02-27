
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
    private int currentRoundNum = 0; // the number of round we are in
    private int roundsCounter = 0; // for internal use
    
    private static double threshold = 7.75; // Defensive threshold
    private ArrayList<Double> attackThresholds = new ArrayList<Double>();
    

    private static Random generator = new Random();

    // Initialize a random int to stick with
    static int randomStick = generator.nextInt(12) + 1;
    
    private boolean belowthreshold(double myCurrentAvg){
        if(myCurrentAvg <= threshold){
            return true;
        }
        return false;
    } 

    private void addAttackThresholds(double myCurrentAvg){
        for(double i = myCurrentAvg; i < 12; i = i + 0.2){
            attackThresholds.add(i);
        }
    }
    private double attackthreshold(double myCurrentAvg){
        double levelOfThreshold = 0;
        addAttackThresholds(myCurrentAvg);
        for(int i = 0; i < attackThresholds.size() - 1; i++){
            if(myCurrentAvg > attackThresholds.get(i)){
                levelOfThreshold = attackThresholds.get(i);
            }
        }
        // System.out.println("Current attack levelOfThreshold " + levelOfThreshold);
        return levelOfThreshold;
    } 
    
    // Detected whether we are suffering sandwich attack
    private boolean sandwiched(int player1LastMove, int player2LastMove){
        int myLastMove = myMoves.get(currentRoundNum - 1);
        int breadOne = player1LastMove - myLastMove;
        int breadTwo = player2LastMove - myLastMove;

        // Are we sandwiched?
        if((breadOne == 1 || breadOne == -11) && (breadTwo == -1 || breadTwo == 11)){
            return true;
            //return 1; // player 1 is to our right, player 2 is to our left
        }

        if((breadTwo == 1 || breadTwo == -11) && (breadOne == -1 || breadOne == 11)){
            return true;
            //return 2;  // player 2 is to our right
        }
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
            // Avg utility over the last 100 rounds
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

        // Defense Mechanism #2 - sandwiched
            // -1: no sandwich; 1: player 1 is to our right; 2; player 2 is to our right
            // int sandwich = sandwiched(player1LastMove, player2LastMove);
        // Different than the first mechanism, as soon as a sandwich happens after 100 rounds, we panick
        if((currentRoundNum > numRoundsCheck) && (sandwiched(player1LastMove, player2LastMove))){
            // System.out.println("Warning: We are being sandwiched!");

            if(generator.nextDouble() >= 0.5){
                int nextRandomStick = generator.nextInt(12) + 1; // Change stick number
                if(randomStick != nextRandomStick){
                    randomStick = nextRandomStick;
                }
            }
            // // Counter sandwich - Attack!
            // else{
            //     // Counter sandwich player 1
            //     if(generator.nextDouble() >= 0.5){
            //         if(sandwich == 1){ // player 1 is to my right
            //             int playerOne = (player1LastMove % 12) + 1;
            //             randomStick = playerOne + 1;
            //         }
            //         else{
            //             int playerOne = (player1LastMove % 12) - 1;
            //             randomStick = playerOne - 1;
            //         }
            //     }
            //     // Counter sandwich player 2
            //     else{
            //         randomStick = player2LastMove + 1;
            //     }
            // }

            roundsCounter = 0;
        }

        // Attack Mechanism #1 - checking for possible marginal increments every 800 rounds 
        if((currentRoundNum % (2 * numRoundsCheck) == 0) && roundsCounter > numRoundsCheck){       
            // Avg utility over the last 100 rounds
            myCurrentAvg = myAvgScore(numRoundsCheck); 
            // attack mechanism
            if(attackthreshold(myCurrentAvg) != 12){

                // Change stick number if not too satisfied with current, as myCurrentAvg gets better, we are less likely to make moves
                if((generator.nextDouble() * myCurrentAvg) / 10 <= 0.33){ 
                    int nextRandomStick = generator.nextInt(12) + 1; 
                    if(randomStick != nextRandomStick){
                        randomStick = nextRandomStick;
                    }
                    roundsCounter = 0;
                }
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