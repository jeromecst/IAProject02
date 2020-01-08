package partitionnement;

import java.io.FileWriter;
import java.io.IOException;


public class TasGaussien {
        private double[] val;
        private double[][] histo;
        private double xmin;
        private double xmax;
        private int nbCases;
        private MixGauss MG;


        /**
         * Constructeur pour un histogramme
         *
         * @param val     : les données
         * @param nbCases : le nombre de cases
         */
        public TasGaussien(double[][] val, int nbCases) {
                this.val = twoToOneDim(val);
                this.xmax = ToolBox.max(this.val);
                this.xmin = ToolBox.min(this.val);
                this.nbCases = nbCases;
                generateHisto();
        }

        /**
         * Génère un histogramme
         */
        public void generateHisto() {
                double[][] Histo = new double[2][this.nbCases];
                double tailleCase = (xmax - xmin) / (double) this.nbCases;

                for (int i = 0; i < this.nbCases; i++) {
                        Histo[0][i] = i * tailleCase + xmin;
                        for (double val : this.val) {
                                if (val >= xmin + i * tailleCase && val < xmin + (i + 1) * tailleCase && val < xmax) {
                                        Histo[1][i] += 1;
                                }
                        }
                }
                this.histo = Histo;
        }

        /**
         * Génère un histogramme normalisé * 2
         */
        public void generateHistoNormalized() {
                double[][] Histo = new double[2][this.nbCases];
                double tailleCase = (xmax - xmin) / (double) this.nbCases;
                int max = 0;

                for (int i = 0; i < this.nbCases; i++) {
                        Histo[0][i] = i * tailleCase + xmin;
                        for (double val : this.val) {
                                if (val >= xmin + i * tailleCase && val < xmin + (i + 1) * tailleCase && val < xmax) {
                                        Histo[1][i] += 1;
                                }
                        }
                        if (Histo[1][i] > max) {
                                max = (int) Histo[1][i];
                        }
                }
                for (int i = 0; i < this.nbCases; i++) {
                        Histo[1][i] = 2 * Histo[1][i] / (double) max;
                }
                this.histo = Histo;
        }


        /**
         * Renvoie la valeur de la gaussienne au point x
         *
         * @param x : le point
         * @param k : le centre associé à la gaussienne
         * @return la valeur f(x)
         */
        private double f(double x, int k) {
                return (1. / Math.sqrt(2. * Math.PI * MG.sigma2[k][0]) * ToolBox.exp(-Math.pow(x - MG.m[k][0], 2) / (2. * MG.sigma2[k][0])));
        }

        /**
         * Génère la somme des gaussiennes (2D uniquement)
         *
         * @param MG       : un algorithme de la mixture de Gaussienne
         * @param nbPoints : le nombre de point pour la fonction
         * @return : l'ensemble des points f(x)
         */
        public double[][] generateSumGaussienne2D(MixGauss MG, int nbPoints) {
                this.MG = MG;
                double[][] f = new double[nbPoints][2];
                double pas = (xmax - xmin) / (double) nbPoints;
                for (int x = 0; x < nbPoints; x++) {
                        f[x][0] = this.xmin + x * pas;
                        for (int k = 0; k < MG.nombreCentres; k++) {
                                f[x][1] += f(this.xmin + x * pas, k);
                        }
                }
                return f;
        }

        /**
         * Ecrit dans un fichier l'histogramme
         *
         * @param fileName : le nom du fichier
         * @throws IOException ouverture d'un fichier
         */
        public void writeHistogram(String fileName) throws IOException {
                FileWriter fileWriter = new FileWriter(ToolBox.path + fileName);
                String line;
                for (int i = 0; i < this.histo[0].length; i++) {
                        line = this.histo[0][i] + " " + this.histo[1][i];
                        fileWriter.write(line + "\n");
                }
                fileWriter.close();
        }

        /**
         * Transforme un tableau 2D en tableau 1D contenant la première ligne
         *
         * @param x : un tableau 2D
         * @return un tableau 1D
         */
        private double[] twoToOneDim(double[][] x) {
                double[] vald = new double[x.length];
                for (int d = 0; d < x.length; d++) {
                        vald[d] = x[d][0];
                }
                return vald;
        }


}
