package questions;

import partitionnement.*;

import java.io.IOException;

public class ZeroBestCenter {

        private static double[] scores(int nbIte, MixGauss myMG, int c) throws IOException {
                double[] score = new double[nbIte];
                double max = 0;
                for (int i = 0; i < nbIte; i++) {
                        myMG.reset();
                        myMG.runAlgorithm();
                        score[i] = myMG.scoreTot();
                        if (score[i] > max) {
                                myMG.writeAllColor("bestCenter" + c, "mms.png");
                        }
                }
                return score;
        }

        private static void stats(double[] scores) {
                double somme = 0;
                for (double score : scores) {
                        somme += score;
                }
                System.out.println("max : " + ToolBox.max(scores) + " moyenne : " + somme / (double) scores.length);
        }

        public static void main(String[] args) throws IOException {
                int nbIte = 20;
                int nbCentre = 10;
                double[][] colors = {
                        {15, 10, 20}, //noir
                        {30, 50, 200}, //bleu
                        {240, 230, 30}, //jaune
                        {240, 230, 30}, //jaune
                        {255, 105, 30}, //orange
                        {150, 30, 30}, //rouge
                        {60, 230, 60}, //vert
                        {220, 210, 170}, //fond clair
                        {220, 210, 170}, //fond clair
                        {220, 210, 170}, //fond clair
                        {200, 180, 120}}; //fond foncé
                double[][] m = ToolBox.normalize(colors, 255);
                double[][] x = ToolBox.getNormalizedPoints("mms.png");
                double[][] allScores = new double[5][nbIte];

                allScores[0] = scores(nbIte, new MixGauss(x, m), 0); //centre donnés manuellement
                allScores[1] = scores(nbIte, new MixGauss(x, nbCentre), 1); //centre complètement random
                allScores[2] = scores(nbIte, new MixGaussCentreEloignes(x, nbCentre), 2); //centre éloignés
                allScores[3] = scores(nbIte, new MixGaussCentreData(x, nbCentre), 3); //centre random sur les données
                allScores[4] = scores(nbIte, new MixGaussKMeans(x, nbCentre), 4); //centre kmeans

                for (double[] allScore : allScores) {
                        stats(allScore);
                }

        }
}
