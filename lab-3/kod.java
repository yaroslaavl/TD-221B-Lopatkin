import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import edu.emory.mathcs.jtransforms.fft.*;
import java.io.IOException;
import java.util.Arrays;

public class kod {

    public static void main(String[] args) {
            double fs = 12000;
            double Tc = 2;
            double fn = 5000;
            double fm = 250;
            int N = (int) (fs * Tc);
            double[] mt = new double[N];
            double[] skalaDecA = new double[N];
            double[] skalaDecP = new double[N];
            double[] skalaDecF = new double[N];
            double[] widmaA = new double[N];
            double[] widmaP = new double[N];
            double[] widmaF = new double[N];
            double kA = 21;
            double kP = 8;
            double kF = 10;
            double[] zat = new double[N];
            double[] zpt = new double[N];
            double[] zft = new double[N];
            double[] fkA = new double[N/2];
            double[] fkP = new double[N/2];
            double[] fkF = new double[N/2];

            for (int l = 0; l < N; l++) {
                double t = l / fs;
                mt[l] = Math.sin(2 * Math.PI * fm * t);
                zat[l] = (kA * mt[l] + 1) * Math.cos(2 * Math.PI * fn * t);
                zpt[l] = Math.cos(2 * Math.PI * fn * t + kP * mt[l]);
                zft[l] = Math.cos(2 * Math.PI * fn * t + (kF / fm) * mt[l]);
            }

            DoubleFFT_1D fftA = new DoubleFFT_1D(N);
            fftA.realForward(zat);
            DoubleFFT_1D fftP = new DoubleFFT_1D(N);
            fftP.realForward(zpt);
            DoubleFFT_1D fftF = new DoubleFFT_1D(N);
            fftF.realForward(zft);

            for (int i = 0; i < N / 2; i++) {
                double a = zat[2 * i];
                double b = zat[2 * i + 1];
                widmaA[i] = Math.sqrt(a * a + b * b);
                skalaDecA[i] = 10 * Math.log10(widmaA[i]);
                fkA[i] = i * (fs/N);
            }

            for (int i = 0; i < N / 2; i++) {
                double a = zpt[2 * i];
                double b = zpt[2 * i + 1];
                widmaP[i] = Math.sqrt(a * a + b * b);
                skalaDecP[i] = 10 * Math.log10(widmaP[i]);
                fkP[i] = i * (fs/N);
            }

            for (int i = 0; i < N / 2; i++) {
                double a = zft[2 * i];
                double b = zft[2 * i + 1];
                widmaF[i] = Math.sqrt(a * a + b * b);
                skalaDecF[i] = 10 * Math.log10(widmaF[i]);
                fkF[i] = i * (fs/N);
            }
            double[][] fkArray = {fkA, fkP, fkF};
            double[][] skalaDecArray = {skalaDecA, skalaDecP, skalaDecF};

            double[][] widthArray = new double[3][3];
             for (int i = 0; i < 3; i++) {
              for (int j = 0; j < 3; j++) {
                widthArray[i][j] = szerokosc(fkArray[i], skalaDecArray[i], (j + 1) * 3);
                System.out.println("Szerokosc " + ((j + 1) * 3) + " z" + (i == 0 ? "a" : (i == 1 ? "p" : "f")) + ": " + widthArray[i][j]);
              }
             }
            XYChart chartA = new XYChartBuilder().width(800).height(600).title("Sygnały zat").xAxisTitle("Czas").yAxisTitle("Wartość").build();
            XYSeries seriesA = (XYSeries) chartA.addSeries("Wartości", null, skalaDecA).setMarker(SeriesMarkers.NONE);

            XYChart chartP = new XYChartBuilder().width(800).height(600).title("Sygnały zpt").xAxisTitle("Czas").yAxisTitle("Wartość").build();
            XYSeries seriesP = (XYSeries) chartP.addSeries("Wartości", null, skalaDecP).setMarker(SeriesMarkers.NONE);

            XYChart chartF = new XYChartBuilder().width(800).height(600).title("Sygnały zft").xAxisTitle("Czas").yAxisTitle("Wartość").build();
            XYSeries seriesF = (XYSeries) chartF.addSeries("Wartości", null, skalaDecF).setMarker(SeriesMarkers.NONE);

            try {
                BitmapEncoder.saveBitmap(chartA, "lab-3/za_c_widmo.png", BitmapEncoder.BitmapFormat.PNG);
                BitmapEncoder.saveBitmap(chartP, "lab-3/zp_c_widmo.png", BitmapEncoder.BitmapFormat.PNG);
                BitmapEncoder.saveBitmap(chartF, "lab-3/zf_c_widmo.png", BitmapEncoder.BitmapFormat.PNG);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public static double szerokosc(double[] fk, double[] widma, double Bdb) {
        double AmplitudaMaksymalna = Arrays.stream(widma).max().getAsDouble();
        double BdbMaksymalne = AmplitudaMaksymalna - Bdb;

        double fmin = 0;
        double fmax = 0;

        for (int i = 0; i < widma.length; i++) {
            if (widma[i] >= BdbMaksymalne) {
                fmin = fk[i];
                break;
            }
        }

        for (int i = widma.length - 1; i >= 0; i--) {
            if (widma[i] >= BdbMaksymalne) {
                fmax = fk[i];
                break;
            }
        }
        return fmax - fmin;
    }
}

