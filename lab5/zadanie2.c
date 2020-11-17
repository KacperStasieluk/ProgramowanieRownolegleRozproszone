#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "err.h"

int main (int argc, char *argv[])
{
    srand((unsigned int)time(NULL));
    int liczbaProcesow = atoi(argv[1]);
    int przedzialOdIlu = atoi(argv[2]);
    int przedzialDoIlu = atoi(argv[3]);
    double H;
    pid_t pid;
    printf("PID = %d\n", getpid());

    for (int i = 1; i <= liczbaProcesow; i++)
    {
        double A = 0.0;
        double B = 0.0;
        int n=rand()%przedzialDoIlu+przedzialOdIlu;

        while(A >= B)
        {
            A = rand()%przedzialDoIlu+przedzialOdIlu;
            B = rand()%przedzialDoIlu+przedzialOdIlu;
        }

        switch (pid = fork()) 
        {
            case -1:
                printf("Wystąpił błąd! \n");
            case 0:
                    H = (B-A)/n;
                    double xi[n-1];
                    double tab[n+1];

                    for(int i=1; i<n; i++) 
                    {
                        xi[i-1] = A + ((i / (double)n) * (B - A));
                    }

                    tab[0]=(4 * pow(A, 2) - 6 * A + 5) / 2;
                    tab[n]=(4 * pow(B, 2) - 6 * B + 5) / 2;

                    for(i=1; i<n; i++)
                    {
                        tab[i]=(4 * pow(xi[i-1], 2) - 6 * xi[i-1] + 5);
                    }

                    double suma = 0;
                    for(int i=0; i<n+1; i++)
                    {
                        suma += tab[i];
                    }

                    double wynik = suma*H;
                    printf("PID procesu: %d\n", getpid());
                    printf("Dane wejściowe: \n");
                    printf("A = %.4lf \n", A);
                    printf("B = %.4lf \n", B);
                    printf("n = %d\n", n);
                    printf("Wynik: %.5lf\n\n", wynik);

                return 0;
        }
    }
    
    for (int i = 1; i <= liczbaProcesow; i++) 
    {
        if (wait(0) == -1) printf("Wystąpił błąd! \n");
    }
    
    return 0;
}
