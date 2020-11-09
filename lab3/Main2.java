import java.util.Random;

class Zwierze extends Thread {
    static int PORT = 1;
    static int START = 2;
    static int PRACA = 3;
    static int POWROT = 4;
    static int KATASTROFA = 5;
    static int JEDZ = 1000;
    static int RESZTKI = 500;
    int nr;
    int jedzenie;
    int stan;
    Gospodarstwo g;
    Random rand;

    public Zwierze(int nr, int jedzenie, Gospodarstwo g) {
        this.nr = nr;
        this.jedzenie = jedzenie;
        this.stan = PRACA;
        this.g = g;
        rand = new Random();
    }

    public void run() {
        while (true) {
            if (stan == PORT) {
                if (rand.nextInt(2) == 1) {
                    stan = START;
                    jedzenie = JEDZ;
                    System.out.println("Wypuszczanie zwierzęcia numer: " + nr);
                    stan = g.wyjdzNaPole(nr);
                } else {
                    System.out.println("Postój.");
                }
            } else if (stan == START) {
                System.out.println("Wypuszczono zwierze numer: " + nr);
                stan = PRACA;
            } else if (stan == PRACA) {
                jedzenie -= rand.nextInt(500);
                System.out.println("Zwierze " + nr + " w drodze.");
                if (jedzenie <= RESZTKI) {
                    stan = POWROT;
                } else try {
                    sleep(rand.nextInt(1000));
                } catch (Exception e) {}
            } else if (stan == POWROT) {
                System.out.println("Pozwolenie o wpuszczenie zwierzęcia numer: " + nr + " ,ilosc jedzenia: " + jedzenie);
                stan = g.wejdzNaPole();
                if (stan == POWROT) {
                    jedzenie -= rand.nextInt(500);
                    System.out.println("Resztki: " + jedzenie);
                    if (jedzenie <= 0) stan = KATASTROFA;
                }
            } else if (stan == KATASTROFA) {
                System.out.println("RIP zwierze numer: " + nr);
                g.zrobJedzenie();
            }
        }
    }
}

class Gospodarstwo {
    static int Gospodarstwo = 1;
    static int START = 2;
    static int PRACA = 3;
    static int POWROT = 4;
    static int KATASTROFA = 5;
    int iloscGospodarstw;
    int iloscZwierzat;
    int iloscZajetych;

    Gospodarstwo(int iloscGospodarstw, int iloscZwierzat) {
        this.iloscGospodarstw = iloscGospodarstw;
        this.iloscZwierzat = iloscZwierzat;
        this.iloscZajetych = 0;
    }

    synchronized int wyjdzNaPole(int numer) {
        iloscZajetych--;
        System.out.println("Wypuszczanie zwierzęcia numer: " + numer);
        return START;
    }

    synchronized int wejdzNaPole() {
        try {
            Thread.currentThread().sleep(1000);
        } catch (Exception ie) {}
        if (iloscZajetych < iloscGospodarstw) {
            iloscZajetych++;
            System.out.println("Pozwolenie na wpuszczenie zwierzęcia " + iloscZajetych);
            return Gospodarstwo;
        } else {
            return POWROT;
        }
    }

    synchronized void zrobJedzenie() {
        iloscZwierzat--;
        System.out.println("RIP");
        if (iloscZwierzat == iloscGospodarstw) System.out.println("Ilosc zwierząt taka sama jak gospodarstw.");
    }
}

public class Main2 {
    static int zwierzeta = 10;
    static int gospodarstwa = 5;
    static Gospodarstwo g1;

    public static void main(String[] args) {
        g1 = new Gospodarstwo(gospodarstwa, zwierzeta);
        for (int i = 0; i < zwierzeta; i++)
            new Zwierze(i, 2000, g1).start();
    }
}
