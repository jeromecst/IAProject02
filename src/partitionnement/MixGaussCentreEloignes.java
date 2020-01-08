package partitionnement;

import javax.tools.Tool;
import java.awt.*;
import java.io.IOException;

public class MixGaussCentreEloignes extends MixGauss {

        /**
         * Constructeur pour les centres éloignés
         *
         * @param x             : les données
         * @param nombreCentres : le nombre de centres
         */
        public MixGaussCentreEloignes(double[][] x, int nombreCentres) {
                super(x);
                this.nombreCentres = nombreCentres;
                this.r = new double[this.nbPoints][this.nombreCentres];
                this.sigma2 = new double[this.nombreCentres][dimension];
                this.rho = new double[this.nombreCentres];
                this.m = ToolBox.pointsExtremite(x, nombreCentres);
                this.rk = new double[this.nombreCentres];
        }

        @Override
        public void reset() {
                this.m = ToolBox.pointsExtremite(x, nombreCentres);
                this.scoreTot = 0;
        }


}
