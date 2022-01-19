package Algoritmes;

import Classes.Tauler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;

public class driverKakuroGenerator {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Escriu la mida:");
        boolean b = false;
        int x = 0;
        int y = 0;
        try {
            while (!b) {
                String xy = reader.readLine();
                String[] xyaux = xy.split(",");
                x = Integer.parseInt(xyaux[0]);
                y = Integer.parseInt(xyaux[1]);
                if (x < 2 || y < 2) {
                    System.out.println("Mida incorrecte, massa petit!");
                } else {
                    b = true;
                }
            }
        } catch (IOException e) {
            throw new Exception("Error de lectura");
        }
        KakuroGenerator kg = new KakuroGenerator(x, y);
        Tauler t = new Tauler(null, x, y);

        System.out.println("Simetric? [y/n]");
        String sim = reader.readLine();

        boolean s = sim.equals("y");

        LocalTime t2 = LocalTime.now();
        kg.generarTaulerAuto(t, x, y, s);
        LocalTime t3 = LocalTime.now();
        System.out.println(Duration.between(t2, t3));
        imprimeixTauler(t);
    }

    private static void imprimeixTauler(Tauler t) {
        System.out.println(t.getFiles() + "," + t.getColumnes());
        for (int i = 0; i < t.getFiles(); i++) {
            for (int j = 0; j < t.getColumnes() - 1; j++) {
                System.out.print(t.getTauler()[i][j].getValor() + ",");
            }
            System.out.println(t.getTauler()[i][t.getColumnes() - 1].getValor());
        }
    }
}
