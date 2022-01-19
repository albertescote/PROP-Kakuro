package Algoritmes;

import Classes.Tauler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class driverKakuroSolver {
    public static void main(String[] args) throws Exception {
        AlgoritmesKakuro ak = new KakuroSolver();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean b = false;
        int x = 0;
        int y = 0;
        String[][] chars;
        try {
            System.out.println("Escriu aqui el seu kakuro:");
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
            chars = llegirTauler(x, y, reader);
        } catch (IOException e) {
            throw new Exception("Error de lectura");
        }
        LocalTime t0 = LocalTime.now();
        Tauler t = new Tauler(null, x, y);
        if (chars == null) return;

        t.generarTaulerCella(chars, ak);
        ArrayList<Tauler> tresultat = ak.resolverKakuro(t);
        LocalTime t1 = LocalTime.now();

        int cont = 0;
        ArrayList<Tauler> tresultataux = new ArrayList<>();
        if (tresultat != null) {
            for (Tauler resultado : tresultat) {
                if (resultado != null) {
                    cont++;
                    tresultataux.add(resultado);
                }
            }
            System.out.println(cont);
            for (Tauler it : tresultataux) {
                imprimeixTauler(it);
            }
        }else{
            System.out.println(cont);
        }
        System.out.println(Duration.between(t0, t1));
    }

    private static String[][] llegirTauler(int files, int columnes, BufferedReader reader) throws Exception {
        int contf;
        String line;
        String[] v;
        String[][] chars = new String[files][columnes];

        for (contf = 0; contf < files; contf++) {
            line = reader.readLine();
            v = line.split(",");
            chars[contf] = v;
            if (v[0].equals("")) {
                System.out.println("Numero de files incorrecte");
                return null;
            } else if (v.length != columnes) {
                System.out.println("Numero de columnes incorrecte");
                return null;
            }
        }
        return chars;
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
