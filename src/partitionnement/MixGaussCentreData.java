package partitionnement;

public class MixGaussCentreData extends MixGauss {

        /**
         * Constructeur pour les centres sur des données aléatoires
         *
         * @param x             : les nombres
         * @param nombreCentres : le nombre de centres
         */
        public MixGaussCentreData(double[][] x, int nombreCentres) {
                super(x, nombreCentres);
                this.nombreCentres = nombreCentres;
                this.r = new double[this.nbPoints][this.nombreCentres];
                this.sigma2 = new double[this.nombreCentres][dimension];
                this.rho = new double[this.nombreCentres];
                this.m = ToolBox.centreData(x, nombreCentres);
                this.rk = new double[this.nombreCentres];
        }

        @Override
        public void reset() {
                this.m = ToolBox.centreData(x, nombreCentres);
                this.scoreTot = 0;
        }

}
