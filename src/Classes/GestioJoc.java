package Classes;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GestioJoc {
    private Usuari user;
    private final GestioUsuaris GU;
    private final GestioTaulers GT;

    public GestioJoc() throws Exception {
        GU = new GestioUsuaris();
        GT = new GestioTaulers(GU);
        user = null;
        inicialitzar();
    }

    private void inicialitzar() throws Exception {
        user = GU.iniciarSessio();
        if (user != null) client();
    }

    private void client() throws Exception {
        System.out.println("Escull una ocpció");
        System.out.println("1: Gestionar compte");
        System.out.println("2: Crear tauler");
        System.out.println("3: Consultar taulers");
        System.out.println("4: Jugar");
        System.out.println("5: Sortir\n----------");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String opt = reader.readLine();

        switch (opt) {
            case ("1"):
                boolean eliminat = GU.gestionarCompte(user);
                if (!eliminat) client();
                else {
                    user = null;
                    inicialitzar();
                }
                break;

            case ("2"):
                //TODO: un cop creat podriem fer que anes directe a jugar
                GT.CrearTauler(user);
                client();
                break;

            case ("3"):
                GT.imprimirTaulers();
                client();
                break;

            case ("4"):
                break;

            case ("5"):
                System.out.println("Segur que vol sortir? [y/n]");
                String confirmacio = reader.readLine();
                if (!confirmacio.equals("y")) client();
                else inicialitzar();
                break;

            default:
                System.out.println("Opció incorrecte. Indrodueix un dels valors: 1, 2, 3, 4 o 5");
                client();
        }
    }
}
