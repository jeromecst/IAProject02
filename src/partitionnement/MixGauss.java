package partitionnement;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class MixGauss {
        public int dimension;
        public int nbPoints;
        public int nombreCentres;

        public double[][] x;
        public double[][] r;
        public double[][] m;
        public double[][] sigma2;
        public double[] rho;
        public double[] rk;
        public int[] rmax;
        public double scoreTot;

        private double[] score;

        /**
         * Constructeur 1
         *
         * @param x: les données
         */
        protected MixGauss(double[][] x) {
                this.x = x;
                this.nbPoints = x.length;
                this.dimension = x[0].length;
                this.rmax = new int[nbPoints];
        }

        /**
         * Constructeur pour les centres donnés manuellement
         *
         * @param x : les données
         * @param m : les centres
         */
        public MixGauss(double[][] x, double[][] m) {
                this(x);
                this.m = m;
                this.nombreCentres = m.length;
                //this.extractColors();
                this.r = new double[this.nbPoints][this.nombreCentres];
                this.sigma2 = new double[this.nombreCentres][dimension];
                this.rho = new double[this.nombreCentres];
                this.rk = new double[this.nombreCentres];
        }

        /**
         * Constructeur pour les centres générés aléatoirement
         *
         * @param x             : les données
         * @param nombreCentres : le nombre de centres
         */
        public MixGauss(double[][] x, int nombreCentres) {
                this(x);
                this.nombreCentres = nombreCentres;
                this.m = ToolBox.fullRandom(nombreCentres, dimension);
                this.r = new double[this.nbPoints][this.nombreCentres];
                this.sigma2 = new double[this.nombreCentres][dimension];
                this.rho = new double[this.nombreCentres];
                this.rk = new double[this.nombreCentres];
        }

        /**
         * Reset les centres et le score
         */
        public void reset() {
                this.m = ToolBox.fullRandom(nombreCentres, dimension);
                this.scoreTot = 0;
        }

        /**
         * Execute l'algorithme:
         * <p>
         * * Initialisation de sigma et rho
         * * Algo
         * * Affichage des scores
         */
        public void runAlgorithm() {
                initConditionInit();

                System.out.print("runAlgorithm... ");
                int nbIte = 0;
                double eps = 10e-2;
                double maj = eps + 1;
                while (maj > eps) {
                        assigner();
                        maj = deplct();
                        if (nbIte < 10) for (int i = 0; i < 28; i++) System.out.print("\b");
                        else for (int i = 0; i < 29; i++) System.out.print("\b");
                        System.out.print("runAlgorithm... " + nbIte + " itérations");
                        nbIte++;
                }
                System.out.println();
                initRmax();
                score();
        }

        /**
         * Calcule et/ou renvoie le score de l'algorithme
         *
         * @return le score
         */
        public double scoreTot() {
                if (this.scoreTot == 0) {

                        double sum = 0;
                        for (int d = 0; d < nbPoints; d++) {
                                sum += score[d];
                        }
                        this.scoreTot = (sum / (double) nbPoints);
                }
                return this.scoreTot;
        }

        /**
         * Affiche le score
         */
        public void displayScore() {
                if (this.scoreTot == 0) {
                        this.scoreTot = scoreTot();
                }
                System.out.println("Le score est de : " + scoreTot);

        }

        /**
         * Statistique sur les probabilités :
         * Combien de points assignés à chaque cluster
         */
        public void statsRmax() {
                int pc;
                int[] rmaxT = new int[ToolBox.max(rmax) + 1];
                for (int value : rmax) {
                        rmaxT[value] += 1;
                }
                for (int k = 0; k < rmaxT.length; k++) {
                        pc = (int) (100 * (double) rmaxT[k] / (double) nbPoints);
                        System.out.println(rmaxT[k] + "(" + pc + "%)" + " points assignés au centre " + k);
                }

        }

        /**
         * Enregistre pour chaque cluster une image et ses points associés
         *
         * @param image  : le nom de l'image (sans extension)
         * @param image2 : sur quelle image on copie les dimenstion
         * @throws IOException écriture et ouverture des fichier
         */
        public void writeSingleColor(String image, String image2) throws IOException {
                for (int centre = 0; centre < this.m.length; centre++) {
                        int R = (int) (this.m[centre][0] * 255);
                        int G = (int) (this.m[centre][1] * 255);
                        int B = (int) (this.m[centre][2] * 255);
                        Color color = new Color(R, G, B);
                        Color[] tabColor = new Color[x.length];
                        for (int i = 0; i < x.length; i++) {
                                if (this.rmax[i] == centre) {
                                        tabColor[i] = color;
                                } else {
                                        tabColor[i] = new Color(255, 255, 255);
                                }
                        }
                        ToolBox.writeImage(image + centre + ".png", tabColor, ToolBox.getWidth(image2), ToolBox.getHeigth(image2));

                }

        }

        /**
         * Enregistre une image composée de tous les clusters
         *
         * @param image  : le nom de l'image (sans extension)
         * @param image2 : sur quelle image on copie les dimenstion
         * @throws IOException écriture et ouverture des fichier
         */
        public void writeAllColor(String image, String image2) throws IOException {
                Color[] tabColor = new Color[this.x.length];
                for (int centre = 0; centre < m.length; centre++) {
                        int R = (int) (this.m[centre][0] * 255);
                        int G = (int) (this.m[centre][1] * 255);
                        int B = (int) (this.m[centre][2] * 255);
                        Color color = new Color(R, G, B);
                        for (int i = 0; i < this.x.length; i++) {
                                if (this.rmax[i] == centre) {
                                        tabColor[i] = color;
                                }
                        }

                }
                ToolBox.writeImage(image + ".png", tabColor, ToolBox.getWidth(image2), ToolBox.getHeigth(image2));

        }

        /**
         * Teste si la somme des probabilité est bien égale à 1
         */
        public void testR() {
                for (double[] points : r) {
                        double sum = 0;
                        for (double prob : points) {
                                sum += prob;
                        }
                        if (Math.abs(1. - sum) > 10e-10) {
                                System.out.println("erreur : " + sum);
                        }
                }

        }

        /**
         * Affiche les statisques par centres (variance, positions...)
         */
        public void displayCenters() {
                for (int k = 0; k < nombreCentres; k++) {
                        System.out.print("Pos centre " + k + ": ");
                        double sommeSigma2 = 0;
                        for (int i = 0; i < dimension; i++) {
                                sommeSigma2 += Math.sqrt(this.sigma2[k][i]);
                                System.out.print(m[k][i] + " ");
                        }
                        System.out.print("variance : " + sommeSigma2);
                        System.out.print(" densité : " + rho[k]);
                        System.out.println(" ");
                }
        }

        /**
         * Calcule le score
         */
        private void score() {
                this.score = new double[nbPoints];
                for (int d = 0; d < nbPoints; d++) {
                        double sum = 0;
                        for (int k = 0; k < nombreCentres; k++) {
                                double produit = 1;
                                for (int i = 0; i < dimension; i++) {
                                        produit *= (1. / Math.sqrt(2. * Math.PI * sigma2[k][i])) * Math.exp(-(Math.pow(x[d][i] - m[k][i], 2)) / (2 * sigma2[k][i]));
                                }
                                sum += produit * rho[k];
                        }
                        score[d] = Math.log(sum);
                }
        }

        /**
         * Calcule la probabilité maximale qu'une donnée appartienne à un cluster
         */
        private void initRmax() {
                for (int d = 0; d < nbPoints; d++) {
                        rmax[d] = ToolBox.iMax(r[d]);
                }
        }

        /**
         * Calcule les probabilités qu'une donnée appartienne à un cluster
         */
        private void assigner() {
                double produitTop, top, sommeBot, produitBot;

                double[] ptitNumerateur = new double[nbPoints + nbPoints * (nombreCentres + nombreCentres * dimension)];
                double[] ptitDenominateur = new double[nombreCentres * nombreCentres + dimension];
                //précalcules pour l'optimisation
                for (int k = 0; k < nombreCentres; k++) {
                        for (int i = 0; i < dimension; i++) {
                                ptitDenominateur[k * nombreCentres + i] = Math.sqrt(2. * Math.PI * sigma2[k][i]);
                                for (int d = 0; d < nbPoints; d++) {
                                        ptitNumerateur[d + nbPoints * k + i * nombreCentres * nbPoints] = Math.exp(-Math.pow(x[d][i] - m[k][i], 2) / (2 * sigma2[k][i]));
                                }
                        }
                }

                for (int d = 0; d < nbPoints; d++) {
                        for (int k = 0; k < nombreCentres; k++) {
                                produitTop = 1;
                                for (int i = 0; i < dimension; i++) {
                                        produitTop *= ptitNumerateur[d + nbPoints * k + i * nombreCentres * nbPoints] / ptitDenominateur[k * nombreCentres + i];
                                }
                                top = rho[k] * produitTop;
                                sommeBot = 0;
                                for (int l = 0; l < nombreCentres; l++) {
                                        produitBot = 1;
                                        for (int i = 0; i < dimension; i++) {
                                                produitBot *= ptitNumerateur[d + nbPoints * l + i * nombreCentres * nbPoints] / ptitDenominateur[l * nombreCentres + i];
                                        }
                                        sommeBot += produitBot * rho[l];
                                }
                                r[d][k] = top / sommeBot;
                        }
                }
        }

        /**
         * Déplace les clusters selon l'assignation des points
         *
         * @return les centres
         */
        private double deplct() {
                double[][] oldM = ToolBox.copyArray(m);

                initRk();
                moyenne();
                sigma2();
                rho();

                double sommeDistance = 0;
                for (int c = 0; c < nombreCentres; c++) {
                        sommeDistance += KMeans.distance(m[c], oldM[c]);
                }
                return sommeDistance;
        }

        /**
         * Calcule la somme des probabilité pour chaque centre
         */
        private void initRk() {
                Arrays.fill(this.rk, 0);
                for (int k = 0; k < nombreCentres; k++) {
                        for (double[] doubles : r) {
                                rk[k] += doubles[k];
                        }
                }
        }

        /**
         * Calcule rho
         */
        private void rho() {
                for (int k = 0; k < nombreCentres; k++) {
                        rho[k] = rk[k] / (double) nbPoints;
                }
        }

        /**
         * Calcule l'esperance au carré
         */
        private void sigma2() {
                double top, bot;
                for (int k = 0; k < nombreCentres; k++) {
                        for (int i = 0; i < dimension; i++) {
                                top = 0;
                                bot = rk[k];
                                for (int d = 0; d < nbPoints; d++) {
                                        top += r[d][k] * Math.pow(x[d][i] - m[k][i], 2);
                                }
                                sigma2[k][i] = top / bot;
                        }
                }
        }

        /**
         * Calcule la position des centres
         */
        private void moyenne() {
                double top, bot;
                for (int k = 0; k < nombreCentres; k++) {
                        for (int i = 0; i < dimension; i++) {
                                top = 0;
                                bot = rk[k];
                                for (int d = 0; d < nbPoints; d++) {
                                        top += r[d][k] * x[d][i];
                                }
                                m[k][i] = top / bot;
                        }
                }
        }

        /**
         * Initialise les contitions initiales sigma et rho de façon aléatoire
         */
        public void initConditionInit() {
                Arrays.fill(rho, 1. / nombreCentres);
                for (int k = 0; k < nombreCentres; k++) {
                        for (int i = 0; i < dimension; i++) {
                                sigma2[k][i] = .4 + ToolBox.Random.nextGaussian() / 20.;
                        }
                }
        }

}
