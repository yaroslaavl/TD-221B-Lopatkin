package zadanie1;
import java.awt.*;
import java.io.IOException;

public class ZadanieJeden {
    public static void main(String[] args) {
        int N = 4;
        double[] Btab = new double[N];
        double[] Atab = new double[N];
        int[]x = {-4,0,-1,3};
        for (int k = 0; k <= N-1; k++) {
            double a = 0;
            double b = 0;
            for (int n = 0; n <= N-1; n++) {
                double fi = (-2 * Math.PI * k * n) / N;
                 a += x[n]*Math.cos(fi);
                 b += x[n]*Math.sin(fi);
            }
            Atab[k] = a;
            Btab[k] = b;
            System.out.println("a " + k + ": " + a);
            System.out.println("b " + k + ": " + b);
        }

    }
}
