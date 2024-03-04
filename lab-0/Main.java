package kod.Main;
import java.util.*;
import java.util.function.Function;
import static java.lang.System.in;
import static java.lang.System.out;
import static java.lang.Integer.*;
import javax.swing.*;
import java.awt.*;


public class Main {

    public static void main(String [] args ) {
        Function<Double, Double> lab0 = Math::cos;

        double poczatek = -1.00;
        double koniec = 4 * Math.PI;
        double interval = Math.PI / 2;

        for (double function = poczatek; function <= koniec; function += interval) {
            double plot = lab0.apply(function);
            System.out.println(plot);
        }
    }
}





