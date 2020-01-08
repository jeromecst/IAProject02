package questions;

import partitionnement.MixGaussCentreEloignes;
import partitionnement.ToolBox;

import java.io.IOException;

public class Four {
        public static void main(String[] args) throws IOException {
                double[][] x = ToolBox.readFile("gmm_data.d", 2);

                double[][] allScore = new double[8][10];
                for (int k = 2; k < 10; k++) {
                        for (int tour = 0; tour < 10; tour++) {
                                MixGaussCentreEloignes myMG4 = new MixGaussCentreEloignes(x, k);
                                myMG4.runAlgorithm();
                                allScore[k - 2][tour] = myMG4.scoreTot();
                                myMG4.displayScore();
                        }
                }

                double[][] maxScore = new double[allScore.length][2];
                for (int i = 0; i < 8; i++) {
                        maxScore[i][0] = i + 2;
                        maxScore[i][1] = ToolBox.max(allScore[i]);
                }
                ToolBox.writeData("q4.d", maxScore);
        }
}
