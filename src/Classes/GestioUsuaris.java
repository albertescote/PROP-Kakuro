package Classes;

import Persistencia.CtrlCapaDades;

import java.io.*;
import java.util.ArrayList;

public class GestioUsuaris {

    private final ArrayList<Usuari> users;
    private final CtrlCapaDades ccp;
    int contador;

    public GestioUsuaris() throws Exception {
        ccp = new CtrlCapaDades();
        this.users = ccp.getAllUsers();
        contador = 0;
    }

    public Usuari iniciarSessio() throws Exception {
        System.out.println("Escull una ocpció");
        System.out.println("1: Log in");
        System.out.println("2: Sign in");
        System.out.println("3: Exit\n----------");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String opt = reader.readLine();
        Usuari user;
        switch (opt) {
            case ("1"):
                user = login();
                return user;
            case ("2"):
                user = signin();
                return user;
            case ("3"):
                System.out.println("Segur que vol sortir? [y/n]");
                String confirmacio = reader.readLine();
                if (!confirmacio.equals("y")) return iniciarSessio();
                break;
            default:
                System.out.println("Opció incorrecte. Indrodueix un dels valors: 1, 2 o 3");
                return iniciarSessio();
        }
        return null;
    }

    public Usuari login() throws Exception {
        String name;
        String pwd;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Nom:");
        name = reader.readLine();
        System.out.println("Pwd:");
        pwd = reader.readLine();

        boolean excpwd = false;
        for (Usuari usr : users) {
            if (usr.getName().equals(name)) {
                if (usr.getPwd().equals(pwd)) {
                    return usr;
                } else {
                    excpwd = true;
                }
            }
        }
        if (!excpwd) System.out.println("Usuari incorrecte");
        else System.out.println("Contrassenya incorrecte");

        if (contador >= 2) {
            System.out.println("Crear un nou usuari?[y/n]");
            String opt = reader.readLine();
            if (opt.equals("y")) {
                contador = 0;
                return signin();
            } else return login();
        } else {
            contador++;
            return login();
        }
    }

    public Usuari signin() throws Exception {
        String name;
        String pwd;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Nom:");
        name = reader.readLine();
        System.out.println("Pwd:");
        pwd = reader.readLine();

        if (!usuariExisteix(name)) {
            Usuari usr = new Usuari(name, pwd);
            users.add(usr);
            ccp.loadUsuari(usr);
            return usr;
        } else {
            System.out.println("El nom d'usuari ja existeix");
            return signin();
        }
    }

    private boolean usuariExisteix(String nom) {
        for (Usuari usr : users) {
            if (usr.getName().equals(nom)) {
                return true;
            }
        }
        return false;
    }

    public Usuari getUsuari(String nom) {
        for (Usuari usr : users) {
            if (usr.getName().equals(nom)) {
                return usr;
            }
        }
        return null;
    }

    public boolean gestionarCompte(Usuari usr) throws Exception {
        boolean eliminat = false;

        System.out.println("Escull una ocpció");
        System.out.println("1: Modificar contrassenya");
        System.out.println("2: Eliminar compte");
        System.out.println("3: Sortir");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String opt = reader.readLine();

        switch (opt) {
            case ("1"):
                modificarPwd(usr);
                break;
            case ("2"):
                eliminat = eliminarCompte(usr);
                break;
            case ("3"):
                break;
            default:
                System.out.println("Opció incorrecte. Indrodueix un dels valors: 1, 2 o 3");

        }
        return eliminat;
    }

    private void modificarPwd(Usuari usr) throws Exception {
        System.out.println("Introdueixi contrassenya actual");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String cactual = reader.readLine();
        if (usr.getPwd().equals(cactual)) {
            boolean mod = false;
            while (!mod) {
                System.out.println("Introdueix nova contrassenya");
                String ncontra = reader.readLine();
                System.out.println("confirmació nova contrassenya");
                String ncontra2 = reader.readLine();
                if (ncontra.equals(ncontra2)) {
                    usr.setPwd(ncontra);
                    mod = true;
                    ccp.loadUsuaris(users);
                    System.out.println("Contrassenya modificada amb èxit");

                } else {
                    System.out.println("Contrassenya no igual");
                }
            }
        } else {
            System.out.println("Contrassenya incorrecte");
            modificarPwd(usr);
        }

    }

    private boolean eliminarCompte(Usuari usr) throws Exception {
        boolean eliminat = false;
        System.out.println("Confirmació contrassenya");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String cactual = reader.readLine();

        if (cactual.equals(usr.getPwd())) {
            System.out.println("Segur que vols eliminar el compte?[y/n]");
            String cond = reader.readLine();
            if (cond.equals("y")) {
                users.remove(usr);
                ccp.loadUsuaris(users);
                eliminat = true;
            }
        } else {
            System.out.println("Contrassenya incorrecte");
            eliminat = eliminarCompte(usr);
        }
        return eliminat;
    }
}