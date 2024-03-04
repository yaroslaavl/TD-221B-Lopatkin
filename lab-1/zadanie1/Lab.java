import java.util.*;
import java.util.function.Function;
import static java.lang.System.in;
import static java.lang.System.out;
import static java.lang.Integer.*;
import javax.swing.*;
import java.awt.*;
//NUMER FUNKCJI: 5
public class Lab {
 public static void main(String[] args) {
  int tc = 11;
  int fs = 6;
  int N = fs*tc;
  int fi = 0;
    double[] tab = new double[N];
    for(int n = 0; n<=N-1;n++){
    double t = n/fs;
    double x = Math.sin(2*Math.PI*fs*t+fi)*Math.cos(4*Math.PI*t);
    tab[n] = x;
    System.out.println("x "+n+": "+x);
   }
 }
}