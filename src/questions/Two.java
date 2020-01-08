package questions;

import partitionnement.MixGaussCentreEloignes;
import partitionnement.ToolBox;

import java.io.IOException;

public class Two {
        public static void main(String[] args) throws IOException {
                String name = "mms";
                String ext = ".png";
                String filename = name + ext;
                double[][] x = ToolBox.getNormalizedPoints(filename);
                double[][] allScore = new double[8][10];
                for (int k = 2; k < 10; k++) {
                        for (int tour = 0; tour < 10; tour++) {
                                MixGaussCentreEloignes myMG2 = new MixGaussCentreEloignes(x, k);
                                myMG2.runAlgorithm();
                                allScore[k - 2][tour] = myMG2.scoreTot();
                                myMG2.displayScore();
                        }
                }

                double[][] maxScore = new double[allScore.length][2];
                for (int i = 0; i < 8; i++) {
                        maxScore[i][0] = i + 2;
                        maxScore[i][1] = ToolBox.max(allScore[i]);
                }
                ToolBox.writeData("q2.d", maxScore);
        }
}
