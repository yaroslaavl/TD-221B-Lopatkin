import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class kodLab5 {

    public static List<Integer> zmiana(double[] c, double Tbp, int[] b) {
        List<Integer> tabelaWynikowa = new ArrayList<>();
        int licznik = 0;

        for (int i = 0; i < b.length; i++) {
            licznik = 0;
            for (int j = (int) (i*Tbp); j < Tbp+Tbp*i; j++) {
                if (c[j] == 1) {
                    licznik++;
                }
            }
            if (licznik >= Tbp / 2 - 1) {
                tabelaWynikowa.add(1);
            } else {
                tabelaWynikowa.add(0);
            }
        }
        return tabelaWynikowa;
    }


    public static void main(String[] args) {
        int[] b = {1, 0, 0, 1, 1, 0, 0, 1, 1, 1};
        double fs = 1000;
        double Tc = 1.0;
        double Tb = Tc / b.length;
        int N = (int) (fs * Tc);
        int W = 2;
        double A1 = 0.2;
        double A2 = 0.5;
        double fn = W * (1 / Tb);
        int Tbp = (int) (Tb * fs);
        double[] skalaDecA = new double[N / 2];
        double[] skalaDecP = new double[N / 2];
        double[] skalaDecF = new double[N / 2];
        double[] widmaA = new double[N / 2];
        double[] widmaP = new double[N / 2];
        double[] widmaF = new double[N / 2];
        double[] za = new double[N];
        double[] zp = new double[N];
        double[] zf = new double[N];
        double[] fkA = new double[N / 2];
        double[] fkP = new double[N / 2];
        double[] fkF = new double[N / 2];

        int granica = 0;
        for (int i = 0; i < b.length; i++) {
            int bitIndex = b[i];
            for (int j = 0; j < Tbp; j++) {
                double t = (double) (i * Tbp + j) / fs;
                if (bitIndex == 1) {
                    za[j + granica] = A2 * Math.sin(2 * Math.PI * fn * t);
                } else {
                    za[j + granica] = A1 * Math.sin(2 * Math.PI * fn * t);
                }
            }
            granica = granica + Tbp;
        }

        granica = 0;
        for (int i = 0; i < b.length; i++) {
            int bitIndex = b[i];
            for (int j = 0; j < Tbp; j++) {
                double t = (double) (i * Tbp + j) / fs;
                if (bitIndex == 1) {
                    zp[j + granica] = Math.sin(2 * Math.PI * fn * t + Math.PI);
                } else {
                    zp[j + granica] = Math.sin(2 * Math.PI * fn * t);
                }
            }
            granica = granica + Tbp;
        }

        granica = 0;
        double fn1 = (W + 1) / Tb;
        double fn2 = (W + 2) / Tb;
        for (int i = 0; i < b.length; i++) {
            int bitIndex = b[i];
            for (int j = 0; j < Tbp; j++) {
                double t = (double) (i * Tbp + j) / fs;
                if (bitIndex == 1) {
                    zf[j + granica] = Math.sin(2 * Math.PI * fn2 * t);
                } else {
                    zf[j + granica] = Math.sin(2 * Math.PI * fn1 * t);
                }
            }
            granica = granica + Tbp;
        }

        double[] nosnaASK = new double[N];
        for (int i = 0; i < N; i++) {
            nosnaASK[i] = Math.sin(2 * Math.PI * fn * i / fs) * za[i];
        }

        double[] p = new double[N];
        for (int i = 0; i < N; i++) {
            double s = 0;
            for (int j = 0; j < Tbp; j++) {
                int index = (int) (i * Tbp + j);
                if (index < N) {
                    s += nosnaASK[index];
                    p[index] = s;
                }
            }
        }
        double d = 0;
        for (int i = 0; i < Tbp; i++) {
            d += p[i];
        }
        double m = d / Tbp;
        double[] c = new double[N];
        for (int i = 0; i < N; i++) {
            c[i] = (p[i] >= m) ? 1 : 0;
        }

        double[] nosnaPSK = new double[N];
        for (int i = 0; i < N; i++) {
            nosnaPSK[i] = Math.sin(2 * Math.PI * fn * i / fs) * zp[i];
        }

        double[] p2 = new double[N];
        for (int i = 0; i < N; i++) {
            double s = 0;
            for (int j = 0; j < Tbp; j++) {
                int index = (int) (i * Tbp + j);
                if (index < N) {
                    s += nosnaPSK[index];
                    p2[index] = s;
                }
            }
        }

        double[] c2 = new double[N];
        for (int i = 0; i < N; i++) {
            c2[i] = (p2[i] > 0) ? 0 : 1;
        }
        double[] nosnaFSK1 = new double[N];
        for (int i = 0; i < N; i++) {
            nosnaFSK1[i] = Math.sin(2 * Math.PI * fn1 * i / fs) * zf[i];
        }
        double[] nosnaFSK2 = new double[N];
        for (int i = 0; i < N; i++) {
            nosnaFSK2[i] = Math.sin(2 * Math.PI * fn2 * i / fs) * zf[i];
        }

        double[] p3FSK1 = new double[N];
        for (int i = 0; i < N; i++) {
            double s = 0;
            for (int j = 0; j < Tbp; j++) {
                int index = (int) (i * Tbp + j);
                if (index < N) {
                    s += nosnaFSK1[index];
                    p3FSK1[index] = s;
                }
            }
        }
        double[] p3FSK2 = new double[N];
        for (int i = 0; i < N; i++) {
            double s = 0;
            for (int j = 0; j < Tbp; j++) {
                int index = (int) (i * Tbp + j);
                if (index < N) {
                    s += nosnaFSK2[index];
                    p3FSK2[index] = s;
                }
            }
        }
        double[] p3FSK = new double[N];
        for (int i = 0; i < N; i++) {
            p3FSK[i] = p3FSK2[i] - p3FSK1[i];
        }

        double[] c3 = new double[N];
        for (int i = 0; i < N; i++) {
            c3[i] = (p3FSK[i] > 0) ? 1 : 0;
        }


        List<Integer> bitASK = zmiana(c, Tbp, b);
        List<Integer> bitPSK = zmiana(c2, Tbp, b);
        List<Integer> bitFSK = zmiana(c3, Tbp, b);

        System.out.println("Bity ASK: " + Arrays.asList(bitASK));
        System.out.println("Bity PSK: " + Arrays.asList(bitPSK));
        System.out.println("Bity FSK: " + Arrays.asList(bitFSK));
        System.out.println("b: " + Arrays.toString(b));



        XYChart chartA = new XYChartBuilder().width(800).height(600).title("Sygnały za").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesA = (XYSeries) chartA.addSeries("Wartości za", null, za).setMarker(SeriesMarkers.NONE);
        XYChart chartA1 = new XYChartBuilder().width(800).height(600).title("Sygnały zt").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesA1 = (XYSeries) chartA1.addSeries("Wartości zt", null, nosnaASK).setMarker(SeriesMarkers.NONE);
        XYChart chartA2 = new XYChartBuilder().width(800).height(600).title("Sygnały pt").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesA2 = (XYSeries) chartA2.addSeries("Wartości pt", null, p).setMarker(SeriesMarkers.NONE);
        XYChart chartA3 = new XYChartBuilder().width(800).height(600).title("Sygnały ct").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesA3 = (XYSeries) chartA3.addSeries("Wartości ct", null, c).setMarker(SeriesMarkers.NONE);

        XYChart chartP = new XYChartBuilder().width(800).height(600).title("Sygnały zp").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesP = (XYSeries) chartP.addSeries("Wartości zp", null, zp).setMarker(SeriesMarkers.NONE);
        XYChart chartP1 = new XYChartBuilder().width(800).height(600).title("Sygnały zt").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesP1 = (XYSeries) chartP1.addSeries("Wartości zt", null, nosnaPSK).setMarker(SeriesMarkers.NONE);
        XYChart chartP2 = new XYChartBuilder().width(800).height(600).title("Sygnały pt").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesP2 = (XYSeries) chartP2.addSeries("Wartości pt", null, p2).setMarker(SeriesMarkers.NONE);
        XYChart chartP3 = new XYChartBuilder().width(800).height(600).title("Sygnały pt").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesP3 = (XYSeries) chartP3.addSeries("Wartości pt", null, c2).setMarker(SeriesMarkers.NONE);



        XYChart chartF = new XYChartBuilder().width(800).height(600).title("Sygnały zp").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesF = (XYSeries) chartF.addSeries("Wartości zf", null, zf).setMarker(SeriesMarkers.NONE);
        XYChart chartF1 = new XYChartBuilder().width(800).height(600).title("Sygnały x1").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesF1 = (XYSeries) chartF1.addSeries("Wartości nosna FSK1", null, nosnaFSK1).setMarker(SeriesMarkers.NONE);
        XYChart chartF2 = new XYChartBuilder().width(800).height(600).title("Sygnały x2").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesF2 = (XYSeries) chartF2.addSeries("Wartości nosna FSK2", null, nosnaFSK2).setMarker(SeriesMarkers.NONE);
        XYChart chartF3 = new XYChartBuilder().width(800).height(600).title("Sygnały p1").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesF3 = (XYSeries) chartF3.addSeries("Wartości p3 FSK 1", null, p3FSK1).setMarker(SeriesMarkers.NONE);
        XYChart chartF4 = new XYChartBuilder().width(800).height(600).title("Sygnały p2").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesF4 = (XYSeries) chartF4.addSeries("Wartości p3 FSK2", null, p3FSK2).setMarker(SeriesMarkers.NONE);
        XYChart chartF5 = new XYChartBuilder().width(800).height(600).title("Sygnały p").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesF5 = (XYSeries) chartF5.addSeries("Wartości p3 FSK", null, p3FSK).setMarker(SeriesMarkers.NONE);
        XYChart chartF6 = new XYChartBuilder().width(800).height(600).title("Sygnały с").xAxisTitle("N").yAxisTitle("Wartość").build();
        XYSeries seriesF6 = (XYSeries) chartF6.addSeries("Wartości с", null, c3).setMarker(SeriesMarkers.NONE);
        try {
            BitmapEncoder.saveBitmap(chartA1, "lab-5/ask_z.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartA, "lab-5/ask_x.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartA2, "lab-5/ask_p.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartA3, "lab-5/ask_c.png", BitmapEncoder.BitmapFormat.PNG);

            BitmapEncoder.saveBitmap(chartP, "lab-5/psk_x.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartP1, "lab-5/psk_z.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartP2, "lab-5/psk_p.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartP3, "lab-5/psk_c.png", BitmapEncoder.BitmapFormat.PNG);

            BitmapEncoder.saveBitmap(chartF, "lab-5/fsk_z.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartF1, "lab-5/fsk_x1.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartF2, "lab-5/fsk_x2.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartF3, "lab-5/fsk_p1.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartF4, "lab-5/fsk_p2.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartF5, "lab-5/fsk_p.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartF6, "lab-5/fsk_c.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
