package Classes;

import Algoritmes.KakuroGenerator;
import Algoritmes.KakuroSolver;
import Persistencia.CtrlCapaDades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class
GestioTaulers {
    private final ArrayList<Tauler> taulers;
    CtrlCapaDades ccp;
    private final KakuroSolver ak;

    public GestioTaulers(GestioUsuaris gu) throws Exception {
        ak = new KakuroSolver();
        ccp = new CtrlCapaDades();
        taulers = Objects.requireNonNullElseGet(ccp.getAllTaulers(gu, ak), ArrayList::new);
    }

    public void CrearTauler(Usuari usr) throws Exception {
        System.out.println("Escull una opció:");
        System.out.println("1: Crear manualment");
        System.out.println("2: Crear automaticament");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String opt = reader.readLine();
        switch (opt) {
            case ("1"):
                crearManual(usr);
                break;
            case ("2"):
                crearAutomatic(usr);
                break;
            case ("3"):
                break;
            default:
                System.out.println("Opció incorrecte. Indrodueix un dels valors: 1, 2 o 3");
        }

    }

    public void crearManual(Usuari usr) throws Exception {
        int x = 0;
        int y = 0;
        boolean b = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (!b) {
                System.out.println("Escriu la mida:");
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
        } catch (NumberFormatException nfe) {
            System.out.println("Mida incorrecte!");
            crearManual(usr);
            return;
        }
        Tauler t = new Tauler(usr, x, y);
        String[][] chars = llegirTauler(x, y, reader);
        if (chars == null) return;

        t.generarTaulerCella(chars, ak);
        LocalTime t0 = LocalTime.now();
        boolean check = ak.esCorrecte(t);
        LocalTime t1 = LocalTime.now();
        System.out.println(Duration.between(t0, t1));
        if (!check) {
            System.out.println("Tauler incorrecte, tornar-ho a provar? [y/n]");
            String o = reader.readLine();
            if (o.equals("y")) {
                crearManual(usr);
            }
        } else {
            System.out.println("Tauler correcte!");
            taulers.add(t);
            ccp.loadTauler(t);
        }

    }

    private String[][] llegirTauler(int files, int columnes, BufferedReader reader) throws Exception {
        try {
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
                    System.out.println("Tornar-ho a provar? [y/n]");
                    String o = reader.readLine();
                    if (o.equals("y")) {
                        System.out.println("Torna a escriure el tauler:");
                        return llegirTauler(files, columnes, reader);
                    } else return null;
                } else if (v.length != columnes) {
                    System.out.println("Numero de columnes incorrecte");
                    System.out.println("Tornar-ho a provar? [y/n]");
                    String o = reader.readLine();
                    if (o.equals("y")) {
                        System.out.println("Torna a escriure el tauler:");
                        return llegirTauler(files, columnes, reader);
                    } else return null;
                }
            }
            return chars;
        } catch (IOException e) {
            throw new Exception("Error de lectura");
        }
    }

    public void crearAutomatic(Usuari usr) throws Exception {
        int x;
        int y;
        boolean s;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Escriu la mida:");
            String xy = reader.readLine();
            String[] xyaux = xy.split(",");
            x = Integer.parseInt(xyaux[0]);
            y = Integer.parseInt(xyaux[1]);

            if (x < 2 || y < 2) {
                System.out.println("Mida incorrecte, massa petit!");
                crearAutomatic(usr);
                return;
            }

            System.out.println("Simetric? [y/n]");
            String sim = reader.readLine();

            s = sim.equals("y");

        } catch (NumberFormatException nfe) {
            System.out.println("Mida incorrecte!");
            crearAutomatic(usr);
            return;
        }
        Tauler t = new Tauler(usr, x, y);

        KakuroGenerator kg = new KakuroGenerator(x, y);
        LocalTime t0 = LocalTime.now();
        kg.generarTaulerAuto(t, x, y, s);
        LocalTime t1 = LocalTime.now();
        System.out.println(Duration.between(t0, t1));
        taulers.add(t);
        ccp.loadTauler(t);
    }

    public void imprimirTaulers() {
        for (Tauler tauler : taulers) {
            tauler.imprimeixTauler();
            System.out.println("----------");
        }
    }

}
