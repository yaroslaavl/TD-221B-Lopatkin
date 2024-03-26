import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;

import java.awt.*;
import java.io.IOException;
//NUNER FUNKCJI: 2
public class ZadanieDwaV {
    public static void main(String[] args) {
        int tc = 1;
        int fs = 8000;
        int N = fs * tc;
        int fi = 0;
        double[] tab = new double[N];
        double x = 0;
        double z = 0;
        int f = 2;
        for (int n = 0; n <= N - 1; n++) {
            double t = (double) n / fs;
            x = Math.sin(2 * Math.PI * f * t * Math.cos(3 * Math.PI * t) + t * fi);
            double y = (x * Math.pow(t, 3)) / 3;
            z = 1.92 * (Math.cos(3 * Math.PI * t / 2) + Math.cos(Math.pow(y, 2) / (8 * x + 3) + t));
            double v = (y * z) / (x + 2) * Math.cos(7.2 * Math.PI * t) + Math.sin(Math.PI * Math.pow(t, 2));
            tab[n] = v;
            System.out.println("v " + n + ": " + v);
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).title("Wykres funkcji V").xAxisTitle("Czas").yAxisTitle("Wartość").build();

        XYSeries series = chart.addSeries("Wartości", null, tab);
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
            BitmapEncoder.saveBitmap(chart, "lab-1/zadanie2/wykresV.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
