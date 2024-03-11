package zadanie2;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;

import java.awt.*;
import java.io.IOException;

public class ZadanieDwa {
    public static void main(String[] args) {
        int N = 4;
        double fs = 22.05;
        double[] widmaA = new double[N];
        double[] Btab = new double[N];
        double[] Atab = new double[N];
        double[] skalaDec = new double[N];
        double[] fk = new double[N];
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

            widmaA[k] = Math.sqrt(a*a + b*b);
             System.out.println("WidmaA: " + widmaA[k]);

            skalaDec[k] = 10 * Math.log10(widmaA[k]);
             System.out.println("SkalaDec: " + skalaDec[k]);

            fk[k] = k * (fs/N);
            System.out.println("Skala Czestotliwosci: " + fk[k]);

        }
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Wykres funkcji").xAxisTitle("Czas").yAxisTitle("Wartość").build();


        XYSeries series = chart.addSeries("Wartości", null, skalaDec);

        chart.getStyler().setMarkerSize(8);
        chart.getStyler().setCursorColor(XChartSeriesColors.BLUE);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setChartTitleBoxBackgroundColor(new Color(0, 222, 0));
        chart.getStyler().setPlotGridLinesVisible(true);
        chart.getStyler().setAxisTickLabelsColor(Color.BLACK);
        chart.getStyler().setChartTitleBoxBackgroundColor(Color.GREEN);
        chart.getStyler().setChartTitleBoxBorderColor(Color.ORANGE);
        chart.getStyler().setXAxisTitleColor(Color.RED);
        chart.getStyler().setLegendBackgroundColor(Color.PINK);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setChartTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setLegendFont(new Font(Font.DIALOG, Font.ITALIC, 20));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        chart.getStyler().setAxisTitleFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        chart.getStyler().setAxisTitleFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        chart.getStyler().setXAxisTickMarkSpacingHint(100);
        chart.getStyler().setYAxisTickMarkSpacingHint(100);


        try {
            BitmapEncoder.saveBitmap(chart, "lab-2/zadanie2/wykres.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
