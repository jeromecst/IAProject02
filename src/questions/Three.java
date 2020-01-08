package questions;

import partitionnement.MixGaussCentreEloignes;
import partitionnement.ToolBox;

import java.io.IOException;

public class Three {
        public static void main(String[] args) throws IOException {
                String name = "space";
                String ext = ".png";
                String filename = name + ext;
                double[][] x = ToolBox.getNormalizedPoints(filename);

                for (int k = 2; k < 10; k++) {
                        MixGaussCentreEloignes myMG3 = ToolBox.bestAmongstX(x, k, 5);

                        myMG3.statsRmax();
                        myMG3.displayScore();

                        myMG3.writeAllColor(name + "k-" + k, filename);
                }
        }
}
