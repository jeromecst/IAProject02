package partitionnement;

import java.util.Arrays;

class KMeans {
        /**
         * Algorithme des k-moyennes
         *
         * @param X : les données
         * @param K : le nombre de centres à calculer
         * @return la position des centres
         */
        public static double[][] algorithm(double[][] X, int K) {
                double eps = 0.01;
                double maj = eps + 1;
                int[] ass;

                double[][] centres = new double[K][X[0].length];
                for (int k = 0; k < K; k++) {
                        for (int i = 0; i < centres[0].length; i++) {
                                centres[k][i] = ToolBox.Random.nextDouble();
                        }
                }

                while (maj > eps) {
                        ass = Assigner(X, centres);
                        maj = Deplct(X, centres, ass);
                }
                return centres;
        }

        /**
         * Calcule une norme euclidienne entre deux points
         *
         * @param A : points A
         * @param B : points B
         * @return la distance
         */
        static double distance(double[] A, double[] B) {
                double distance = 0;
                for (int i = 0; i < A.length; i++) {
                        distance += Math.pow(A[i] - B[i], 2);
                }
                return Math.sqrt(distance);
        }

        /**
         * Assigne à chaque donnée un centre
         *
         * @param x       : les données
         * @param centres : les centres
         * @return un tableau avec l'assignation de chaque donnée
         */
        static int[] Assigner(double[][] x, double[][] centres) {
                int[] ass = new int[x.length];
                double minDistance;
                double distance;
                for (int i = 0; i < x.length; i++) {
                        minDistance = 10e100;
                        for (int c = 0; c < centres.length; c++) {
                                distance = distance(centres[c], x[i]);
                                if (distance < minDistance) {
                                        minDistance = distance;
                                        ass[i] = c;
                                }
                        }
                }
                return ass;
        }

        /**
         * Déplace les centres en fonction de l'assignation des données
         *
         * @param x       : les données
         * @param centres : les centres
         * @param ass     : le tabeau avec l'assignation de chaque donnée
         * @return la somme des distance entre l'ancienne et la nouvelle position des centres
         */
        static double Deplct(double[][] x, double[][] centres, int[] ass) {
                double[][] anciensCentre = Arrays.copyOf(centres, centres.length);
                double numerateur;
                double denominateur;
                for (int c = 0; c < centres.length; c++) {
                        for (int dim = 0; dim < x[0].length; dim++) {
                                numerateur = 0;
                                denominateur = 0;
                                for (int i = 0; i < x.length; i++) {
                                        if (ass[i] == c) {
                                                numerateur += x[i][dim];
                                                denominateur += 1;
                                        }
                                }
                                if (denominateur != 0) {
                                        centres[c][dim] = numerateur / denominateur;
                                }
                        }
                }

                double sommeDistance = 0;
                for (int c = 0; c < centres.length; c++) {
                        sommeDistance += distance(centres[c], anciensCentre[c]);
                }
                return sommeDistance;
        }


}
