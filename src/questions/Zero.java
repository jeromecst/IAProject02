package questions;

import partitionnement.*;

import java.io.IOException;


public class Zero {
        public static void main(String[] args) throws IOException {
                double[][] x = ToolBox.getNormalizedPoints("mms.png");
                MixGaussCentreEloignes myMGAuto = new MixGaussCentreEloignes(x, 8);
                myMGAuto.runAlgorithm();

                myMGAuto.statsRmax();
                myMGAuto.displayScore();

                myMGAuto.writeSingleColor("q0autoCentre", "mms.png");
                myMGAuto.writeAllColor("q0autoCentreAll", "mms.png");
        }
}
