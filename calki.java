class Watkowa extends Thread
{
    private double wynik;
    private double x;
    
    public Watkowa(double x)
    {
        this.x = x;
    }

    public double zwrocWynik()
    {
        return wynik;
    }

    public void run()
    {
        wynik = Math.cos(x) - Math.exp(x);
    }
}

class Prostokaty
{
    public static double wyliczCalke(double poczatekPrzedzialu, double koniecPrzedzialu, int n)
    {
        double zmiennaWynikowa = 0;
        double dx = (koniecPrzedzialu - poczatekPrzedzialu) / n;
        Watkowa[] tab = new Watkowa[n];
        for (int i=0; i<n; i++)
        {
            tab[i] = new Watkowa(dx*(i+1));
            tab[i].start();
        }
        for (int i=0; i<n; i++)
        {
            try
            {
                tab[i].join();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            zmiennaWynikowa = zmiennaWynikowa + tab[i].zwrocWynik();
        }
        zmiennaWynikowa *= dx;
        return zmiennaWynikowa;
    }
}

class Simpson
{
    public static double wyliczCalke(double poczatekPrzedzialu, double koniecPrzedzialu, int n)
    {
        double zmiennaWynikowa = 0;
        double dx = (koniecPrzedzialu - poczatekPrzedzialu) / n;
        Watkowa f0 = new Watkowa(poczatekPrzedzialu);
        Watkowa fn = new Watkowa(koniecPrzedzialu);
        Watkowa [] fi = new Watkowa[n-1];
        Watkowa [] fti = new Watkowa[n];
        f0.start();
        fn.start();
        for (int i=1; i<=n-1; i++)
        {
            double x = poczatekPrzedzialu + i * dx;
            fi[i-1] = new Watkowa(x);
            fi[i-1].start();
        }
        for (int i=1; i<=n; i++)
        {
            double a = poczatekPrzedzialu + (i-1) * dx;
            double b = poczatekPrzedzialu + (i+1) * dx;
            double x = (a + b) / 2;
            fti[i-1] = new Watkowa(x);
            fti[i-1].start();
        }
        try
        {
            f0.join();
            zmiennaWynikowa += f0.zwrocWynik();
            fn.join();
            zmiennaWynikowa += fn.zwrocWynik();
            for (int i=0; i<fi.length; i++)
            {
                zmiennaWynikowa += 2 * fi[i].zwrocWynik();
            }
            for (int i=0; i<fti.length; i++)
            {
                zmiennaWynikowa += 4 * fti[i].zwrocWynik();
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        zmiennaWynikowa *= dx/6;
        return zmiennaWynikowa;
    }
}

class Trapezy
{
    public static double wyliczCalke(double poczatekPrzedzialu, double koniecPrzedzialu, int n)
    {
        double zmiennaWynikowa = 0;
        double dx = (koniecPrzedzialu - poczatekPrzedzialu) / n;
        Watkowa[] tab = new Watkowa[n+1];
        for (int i=0; i<n+1; i++)
        {
            double x = poczatekPrzedzialu + dx * i;
            Watkowa f = new Watkowa(x);
            f.start();
            tab[i] = f;
        }
        for (int i=0; i<n+1; i++)
        {
            try
            {
                tab[i].join();
                if (i==0 || i==n)
                {
                    zmiennaWynikowa += tab[i].zwrocWynik()/2;
                }
                else
                {
                    zmiennaWynikowa += tab[i].zwrocWynik();
                }
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return zmiennaWynikowa * dx;
    }
}

public class calki
{
    public static void main(String[] args)
    {
        double wynik1 = Prostokaty.wyliczCalke(0, Math.PI / 2, 555);
        double wynik2 = Simpson.wyliczCalke(0, Math.PI / 2, 555);
        double wynik3 = Trapezy.wyliczCalke(0, Math.PI / 2, 555);

        System.out.format("Metoda prostokątów: %.20f%n", wynik1);
        System.out.format("Metoda Simpsona: %.20f%n", wynik2);
        System.out.format("Metoda trapezów: %.20f%n", wynik3);
    }
}
