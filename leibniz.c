#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include "mpi.h"

int main(int argc, char **argv)
{
    int n, p;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &p);
    MPI_Comm_size(MPI_COMM_WORLD, &n);
    MPI_Status status;

    float pi=0.0, liczba=0.0;
    int tag=50;
    
    if((p > 0) && (p < n))
    {

        MPI_Recv(&pi, 1, MPI_INT, p-1, tag, MPI_COMM_WORLD, &status);
        liczba =( pow( - 1.0, p ) ) /( 2 * p + 1 );
        pi += liczba;
        printf("Dane: n = %d, PI = %f\n", p, pi*4);
        
        if(p != n - 1)
        { 
            MPI_Send(&pi, 1, MPI_INT ,p+1 ,tag, MPI_COMM_WORLD);
        }   
    }  
    
    else if(p == 0)
    {
        liczba =( pow( - 1.0, p ) ) /( 2 * p + 1 );
        pi += liczba;
        printf("Dane: n = %d, PI = %f\n", p, pi*4);
        MPI_Send(&pi, 1, MPI_INT ,p+1 ,tag, MPI_COMM_WORLD);
        MPI_Send(&pi, 1, MPI_INT ,p+1 ,tag, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}