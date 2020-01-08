package questions;

import partitionnement.MixGaussCentreEloignes;
import partitionnement.ToolBox;

import java.io.IOException;
import java.util.ArrayList;

public class One {

        public static void main(String[] args) throws IOException {
                int tour = 10;
                String name = "mms";
                String ext = ".png";
                String filename = name + ext;
                double[] allScores = new double[tour];
                ArrayList<MixGaussCentreEloignes> allMGs = new ArrayList<>();
                double[][] x = ToolBox.getNormalizedPoints(filename);

                for (int t = 0; t < tour; t++) {
                        allMGs.add(t, new MixGaussCentreEloignes(x, 10));
                        allMGs.get(t).runAlgorithm();

                        allScores[t] = allMGs.get(t).scoreTot();
                }

                int imin = ToolBox.iMin(allScores);
                int imax = ToolBox.iMax(allScores);

                allMGs.get(imin).writeAllColor(name + "Worst", filename);
                allMGs.get(imax).writeAllColor(name + "Best", filename);
                allMGs.get(imin).displayScore();
                allMGs.get(imax).displayScore();


        }
}
