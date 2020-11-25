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
    
    double x;
    float suma=0.0, a=1.0, b=4.0, h;
    int tag=50;

    h=(b-a)/n;

    if(p == n - 1)
    { 
        printf("\n PROCES: %d\n",p);
        printf("a = %lf\n",a);
        printf("b = %lf\n",b);
        printf("H = %lf\n\n",h);

        suma=suma+pow(a,2)/2;
        printf("SUMA = %lf\n",suma);
        suma=suma+pow(b,2)/2;
        printf("SUMA = %lf\n",suma);

        x=b;
        MPI_Send(&suma, 1, MPI_DOUBLE, p-1,tag, MPI_COMM_WORLD);
        MPI_Send(&x, 1, MPI_DOUBLE, p-1,tag, MPI_COMM_WORLD);
    }

    else if((p > 0) && (p < n - 1))
    { 
        MPI_Recv(&suma, 1, MPI_DOUBLE, p+1, tag,MPI_COMM_WORLD, &status);
        MPI_Recv(&x, 1, MPI_DOUBLE, p+1, tag,MPI_COMM_WORLD, &status);

        x -= h;
        suma=suma+pow(x,2);
    
        printf("\n PROCES: %d\n",p);
        printf("SUMA: %lf\n",suma);

        MPI_Send(&suma, 1, MPI_DOUBLE, p-1,tag, MPI_COMM_WORLD);
        MPI_Send(&x, 1, MPI_DOUBLE, p-1,tag, MPI_COMM_WORLD);
    }
    
    else if(p==0){ 
        MPI_Recv(&suma, 1, MPI_DOUBLE, p+1, tag,MPI_COMM_WORLD, &status);
        MPI_Recv(&x, 1, MPI_DOUBLE, p+1, tag,MPI_COMM_WORLD, &status);

        x -= h;
        suma=suma+pow(x,2);

        printf("\n PROCES: %d\n",p);
        printf("WYNIK: %lf\n",suma*h);       
    }   
      

    MPI_Finalize();
    return 0;
} 