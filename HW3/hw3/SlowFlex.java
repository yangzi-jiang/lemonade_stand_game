
import java.util.Random;
import java.util.*;

/** A Lemonade Stand game player that picks moves uniformly at random and then stick.
 *  There are two defensive mechanisms, against other sticky players and sandwich attacks.
  * 
  * @author RR
  */
public class SlowFlex implements Bot {

    public static ArrayList<Integer> player1Moves = new ArrayList<Integer>(); 
	public static ArrayList<Integer> player2Moves = new ArrayList<Integer>();
    public static ArrayList<Integer> myMoves = new ArrayList<Integer>();
    public static ArrayList<Integer> myScores = new ArrayList<Integer>();
    private int currentRoundNum = 0; // the number of round we are in

    int numRoundsCheck = 1000; // check per numRoundsCheck

    private boolean repositioned = false;
    private int myPreviousStick = 0;
    private Double myPreviousStickScore = 0.0;

    private static Random generator = new Random();

    // Initialize a random int to stick with
    static int randomStick = generator.nextInt(12) + 1;
    
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

        if(myMoves.size() > 0){
            int lastRoundScore = scoreRound(myMoves.get(myMoves.size()-1), player1LastMove, player2LastMove);
            // System.out.println("My score last round is " + lastRoundScore);
            myScores.add(lastRoundScore);
        }
        
        // Change macro parameter, how often we check based on our performances
        if(currentRoundNum % 10000 == 0 && currentRoundNum > 1 && numRoundsCheck > 20){
            double totalAvg = myAvgScore(10000);
            if(totalAvg < 7.65){
                numRoundsCheck = numRoundsCheck / 3;
            }
            if(totalAvg < 7.30){
                numRoundsCheck = numRoundsCheck / 3;
            }
            if(totalAvg < 6.95){
                numRoundsCheck = numRoundsCheck / 3;
            }
            if(totalAvg < 6.60){
                numRoundsCheck = numRoundsCheck / 3;
            }
            if(numRoundsCheck < 2){
                numRoundsCheck = 2;
            }
            // System.out.println("num rounds check is: " + numRoundsCheck);
        }
        else if(currentRoundNum % 20000 == 0 && currentRoundNum > 1 && numRoundsCheck < 2000){
            double totalAvg = myAvgScore(10000);
            if(totalAvg < 7.65){
                numRoundsCheck = numRoundsCheck * 4;
            }
            if(totalAvg < 7.30){
                numRoundsCheck = numRoundsCheck * 4;
            }
            if(totalAvg < 6.95){
                numRoundsCheck = numRoundsCheck * 4;
            }
            if(totalAvg < 6.60){
                numRoundsCheck = numRoundsCheck * 4;
            }
            if(numRoundsCheck > 5000){
                numRoundsCheck = 5000;
            }
            // System.out.println("num rounds check is: " + numRoundsCheck);
        }


        Double myCurrentAvg = 0.0;

        // Defense Mechanism - sandwiched
        // Different than the first mechanism, as soon as a sandwich happens after 200 rounds, we panick
        if((currentRoundNum > 200) && (sandwiched(player1LastMove, player2LastMove))){
            // System.out.println("Warning: We are being sandwiched!");
            double rand = generator.nextDouble();
            if(rand >= 0.8){
                int nextRandomStick = generator.nextInt(12) + 1; // Change stick number
                if(randomStick != nextRandomStick){
                    randomStick = nextRandomStick;
                }
            }
            // Counter sandwich - hard
            else if(rand <= 0.4){
                // System.out.println("Player 1 is at " + player1LastMove);
                // System.out.println("Player 2 is at " + player2LastMove);
                randomStick = (myMoves.get(myMoves.size()-1) + 2) % 12;
                if(randomStick == 0){
                    randomStick = 12;
                }
                // System.out.println("My next move is at " + randomStick);
            }
            // Counter sandwich - soft
            else{
                // System.out.println("Player 1 is at " + player1LastMove);
                // System.out.println("Player 2 is at " + player2LastMove);
                randomStick = (myMoves.get(myMoves.size()-1) + 3) % 12; // 
                if(randomStick == 0){
                    randomStick = 12;
                }
                // System.out.println("My next move is at " + randomStick);
            }

            currentRoundNum++;
            myMoves.add(randomStick);
            return randomStick;
        }

        // After repositioned for 5 rounds, check is reposition is better than before
        if(repositioned && (currentRoundNum % 5) == 0){
            if(myScores.get(myScores.size() - 1) < myPreviousStickScore){
                randomStick = myPreviousStick;
            }
            else{
                repositioned = false; // If better, we are good to go
            }
        }

        // Thresholds - checking for possible marginal increments every 100 rounds 
        if((currentRoundNum % numRoundsCheck) == 0 && currentRoundNum > 2000){       
            // Avg utility over the last 100 rounds
            myCurrentAvg = myAvgScore(numRoundsCheck);

            // Don't move if payoff is at 11
            if(myCurrentAvg < 11){
                // Change stick number if not too satisfied with current, but as 
                // myCurrentAvg gets better, we are less likely to make moves, because of this inverse squared function
                double diff = myCurrentAvg - 7.70;
                // Avoid dividing by 0
                if(diff <= 0 || generator.nextDouble() < 0.4 * (0.05 / Math.pow(diff, 2))){ // 100% at 7.9; 44% at 8.0
                    int nextRandomStick = generator.nextInt(12) + 1; 
                    if(randomStick == nextRandomStick){
                        nextRandomStick = generator.nextInt(12) + 1;;
                    }
                    randomStick = nextRandomStick;
                    repositioned = true;
                    myPreviousStick = myMoves.get(myMoves.size() - 1);
                    myPreviousStickScore = myCurrentAvg;
                }
            }
        }

        // Pickup a sandwich
        if(currentRoundNum > 2000){
            if((player1LastMove - player2LastMove) == 1)
                randomStick = player1LastMove + 1;
            if((player2LastMove - player1LastMove) == 1)
                randomStick = player2LastMove + 1;
            if((player1LastMove - player2LastMove) == -11)
                randomStick = player1LastMove + 1;
            if((player2LastMove - player1LastMove) == -11)
                randomStick = player2LastMove + 1;
        }
        
        if(randomStick == 13){
            randomStick = 1;
        }
        if(randomStick == 0){
            randomStick = 12;
        }

        // System.out.println("The round is " + currentRoundNum + " with the current stick is " + randomStick);  
        myMoves.add(randomStick);
        currentRoundNum++;
        return randomStick;
    }
    
}