import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class kodLab7 {
    public static void main(String[] args) {
        String data = "minecraft";

        List<Integer> bitStream = convertor(data);
        System.out.println("Bity ze slowa "+ data +": " +bitStream);
        System.out.println("Rozmiar: " + bitStream.size());

        List<Integer> wybraneBity = wybierzBity(bitStream, 56);
        System.out.println("Bity wybrane: " + wybraneBity);

        List<Integer> encodedBits = koderHamming(wybraneBity);
        System.out.println("Bity zakodowane: " + encodedBits);

        List<Double> ASKSignal = ask(encodedBits);

        List<Integer> demodulatedBits = DASK(ASKSignal);
        System.out.println("Bity po demodulacji: " + demodulatedBits);
        System.out.println("Rozmiar bitow po demodulacji: " + demodulatedBits.size());

        double ber = BER(encodedBits, demodulatedBits);
        System.out.println("Procent bledu: " + ber);

        List<Integer> decodedBits = correctHamming(demodulatedBits);
        System.out.println("Dekodowane bity: " + decodedBits);
    }

    static double fs = 1000;
    static double Tc = 1.0;
    static int N = (int) (fs * Tc);
    static double Tb;
    static int W = 2;
    static double A1 = 1.0;
    static double A2 = 0.5;
    static double fn;
    static int Tbp;

    public static List<Integer> hamming7(List<Integer> daneBites) {
        int x0 = daneBites.get(0) ^ daneBites.get(1) ^ daneBites.get(3);
        int x1 = daneBites.get(0) ^ daneBites.get(2) ^ daneBites.get(3);
        int x3 = daneBites.get(1) ^ daneBites.get(2) ^ daneBites.get(3);

        List<Integer> wynik = new ArrayList<>(7);
        wynik.add(x0);
        wynik.add(x1);
        wynik.add(daneBites.get(0));
        wynik.add(x3);
        wynik.add(daneBites.get(1));
        wynik.add(daneBites.get(2));
        wynik.add(daneBites.get(3));
        return wynik;
    }

    private static List<Integer> wybierzBity(List<Integer> bitStream, int length) {
        return new ArrayList<>(bitStream.subList(0, Math.min(bitStream.size(), length)));
    }

    private static List<Integer> koderHamming(List<Integer> bitStream) {
        List<Integer> koderBity = new ArrayList<>();
        List<Integer> fragment = new ArrayList<>();

        for (int i = 0; i < bitStream.size(); i++) {
            fragment.add(bitStream.get(i));
            if (fragment.size() == 4) {
                koderBity.addAll(hamming7(fragment));
                fragment.clear();
            }
        }
        return koderBity;
    }

    //ChatGPT - start
    public static List<Integer> convertor(String stringToConvert) {
        List<Integer> bitStream = new ArrayList<>();
        for (char tempChar : stringToConvert.toCharArray()) {
            for (int i = 6; i >= 0; i--) {
                bitStream.add((tempChar & (1 << i)) >> i);
            }
        }
        return bitStream;
    }
    //ChatGPT - end

    public static List<Double> ask(List<Integer> bity) {
        Tb = Tc / bity.size();
        Tbp = (int) (Tb * fs);
        fn = (1 / Tb);

        double[] za = new double[N];
        int granica = 0;
        for (int i = 0; i < bity.size(); i++) {
            int bitIndex = bity.get(i);
            for (int j = 0; j < Tbp; j++) {
                double t = (j + Tbp * i)/fs;

                if (bitIndex == 1) {
                    za[j + granica] = A1 * Math.sin(2 * Math.PI * fn * t);
                } else {
                    za[j + granica] = A2 * Math.sin(2 * Math.PI * fn * t);
                }
            }
            granica = granica + Tbp;
        }

         /*return kanalAlfa(za,whiteNoise(za.length),0);
           return kanalBeta(za,2);
           return kanalAlfaBeta(za,whiteNoise(za.length),1,2);*/
        return kanalBetaAlfa(za,whiteNoise(za.length),1,0);
    }

    public static List<Double> kanalAlfa(double[] ASK, double[] whiteNoise, int alfa) {
        return IntStream.range(0, ASK.length)
                .mapToObj(i -> ASK[i] + alfa * whiteNoise[i])
                .collect(Collectors.toList());
    }

    public static List<Double> kanalBeta(double[] ASK, int beta) {
        return IntStream.range(0, ASK.length)
                .mapToObj(i -> ASK[i] * Math.exp(-beta * i))
                .collect(Collectors.toList());
    }

    public static List<Double> kanalAlfaBeta(double[] ASK, double[] whiteNoise,int alfa, int beta){
        return IntStream.range(0,ASK.length)
                .mapToObj(i -> ASK[i] + alfa * whiteNoise[i] * Math.exp(-beta * i))
                .collect(Collectors.toList());
    }

    public static List<Double> kanalBetaAlfa(double[] ASK, double[] whiteNoise,int alfa, int beta){
        return IntStream.range(0,ASK.length)
                .mapToObj(i -> ASK[i] * Math.exp(-beta * i) + alfa * whiteNoise[i])
                .collect(Collectors.toList());
    }

    //ChatGPT - start
    public static double[] whiteNoise(int length) {
        double[] whiteNoise = new double[length];

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            whiteNoise[i] = 2 * random.nextDouble() - 1;
        }
        return whiteNoise;
    }
    //ChatGPT - end

    public static List<Integer> DASK(List<Double> za) {
        double[] nosnaASK = new double[N];
        for (int i = 0; i < N; i++) {
            nosnaASK[i] = Math.sin(2 * Math.PI * fn * i / fs) * za.get(i);
        }

        double[] p = new double[N];
        for (int i = 0; i < N; i++) {
            double s = 0;
            for (int j = 0; j < Tb*fs; j++) {
                int index = (int) (i * Tb*fs + j);
                if (index < N) {
                    s += nosnaASK[index];
                    p[index] = s;
                }
            }
        }

        XYChart chartA = new XYChartBuilder().width(800).height(600).title("Sygnały za").xAxisTitle("Czas").yAxisTitle("Wartość").build();
        XYSeries seriesA = (XYSeries) chartA.addSeries("Wartości za", null, p).setMarker(SeriesMarkers.NONE);
        try {
            BitmapEncoder.saveBitmap(chartA, "lab-7/p.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Integer> c = new ArrayList<>();
        for (int i = 0; i < 98; i++) {
            int count = 0;
            for (int j = i * Tbp; j < Tbp + Tbp * i; j++) {
                if (p[j] >= 1.8) {
                    count++;
                }
            }
            if (count > Tbp / 2 - 1) {
                c.add(1);
            } else {
                c.add(0);
            }
        }
        return c;
    }

    public static double BER(List<Integer> bitPoczatkowy, List<Integer> bitZamieniony) {
        if (bitPoczatkowy.size() != bitZamieniony.size()) {
            throw new IllegalArgumentException("Blad!!!");
        }
        long blad = IntStream.range(0, bitPoczatkowy.size())
                .filter(i -> !bitPoczatkowy.get(i).equals(bitZamieniony.get(i)))
                .count();
        return (double) blad / bitPoczatkowy.size();
    }

    public static List<Integer> correctHamming(List<Integer> bits) {
        List<Integer> dataBits = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        for (Integer demodulatedBit : bits) {
            data.add(demodulatedBit);
            if (data.size() == 7) {
                int s1 = data.get(0) ^ data.get(2) ^ data.get(4) ^ data.get(6);
                int s2 = data.get(1) ^ data.get(2) ^ data.get(5) ^ data.get(6);
                int s3 = data.get(3) ^ data.get(4) ^ data.get(5) ^ data.get(6);
                int znalezienieBledu = s1 * 1 + s2 * 2 + s3 * 4;

                if (znalezienieBledu != 0) {
                    if (data.get(znalezienieBledu - 1) == 1) {
                        data.set(znalezienieBledu - 1, 0);
                    } else{
                        data.set(znalezienieBledu - 1, 1);
                    }
                }
                dataBits.add(data.get(2));
                dataBits.add(data.get(4));
                dataBits.add(data.get(5));
                dataBits.add(data.get(6));
                data.clear();
            }
        }
        return dataBits;
    }
}