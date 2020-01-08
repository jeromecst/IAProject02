package questions;

import partitionnement.MixGaussCentreEloignes;
import partitionnement.TasGaussien;
import partitionnement.ToolBox;

import java.io.IOException;

public class Five {
        public static double[][] generateData(int nbPpts) {
                double[][] x = new double[nbPpts][1];
                for (int i = 0; i < nbPpts; i++) {
                        if (i < nbPpts / 2.) {
                                x[i][0] = ToolBox.Random.nextGaussian() * .2 - 2.;
                        } else {
                                x[i][0] = ToolBox.Random.nextGaussian() * 1.5 + 5.;
                        }
                }
                return x;
        }

        public static void main(String[] args) throws IOException {
                double[][] x = generateData(1000);

                MixGaussCentreEloignes myMG5 = new MixGaussCentreEloignes(x, 2);
                myMG5.runAlgorithm();
                myMG5.displayCenters();

                TasGaussien myHisto = new TasGaussien(x, 100);
                myHisto.generateHistoNormalized();
                double[][] sommeGaussiennes = myHisto.generateSumGaussienne2D(myMG5, 10000);
                ToolBox.writeData("q5gaussienne.d", sommeGaussiennes);
                myHisto.writeHistogram("q5histo.d");
                //plot "q5gaussienne.d" with lp, "q5histo.d" with boxes
        }
}
