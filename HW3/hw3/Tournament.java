import java.util.HashMap;
import jdk.jfr.Threshold;
import java.util.*;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/** Program that runs a Lemonade Stand tournament between the three named
  * players.
  * 
  * @author RR
  */
public class Tournament {
    
    public static void main(String[] args) throws Exception{
        if (args.length != 4) {
            System.out.print("Usage: java Tournament <player1> <player2> ");
            System.out.println("<player3> <n>");
            System.out.println("where <player1> = class name of first bot.");
            System.out.println("      <player2> = class name of second bot.");
            System.out.println("      <player3> = class name of third bot.");
            System.out.println("      <n>       = number of rounds to play.");
            System.out.println("Example:");
            System.out.print("java Tournament RandomBot FiveOClockBot ");
            System.out.println("RandomBot 10000");
            System.exit(-1);
        }
        
        // Determine number of rounds to run
        int numRounds = 0;
        try {
            numRounds = Integer.parseInt(args[3]);
        }
        catch (Exception e) {
            System.out.println("Error: invalid value for num rounds.");
            System.exit(-1);
        }
        
        // Instantiate players
        Bot[] players = {null, null, null};
        try {
            for (int i = 0; i < 3; i++)
                players[i] = (Bot)Class.forName(args[i]).newInstance();
        }
        catch (Exception e) {
            System.out.println("Error: one or more named bots could not be " +
                               "loaded.");
            System.out.println("Double check that your bots have been " + 
                               "compiled and that the .class files are in " + 
                               "the current directory.");
            System.exit(-1);
        }
        
        // Run tournament
        Arbiter judge = new Arbiter(players[0], players[1], players[2]);

        for (int i = 0; i < numRounds; i++)
            judge.runRound();
            
       

        // Print mean scores
        int[] scores = judge.getCurrentScore();
        for (int i = 0; i < 3; i++)
            System.out.println(args[i] + ": " + (scores[i]/(double)numRounds));

        // Print out all moves and scores
        PrintWriter pw = new PrintWriter(new File("/Users/yangzijiang/Desktop/LemonadeStandGame4.csv"));
		StringBuilder sb = new StringBuilder();
        
        sb.append("My Moves, My Scores, Player1 Moves, Player1 Scores, Player2 Moves, Player2 Scores" + '\n');
        for(int i =0; i< JoseZiBot3.myMoves.size(); i++){
            System.out.println(JoseZiBot3.myMoves.get(i));
            sb.append(JoseZiBot3.myMoves.get(i));
            sb.append(',');
            sb.append(JoseZiBot3.myScores.get(i));
            sb.append(',');
            sb.append(JoseZiBotDef.myMoves.get(i));
            sb.append(',');
            sb.append(JoseZiBotDef.myScores.get(i));
            sb.append(',');
            sb.append(JoseZiBotDef100.myMoves.get(i));
            sb.append(',');
            sb.append(JoseZiBotDef100.myScores.get(i));
            sb.append('\n');
        }
        sb.append('\n');

		pw.write(sb.toString());
		pw.close();
    }
    
}
