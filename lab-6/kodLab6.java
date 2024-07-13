import Jama.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class kodLab6 {
    public static void main(String[] args) {

        haming7("hell");
//        haming15("chmoshik");
    }

    public static void haming15(String data) {
        List<Integer> bites = convertor(data);
        List<Integer> daneBites = new ArrayList<>(bites);
        System.out.println("Bity: " + daneBites);

        List<Integer> M = new ArrayList<>(4);
        M.add(daneBites.get(0));
        M.add(daneBites.get(1));
        M.add(daneBites.get(3));
        M.add(daneBites.get(7));
        System.out.println("M: " + M);

        List<Integer> K = new ArrayList<>(11);
        K.add(daneBites.get(2));
        K.add(daneBites.get(4));
        K.add(daneBites.get(5));
        K.add(daneBites.get(6));
        K.add(daneBites.get(8));
        K.add(daneBites.get(9));
        K.add(daneBites.get(10));
        K.add(daneBites.get(11));
        K.add(daneBites.get(12));
        K.add(daneBites.get(13));
        K.add(daneBites.get(14));
        System.out.println("K: " + K);

        List<Integer> MK = new ArrayList<>(15);
        MK.addAll(M);
        MK.addAll(K);
        System.out.println("MK: " + MK);

        int[][] I = new int[11][11];
        for (int i = 0; i < I.length; i++) {
            I[i][i] = 1;
        }

        int[][] P =
                    {{1, 1, 0, 0},
                        {1, 0, 1, 0},
                        {0, 1, 1, 0},
                        {1, 1, 1, 0},
                        {1, 0, 0, 1},
                        {0, 1, 0, 1},
                        {1, 1, 0, 1},
                        {0, 0, 1, 1},
                        {1, 0, 1, 1},
                        {0, 1, 1, 1},
                        {1, 1, 1, 1}};

        int[][] G = new int[11][15];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 4; j++) {
                G[i][j] = P[i][j];
            }
            for (int j = 0; j < 11; j++) {
                G[i][j + 4] = I[i][j];
            }
        }
        System.out.println("G ");
        for (int i = 0; i < G.length; i++) {
            for (int j = 0; j < G[0].length; j++) {
                System.out.print(G[i][j]);
            }
            System.out.println();
        }
        int[] c = new int[G[0].length];
        for (int i = 0; i < 15; i++) {
            int count = 0;
            for (int j = 0; j < 11; j++) {
                count += G[j][i] * K.get(j);
            }
            c[i] = count % 2;
        }
        System.out.println("C: " + Arrays.toString(c));


        c[2] = (c[2] == 0) ? 1 : 0;
        System.out.println("Blad: " + Arrays.toString(c));

        double[][] doubleArray = new double[P.length][P[0].length];
        for (int i = 0; i < P.length; i++) {
            for (int j = 0; j < P[i].length; j++) {
                doubleArray[i][j] = P[i][j];
            }
        }

        Matrix P_matrix = new Matrix(doubleArray);
        Matrix P_trans = P_matrix.transpose();

        int[][] intTransArray = new int[P_trans.getRowDimension()][P_trans.getColumnDimension()];
        for (int i = 0; i < P_trans.getRowDimension(); i++) {
            for (int j = 0; j < P_trans.getColumnDimension(); j++) {
                intTransArray[i][j] = (int) P_trans.get(i, j);
            }
        }

        System.out.println("PT: ");
        for (int i = 0; i < intTransArray.length; i++) {
            for (int j = 0; j < intTransArray[0].length; j++) {
                System.out.print(intTransArray[i][j] + " ");
            }
            System.out.println();
        }

        int[][] Ink = new int[4][4];
        for (int i = 0; i < Ink.length; i++) {
            Ink[i][i] = 1;
        }

        int[][] H = new int[8][15];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                H[i][j] = Ink[i][j];
            }
            for (int j = 0; j < 11; j++) {
                H[i][j + 4] = intTransArray[i][j];
            }
        }

        System.out.println("H ");
        for (int i = 0; i < H.length; i++) {
            for (int j = 0; j < H[0].length; j++) {
                System.out.print(H[i][j]);
            }
            System.out.println();
        }

        double[][] doubleArrayH = new double[H.length][H[0].length];
        for (int i = 0; i < H.length; i++) {
            for (int j = 0; j < H[i].length; j++) {
                doubleArrayH[i][j] = H[i][j];
            }
        }

        Matrix H_matrix = new Matrix(doubleArrayH);
        Matrix H_trans = H_matrix.transpose();

        int[][] intTransArrayH = new int[H_trans.getRowDimension()][H_trans.getColumnDimension()];
        for (int i = 0; i < H_trans.getRowDimension(); i++) {
            for (int j = 0; j < H_trans.getColumnDimension(); j++) {
                intTransArrayH[i][j] = (int) H_trans.get(i, j);
            }
        }

        System.out.println("HT: ");
        for (int i = 0; i < intTransArrayH.length; i++) {
            for (int j = 0; j < intTransArrayH[0].length; j++) {
                System.out.print(intTransArrayH[i][j] + " ");
            }
            System.out.println();
        }

        int[] s = new int[4];
        for (int i = 0; i < 4; i++) {
            int con = 0;
            for (int j = 0; j < intTransArrayH.length; j++) {
                con += c[j] * intTransArrayH[j][i];
            }
            s[i] = con % 2;
        }

        System.out.println("S: " + Arrays.toString(s));
        int znalezienieBledu = s[0] * 1 + s[1] * 2 + s[2] * 4;

        if (znalezienieBledu != 0) {
            System.out.println("Bit z bledem: " + znalezienieBledu);
            c[znalezienieBledu - 1] = (c[znalezienieBledu - 1] == 1) ? 0 : 1;
        }

        System.out.println("Koniec zadania: " );for (int i = 4; i < c.length; i++) {
            System.out.print(c[i] + " ");
        };
    }

    public static void haming7(String data) {
        List<Integer> bites = convertor(data);
        List<Integer> daneBites = new ArrayList<>(bites);
        System.out.println("Bity: " + daneBites);

        int x0 = daneBites.get(0) ^ daneBites.get(1) ^ daneBites.get(3);
        int x1 = daneBites.get(0) ^ daneBites.get(2) ^ daneBites.get(3);
        int x3 = daneBites.get(1) ^ daneBites.get(2) ^ daneBites.get(3);

        List<Integer> wynik = new ArrayList<>(7);
        wynik.add(x0);
        wynik.add(x1);
        wynik.add(daneBites.get(0));
        wynik.add(x3);
        wynik.add(daneBites.get(1));
        wynik.add(daneBites.get(2));
        wynik.add(daneBites.get(3));
        System.out.println("Slowo ma: " + wynik);

        wynik.set(1, (wynik.get(1) == 0) ? 1 : 0);
        System.out.println("Bit z bledem" + wynik);

        int s1 = wynik.get(0) ^ wynik.get(2) ^ wynik.get(4) ^ wynik.get(6);
        int s2 = wynik.get(1) ^ wynik.get(2) ^ wynik.get(5) ^ wynik.get(6);
        int s3 = wynik.get(3) ^ wynik.get(4) ^ wynik.get(5) ^ wynik.get(6);
        int znalezienieBledu = s1 * 1 + s2 * 2 + s3 * 4;

        if (znalezienieBledu != 0) {
            if (wynik.get(znalezienieBledu - 1) == 1) {
                wynik.set(znalezienieBledu - 1, 0);
            } else{
                wynik.set(znalezienieBledu - 1, 1);
            }
        }
        System.out.println(wynik);
        System.out.println("Koniec zadania: " + wynik.get(2) + wynik.get(4) + wynik.get(5) + wynik.get(6));
    }

    public static List<Integer> convertor(String stringToConvert) {
        List<Integer> bitStream = new ArrayList<>();
        for (char tempChar : stringToConvert.toCharArray()) {
            for (int i = 6; i >= 0; i--) {
                bitStream.add((tempChar & (1 << i)) >> i);
            }
        }
        return bitStream;
    }

}
