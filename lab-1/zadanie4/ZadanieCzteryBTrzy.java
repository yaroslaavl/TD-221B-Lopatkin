import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;

import java.awt.*;
import java.io.IOException;
//NUNER FUNKCJI: 3
public class ZadanieCzteryBTrzy {
    public static void main(String[] args) {
        double tc = 1;
        double fs = 22.05;
        double N = (fs * tc);
        double[] tab = new double[(int) N];
        double b3 = 0;
        for (int n = 1; n <= 50; n++) {
            double t = n / fs;
            double gora = Math.cos(4*Math.PI*n*t);
            double dol = 4*n*(Math.sin(8*Math.PI*n*t)+2);
            b3 += gora / dol;
            tab[n] = b3;
            System.out.println("B3 " + n + ": " + b3);
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).title("Wykres funkcji B3").xAxisTitle("Czas").yAxisTitle("Wartość").build();

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
            BitmapEncoder.saveBitmap(chart, "lab-1/zadanie4/wykresB3.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

