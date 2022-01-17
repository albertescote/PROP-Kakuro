Tal com haviem acordat, no hem afegit un makefile ja que ho hem programat amb IntelliJ i no és necessari. El que si hem afegit es dos fitxers ".jar" anomenats "driverKakuroSolver.jar" i "driverKakuroGenerator.jar" per provar les funcionalitats exclusives de les classes solver i generator, ho trobareu dins la carpeta "Drivers". També adjuntem, dins del projecte, una carpeta "out" amb tots els documents ".class" i una carpeta "test" amb les proves JUnit de la classe Cella. Trobareu, també un document pdf amb tota la documentació del projecte (casos d'ús, diagrama de clsses, etc).

[Introducció]:
El projecte està format per quatre packages: classes, algoritmes, persistencia i data (hi ha un package exception buit per recollir futures excepcions):

El package Classes conté les classes útils (tauler, cella, usuari...) i els seus controladors.

El package Algoritmes conté les classes per resoldre i generar kakuros, juntament amb els seus drivers (actualment hi ha una classes KakuroGenerator2 que no es fa servir, és una classe en proves per a millorar l'algoritme generator).

El package Persistencia conté la classe controladora de la capa de dades.

I el package Data conté els fitxers .txt que fan la funció de base de dades per guardar informació dels usuaris i dels taulers creats.

Com executar el projecte:

1. El projecte té una classe main fora dels packages que s'ha d'executar per a començar a jugar.

2. Primer demanarà iniciar sessió, si és el primer cop que entres hauràs d'introduir un nom i contrassenya.

3. Un cop alli podras gestionar el teu compte, crear un tauler manualment (comprova que tingui solució única) o automàticament, consultar els taulers creats anteriorment o tancar sessió.

4. Alternativament, pots executar directament els drivers "driverKakuroSolver" i "driverKakuroGenerator" dins del package Algoritmes.

5. També hem adjuntat uns jocs de proves per a passar sobre els drivers.