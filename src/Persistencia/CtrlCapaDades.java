package Persistencia;

import Algoritmes.AlgoritmesKakuro;
import Classes.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CtrlCapaDades {

    private final File userDataBase;
    private final File taulerDataBase;

    public CtrlCapaDades() {
        userDataBase = new File("src/Data/BDUsuaris.txt");
        taulerDataBase = new File("src/Data/BDTaulers.txt");
    }

    public ArrayList<Usuari> getAllUsers() throws Exception {
        try {
            Scanner myReader = new Scanner(userDataBase);
            ArrayList<Usuari> usuaris = new ArrayList<>();
            String nom;
            String pwd;
            while (myReader.hasNextLine()) {
                nom = myReader.nextLine();
                pwd = myReader.nextLine();
                Usuari usr = new Usuari(nom, pwd);
                usuaris.add(usr);

            }
            myReader.close();
            return usuaris;
        } catch (IOException e) {
            throw new Exception("Error de lectura al fitxer 'BDUsuaris.txt'");
        }
    }

    public ArrayList<Tauler> getAllTaulers(GestioUsuaris gu, AlgoritmesKakuro ak) throws Exception {
        try {
            Scanner myReader = new Scanner(taulerDataBase);
            ArrayList<Tauler> taulers = new ArrayList<>();
            String line;
            String[] fic;
            String[] time;
            while (myReader.hasNextLine()) {
                line = myReader.nextLine();
                Usuari creador = gu.getUsuari(line);
                line = myReader.nextLine();
                time = line.split(":");
                int hores = Integer.parseInt(time[0]);
                int minuts = Integer.parseInt(time[1]);
                int segons = Integer.parseInt(time[2]);

                line = myReader.nextLine();
                fic = line.split(",");
                int fila = Integer.parseInt(fic[0]);
                int columna = Integer.parseInt(fic[1]);

                Tauler t = new Tauler(creador, fila, columna, hores, minuts, segons);
                t.generarTaulerCella(llegirTauler(fila, columna, myReader), ak);
                taulers.add(t);
            }
            myReader.close();
            return taulers;
        } catch (IOException e) {
            throw new Exception("Error de lectura al fitxer 'BDTaulers.txt'");
        }
    }

    private String[][] llegirTauler(int files, int columnes, Scanner myReader) throws FileNotFoundException {
        int contf;
        String line;
        String[][] chars = new String[files][columnes];

        for(contf = 0; contf<files; contf++){
            line = myReader.nextLine();
            chars[contf] = line.split(",");
        }
        return chars;
    }

    public void loadUsuari(Usuari usr) throws Exception {
        try {
            FileWriter fw = new FileWriter(userDataBase, true);
            fw.write(usr.getName() + "\n");
            fw.write(usr.getPwd() + "\n");
            fw.close();
        } catch (IOException e) {
            throw new Exception("Error d'escriptura al fitxer 'BDUsuaris.txt'");
        }
    }



    public void loadUsuaris(ArrayList<Usuari> usrs) throws Exception{
        try {
            PrintWriter writer = new PrintWriter(userDataBase);
            writer.print("");
            writer.close();
            for (Usuari usr : usrs) {
                FileWriter fw = new FileWriter(userDataBase, true);
                fw.write(usr.getName() + "\n");
                fw.write(usr.getPwd() + "\n");
                fw.close();
            }
        }
            catch (IOException e) {
                throw new Exception("Error d'escriptura al fitxer 'BDUsuaris.txt'");
        }
    }
    public void loadTauler(Tauler t) throws Exception {
        try {
            FileWriter fw = new FileWriter(taulerDataBase, true);
            fw.write(t.getCreador().getName() + "\n");
            fw.write(t.getHores() + ":" + t.getMinuts() + ":" + t.getSegons() + "\n");
            fw.write(t.getFiles() + "," + t.getColumnes() + "\n");

            Cella[][] tauler = t.getTauler();

            for (int i = 0; i< t.getFiles(); ++i){
                for(int j = 0; j<t.getColumnes() - 1; ++j){
                    fw.write(tauler[i][j].getValor() + ",");
                }
                fw.write(tauler[i][t.getColumnes()-1].getValor() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            throw new Exception("Error d'escriptura al fitxer 'BDTauler.txt'");
        }


    }


}
