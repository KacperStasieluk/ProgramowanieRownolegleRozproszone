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
    pid_t pid;
    printf("PID = %d\n", getpid());

    for (int i = 1; i <= liczbaProcesow; i++)
    {
        srand(getpid()+getppid());
        
        double pi = 1.0;
        int N;
        int n = rand()%5000+100;

        switch (pid = fork()) 
        {
            case -1:
                printf("Wystąpił błąd! \n");
            case 0:
                    for (int i=3, N=2*n+1; i<=N; i+=2)
                    {
                        pi += ((i&2) ? -1.0 : 1.0) / i;
                    }

                    printf("%f",4*pi);
                    printf("\n");

                return 0;
        }
    }
    
    for (int i = 1; i <= liczbaProcesow; i++) 
    {
        if (wait(0) == -1) printf("Wystąpił błąd! \n");
    }
    
    return 0;
}
