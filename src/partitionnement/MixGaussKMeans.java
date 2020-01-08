package partitionnement;

public class MixGaussKMeans extends MixGauss {

        /**
         * Constructeur pour les centres généré par l'algorithme des k-moyennes
         *
         * @param x             : les données
         * @param nombreCentres : le nombre de centres
         */
        public MixGaussKMeans(double[][] x, int nombreCentres) {
                super(x);
                this.nombreCentres = nombreCentres;
                this.r = new double[this.nbPoints][this.nombreCentres];
                this.sigma2 = new double[this.nombreCentres][dimension];
                this.rho = new double[this.nombreCentres];
                this.m = KMeans.algorithm(x, nombreCentres);
                this.rk = new double[this.nombreCentres];
        }

        @Override
        public void reset() {
                this.m = KMeans.algorithm(x, nombreCentres);
                this.scoreTot = 0;
        }
}
