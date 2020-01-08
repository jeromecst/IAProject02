package partitionnement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class ToolBox {
        protected static String path = "./";
        public static java.util.Random Random = new Random();

        /**
         * Ecrit une image à partir d'un tableau de couleurs et de dimensions
         *
         * @param image    : le nom de l'image à écrire
         * @param tabColor : le tableau de pixel de couleurs
         * @param width    : largeur de l'image
         * @param height   : hauteur de l'image
         * @throws IOException si l'on ne peut pas enregistrer l'image
         */
        public static void writeImage(String image, Color[] tabColor, int width, int height) throws IOException {
                BufferedImage bui_out = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++)
                                bui_out.setRGB(i, j, tabColor[i * height + j].getRGB());
                }
                ImageIO.write(bui_out, "PNG", new File(path + image));

        }

        /**
         * A partir d'une image, renvoie un tableau de point contenant le cod RGB de chaque pixel
         *
         * @param image nom de l'image
         * @return le tableau contenant le code RGB de chaque pixel
         * @throws IOException si l'on ne peut pas ouvrir l'image
         */
        public static double[][] getNormalizedPoints(String image) throws IOException {
                BufferedImage bui = ImageIO.read(new File(path + image));

                int width = bui.getWidth();
                int height = bui.getHeight();

                double[][] x = new double[width * height][3];
                int k = 0;
                for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                                int pixel = bui.getRGB(i, j);
                                Color c = new Color(pixel);
                                x[k][0] = c.getRed() / 255.;
                                x[k][1] = c.getGreen() / 255.;
                                x[k][2] = c.getBlue() / 255.;
                                k++;
                        }
                }
                return x;
        }

        /**
         * Largeur d'une image
         *
         * @param image : le nom de l'image
         * @return la largeur
         * @throws IOException ouvrir le fichier
         */
        public static int getWidth(String image) throws IOException {
                String imageMMS = path + image;
                BufferedImage bui = ImageIO.read(new File(imageMMS));
                return bui.getWidth();
        }

        /**
         * Hauteur d'une image
         *
         * @param image : le nom de l'image
         * @return la hauteur
         * @throws IOException ouvrir le fichier
         */
        public static int getHeigth(String image) throws IOException {
                String imageMMS = path + image;
                BufferedImage bui = ImageIO.read(new File(imageMMS));
                return bui.getHeight();

        }

        /**
         * Normalise un vecteur selon val
         *
         * @param tab : le vecteur à normaliser
         * @param val : supposé > à toutes les composantes de tab
         * @return : tab / val
         */
        public static double[][] normalize(double[][] tab, double val) {
                double[][] tabNormalized = new double[tab.length][tab[0].length];
                for (int i = 0; i < tab.length; i++) {
                        for (int j = 0; j < tab[0].length; j++) {
                                tabNormalized[i][j] = tab[i][j] / val;
                        }
                }
                return tabNormalized;
        }


        /**
         * Copie un tableau 2D dans un autre
         *
         * @param a : le tableau 2D
         * @return une copie du tableau 2D
         */
        public static double[][] copyArray(double[][] a) {
                double[][] b = new double[a.length][a[0].length];
                for (int i = 0; i < a.length; i++) {
                        System.arraycopy(a[i], 0, b[i], 0, a[0].length);
                }
                return b;
        }

        /**
         * Ecrit l'ensemble d'un tableau dans un fichier
         *
         * @param name : le nom du fichier à écrire
         * @param x    : le tableau
         * @throws IOException ouverture d'un fichier
         */
        public static void writeData(String name, double[][] x) throws IOException {
                FileWriter fileWriter = new FileWriter(path + name);

                for (double[] doubles : x) {
                        for (int j = 0; j < x[0].length; j++) {
                                fileWriter.write(doubles[j] + " ");
                        }
                        fileWriter.write("\n");
                }
                fileWriter.close();
                System.out.println(name + " written");
        }

        /**
         * Ecrit les valeurs d'un fichier dans un tableau
         *
         * @param name : le nom du fichier
         * @param dim  : le nombre d'éléments par ligne
         * @return le tableau
         * @throws FileNotFoundException ouverture du fichier
         */
        public static double[][] readFile(String name, int dim) throws FileNotFoundException {
                ArrayList<double[]> x = new ArrayList<>();

                Scanner scanner = new Scanner(new File(path + name));
                scanner.useDelimiter("[\n ]");

                int d = 0;
                String word;
                while (scanner.hasNext()) {
                        x.add(d, new double[dim]);
                        word = scanner.next().replace(" ", "");
                        x.get(d)[0] = Double.parseDouble(word);
                        word = scanner.next().replace(" ", "");
                        x.get(d)[1] = Double.parseDouble(word);
                        d++;
                }
                scanner.close();

                double[][] data = new double[x.size()][dim];
                for (int i = 0; i < x.size(); i++) {
                        data[i] = x.get(i).clone();
                }
                return data;
        }

        public static double max(double[] table) {
                double max = table[0];
                for (double val : table) {
                        if (val > max) {
                                max = val;
                        }
                }
                return max;
        }

        public static double min(double[] table) {
                double min = table[0];
                for (double val : table) {
                        if (val < min) {
                                min = val;
                        }
                }
                return min;
        }

        static int max(int[] table) {
                int max = table[0];
                for (int t : table) {
                        if (t > max) {
                                max = t;
                        }
                }
                return max;
        }

        public static int iMax(double[] table) {
                double max = table[0];
                int imax = 0;
                for (int i = 0; i < table.length; i++) {
                        if (table[i] > max) {
                                max = table[i];
                                imax = i;
                        }
                }
                return imax;

        }

        /**
         * Indice de la valeur min d'un tableau
         *
         * @param table : le tableau
         * @return l'indice min
         */
        public static int iMin(double[] table) {
                double min = table[0];
                int imin = 0;
                for (int i = 0; i < table.length; i++) {
                        if (table[i] < min) {
                                min = table[i];
                                imin = i;
                        }
                }
                return imin;

        }

        /**
         * Renvoie l'algorithme qui a le mieux performé sur n tours
         *
         * @param x    : les données
         * @param k    : le nombre de centres
         * @param tour : le nombre de tour
         * @return l'algorithme le plus performant
         */
        public static MixGaussCentreEloignes bestAmongstX(double[][] x, int k, int tour) {
                double[] score = new double[tour];
                ArrayList<MixGaussCentreEloignes> MGs = new ArrayList<>(tour);
                for (int i = 0; i < tour; i++) {
                        MGs.add(i, new MixGaussCentreEloignes(x, k));
                        MGs.get(i).runAlgorithm();
                        score[i] = MGs.get(i).scoreTot();
                }
                return MGs.get(ToolBox.iMax(score));
        }

        /**
         * Initialise les centres de façon complètement aléatoire entre 0 et 1
         *
         * @param nombreCentres : le nombre de centres
         * @param dim           : la dimension
         * @return les centres
         */
        public static double[][] fullRandom(int nombreCentres, int dim) {
                double[][] m = new double[nombreCentres][dim];
                for (int k = 0; k < nombreCentres; k++) {
                        for (int i = 0; i < dim; i++) {
                                m[k][i] = Random.nextDouble();
                        }
                }
                return m;
        }

        /**
         * Initialise les centres parmis les données
         *
         * @param x             : les données
         * @param nombreCentres : le nombre de centres
         * @return les centres
         */
        public static double[][] centreData(double[][] x, int nombreCentres) {
                double[][] m = new double[nombreCentres][x[0].length];
                for (int k = 0; k < nombreCentres; k++) {
                        m[k] = x[Random.nextInt(x.length)].clone();
                }
                return m;
        }

        /**
         * Algo pour éloigner les centres les uns des autres
         *
         * @param x         : les données
         * @param nbCentres : le nombre de centres
         * @return les centres
         */
        public static double[][] pointsExtremite(double[][] x, int nbCentres) {
                double[][] m = new double[nbCentres][x[0].length];
                double max;
                int imax = 0;
                m[0] = x[Random.nextInt(x.length)].clone();

                for (int k = 1; k < nbCentres; k++) {
                        max = 0;
                        for (int d = 0; d < x.length; d++) {
                                double sumDistance = 0;
                                for (double[] extCentre : m) {
                                        sumDistance += KMeans.distance(x[d], extCentre);
                                }
                                if (sumDistance > max) {
                                        max = sumDistance;
                                        imax = d;
                                }
                        }
                        m[k] = x[imax].clone();
                }
                return m;
        }

        // inutile

        /**
         * Approximation de l'exp qui semble être 5x plus rapide que Math.exp() mais si peu précise qu'elle renvoie des NaN
         *
         * @param val : la valeur à calculer
         * @return une approximation de cette valeur
         * @source : https://martin.ankerl.com/2007/02/11/optimized-exponential-functions-for-java/
         */
        public static double exp(double val) {
                final long tmp = (long) (1512775 * val + (1072693248 - 60801));
                return Double.longBitsToDouble(tmp << 32);
        }

}
