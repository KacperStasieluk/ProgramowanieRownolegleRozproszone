import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.Random;
public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.println("Który algorytm? Od 1 do 3: ");
        int x = s.nextInt();

        System.out.println("Ile filozofów? Od 2 do 200: ");
        int ilosc = s.nextInt();

        s.close();

        switch (x) {
            case 1:
                Alg1 f1 = new Alg1(1);
                f1.ustaw(ilosc);
                f1.rozpocznij();
                break;
            case 2:
                Alg2 f2 = new Alg2(1);
                f2.ustaw(ilosc);
                f2.rozpocznij();
                break;
            case 3:
                Alg3 f3 = new Alg3(1);
                f3.ustaw(ilosc);
                f3.rozpocznij();
                break;
            default:
                System.out.println("Zły argument!");
                break;
        }
    }
}
class Alg1 extends Thread {
    static int MAX;
    static Semaphore[] tab;
    int numer;
    public Alg1(int nr) {
        numer = nr;
    }

    public void ustaw(int ilosc) {
        MAX = ilosc;
        tab = new Semaphore[MAX];
    }

    public void run() {
        while (true) {

            System.out.println("Filozof " + numer + " myśli!");
            try {
                Thread.sleep((long)(2000 * Math.random()));
            } catch (InterruptedException e) {}
            tab[numer].acquireUninterruptibly();
            tab[(numer + 1) % MAX].acquireUninterruptibly();
            System.out.println("Filozof " + numer + " zaczyna jeść!");
            try {
                Thread.sleep((long)(1000 * Math.random()));
            } catch (InterruptedException e) {}
            System.out.println("Filozof " + numer + " kończy jeść!");
            tab[numer].release();
            tab[(numer + 1) % MAX].release();
        }
    }
    public void rozpocznij() {
        for (int i = 0; i < MAX; i++) {
            tab[i] = new Semaphore(1);
        }
        for (int i = 0; i < MAX; i++) {
            new Alg1(i).start();
        }
    }
}

class Alg2 extends Thread {
    static int MAX;
    static Semaphore[] tab;
    int numer;
    public Alg2(int nr) {
        numer = nr;
    }

    public void ustaw(int ilosc) {
        MAX = ilosc;
        tab = new Semaphore[MAX];
    }

    public void run() {
        while (true) {

            System.out.println("Filozof " + numer + " myśli!");
            try {
                Thread.sleep((long)(2000 * Math.random()));
            } catch (InterruptedException e) {}
            if (numer == 0) {
                tab[(numer + 1) % MAX].acquireUninterruptibly();
                tab[numer].acquireUninterruptibly();
            } else {
                tab[numer].acquireUninterruptibly();
                tab[(numer + 1) % MAX].acquireUninterruptibly();
            }
            System.out.println("Filozof " + numer + " zaczyna jeść!");
            try {
                Thread.sleep((long)(1000 * Math.random()));
            } catch (InterruptedException e) {}
            System.out.println("Filozof " + numer + " kończy jeść!");
            tab[numer].release();
            tab[(numer + 1) % MAX].release();
        }
    }
    public void rozpocznij() {
        System.out.println("");
        for (int i = 0; i < MAX; i++) {
            tab[i] = new Semaphore(1);
        }
        for (int i = 0; i < MAX; i++) {
            new Alg2(i).start();
        }
    }
}


class Alg3 extends Thread {
    static int MAX;
    static Semaphore[] tab = new Semaphore[MAX];
    int numer;
    Random r;

    public Alg3(int nr) {
        numer = nr;
        r = new Random(numer);
    }

    public void ustaw(int ilosc) {
        MAX = ilosc;
        tab = new Semaphore[MAX];
    }

    public void run() {
        while (true) {
            System.out.println("Filozof " + numer + " myśli!");
            try {
                Thread.sleep((long)(2000 * Math.random()));
            } catch (InterruptedException e) {}
            int strona = r.nextInt(2);
            boolean twoForkUp = false;
            do {
                if (strona == 0) {
                    tab[numer].acquireUninterruptibly();
                    if (!(tab[(numer + 1) % MAX].tryAcquire())) {
                        tab[numer].release();
                    } else {
                        twoForkUp = true;
                    }
                } else {
                    tab[(numer + 1) % MAX].acquireUninterruptibly();
                    if (!(tab[numer].tryAcquire())) {
                        tab[(numer + 1) % MAX].release();
                    } else {
                        twoForkUp = true;
                    }
                }
            } while (twoForkUp == false);
            System.out.println("Filozof " + numer + " zaczyna jeść!");
            try {
                Thread.sleep((long)(1000 * Math.random()));
            } catch (InterruptedException e) {}
            System.out.println("Filozof " + numer + " kończy jeść!");
            tab[numer].release();
            tab[(numer + 1) % MAX].release();
        }
    }
    public void rozpocznij() {
        for (int i = 0; i < MAX; i++) {
            tab[i] = new Semaphore(1);
        }
        for (int i = 0; i < MAX; i++) {
            new Alg3(i).start();
        }
    }
}
