import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class kodLabCztery {
    public static void main(String[] args) {
        int[] b = {1, 0, 0, 1, 1, 0, 0, 1, 1, 1};
        double fs = 1000;
        double Tc = 1.0;
        double Tb = Tc / b.length;
        int N = (int) (fs * Tc);
        int W = 2;
        double A1 =  0.2;
        double A2 =  0.5;
        double fn = W *(1/Tb);
        int Tbp = (int) (Tb * fs);
        double[] skalaDecA = new double[N/2];
        double[] skalaDecP = new double[N/2];
        double[] skalaDecF = new double[N/2];
        double[] widmaA = new double[N/2];
        double[] widmaP = new double[N/2];
        double[] widmaF = new double[N/2];
        double[] za = new double[N];
        double[] zp = new double[N];
        double[] zf = new double[N];
        double[] fkA = new double[N/2];
        double[] fkP = new double[N/2];
        double[] fkF = new double[N/2];

        int granica = 0;
        for (int i = 0; i <b.length; i++) {
            int bitIndex = b[i];
            for(int j = 0;j<Tbp;j++) {
                double t = (double) (i * granica +j)/fs;
                if (bitIndex == 1) {
                    za[j+granica] = A2 * Math.sin(2 * Math.PI * fn * t);
                } else {
                    za[j+granica] = A1 * Math.sin(2 * Math.PI * fn * t);
                }
            }
            granica = granica + Tbp;
        }
        int granica2 = 0;
        for (int i = 0; i <b.length; i++) {
            int bitIndex = b[i];
            for(int j = 0;j<Tbp;j++) {
                double t = (double) (i * granica2 +j)/fs;
                if (bitIndex == 1) {
                    zp[j+granica2] = Math.sin(2 * Math.PI * fn * t + Math.PI);
                } else {
                    zp[j+granica2] = Math.sin(2 * Math.PI * fn * t);
                }
            }
            granica2 = granica2 + Tbp;
        }
        int granica3 = 0;
        double fn1 = (W + 1) / Tb;
        double fn2 = (W + 2) / Tb;
        for (int i = 0; i <b.length; i++) {
            int bitIndex = b[i];
            for(int j = 0;j<Tbp;j++) {
                double t = (double) (i * granica3 +j)/fs;
                if (bitIndex == 1) {
                    zf[j+granica3] = Math.sin(2 * Math.PI * fn2 * t);
                } else {
                    zf[j+granica3] = Math.sin(2 * Math.PI * fn1 * t);
                }
            }
            granica3 = granica3 + Tbp;
        }

        XYChart chartA = new XYChartBuilder().width(800).height(600).title("Sygnały za").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesA = (XYSeries) chartA.addSeries("Wartości za", null, za).setMarker(SeriesMarkers.NONE);

        XYChart chartP = new XYChartBuilder().width(800).height(600).title("Sygnały zp").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesP = (XYSeries) chartP.addSeries("Wartości zp", null, zp).setMarker(SeriesMarkers.NONE);

        XYChart chartF = new XYChartBuilder().width(800).height(600).title("Sygnały zf").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesF = (XYSeries) chartF.addSeries("Wartości zf", null, zf).setMarker(SeriesMarkers.NONE);

        try {
            BitmapEncoder.saveBitmap(chartA, "lab-4/za.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartP, "lab-4/zp.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartF, "lab-4/zf.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DoubleFFT_1D fftA = new DoubleFFT_1D(N);
        fftA.realForward(za);
        DoubleFFT_1D fftP = new DoubleFFT_1D(N);
        fftP.realForward(zp);
        DoubleFFT_1D fftF = new DoubleFFT_1D(N);
        fftF.realForward(zf);

        for (int i = 0; i < N / 2; i++) {
            double a = za[2 * i];
            double b1 = za[2 * i + 1];
            widmaA[i] = Math.sqrt(a * a + b1 * b1);
            skalaDecA[i] = 10 * Math.log10(widmaA[i]);
            fkA[i] = i * (fs/N);
        }

        for (int i = 0; i < N / 2; i++) {
            double a = zp[2 * i];
            double b1 = zp[2 * i + 1];
            widmaP[i] = Math.sqrt(a * a + b1 * b1);
            skalaDecP[i] = 10 * Math.log10(widmaP[i]);
            fkP[i] = i * (fs/N);
        }

        for (int i = 0; i < N / 2; i++) {
            double a = zf[2 * i];
            double b1 = zf[2 * i + 1];
            widmaF[i] = Math.sqrt(a * a + b1 * b1);
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
        XYChart chartA1 = new XYChartBuilder().width(800).height(600).title("Sygnały za").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesA1 = (XYSeries) chartA1.addSeries("Wartości widmy", null, skalaDecA).setMarker(SeriesMarkers.NONE);

        XYChart chartP1 = new XYChartBuilder().width(800).height(600).title("Sygnały zp").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesP1 = (XYSeries) chartP1.addSeries("Wartości widmy", null, skalaDecP).setMarker(SeriesMarkers.NONE);

        XYChart chartF1 = new XYChartBuilder().width(800).height(600).title("Sygnały zf").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesF1 = (XYSeries) chartF1.addSeries("Wartości widmy", null, skalaDecF).setMarker(SeriesMarkers.NONE);

        try {
            BitmapEncoder.saveBitmap(chartA1, "lab-4/za_widmo.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartP1, "lab-4/zp_widmo.png", BitmapEncoder.BitmapFormat.PNG);
            BitmapEncoder.saveBitmap(chartF1, "lab-4/zf_widmo.png", BitmapEncoder.BitmapFormat.PNG);
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