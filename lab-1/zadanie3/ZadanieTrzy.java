import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;

import java.awt.*;
import java.io.IOException;
//NUNER FUNKCJI: 6
public class ZadanieTrzy {
    public static void main(String[] args) {
        int tc = 1;
        int fs = 8000;
        int N = fs * tc;
        double[] tab = new double[N];
        for (int n = 0; n <= N - 1; n++) {
            double t = (double) n / fs;
            if(t >= 0 && t < 1.8) {
                double u = -0.5 * t * Math.sin(20 * Math.pow(t, 3) - 18 * Math.pow(t, 2));
                        tab[n] = u;
                System.out.println("u " + n + ": " + u);
            } else if(t >= 1.8 && t < 3){
                double u = Math.cos(5*Math.PI*t)*Math.sin(12*Math.PI*Math.pow(t, 2));
                tab[n] = u;
                System.out.println("u " + n + ": " + u);
            } else if(t >= 3 && t < 4.5){
                double u = (t-3/3)*Math.sin((12-t)*Math.PI*Math.pow(t, 2));
                tab[n] = u;
                System.out.println("u " + n + ": " + u);
            }
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).title("Wykres funkcji").xAxisTitle("Czas").yAxisTitle("Wartość").build();

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
            BitmapEncoder.saveBitmap(chart, "lab-1/zadanie3/wykres.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

