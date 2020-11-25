#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "mpi.h"

#define POSToJ 1
#define ROZPOCZeCIE_KURSU 2 
#define KURS 3
#define KONIEC_KURSU 4
#define WYPADEK 5
#define REZERWA 500
#define TANKUJ 5000

int paliwo = 5000;
int ZJEZDZAJ = 1, NIE_ZJEZDZAJ = 0;
int liczba_procesow;
int nr_procesu;
int ilosc_taksowek;
int ilosc_miejsc = 4;
int ilosc_zajetych_miejsc = 0;
int tag = 1;

int wyslij[2];
int odbierz[2];

MPI_Status mpi_status;

void Wyslij(int nr_taksowki, int stan) //wyslij do lotniska swoj stan
{
  wyslij[0] = nr_taksowki;
  wyslij[1] = stan;
  MPI_Send( & wyslij, 2, MPI_INT, 0, tag, MPI_COMM_WORLD);
  sleep(1);
}

void KorporacjaTaksowkarska(int liczba_procesow) {
  int nr_taksowki, status;
  ilosc_taksowek = liczba_procesow - 1;
  printf("Tu dystrybutor korporacji taksowkarskiej \n");
  if (rand() % 2 == 1) {
    printf("Ladna pogoda zwiastuje mnostwo klientow \n");
  } else {
    printf("Dzisiejsza pogoda wymusza zwiekszona ostroZność na kierowcach \n");
  }
  printf("Zycze udanych kursow \n \n \n");
  printf("Dysponujemy %d miejscami postojowymi \n", ilosc_miejsc);
  sleep(2);
  while (ilosc_miejsc <= ilosc_taksowek) {
    MPI_Recv( & odbierz, 2, MPI_INT, MPI_ANY_SOURCE, tag, MPI_COMM_WORLD, & mpi_status);
    nr_taksowki = odbierz[0];
    status = odbierz[1];
    if (status == 1) {
      printf("Taksowka %d stoi na parkingu \n", nr_taksowki);
    }
    if (status == 2) {
      printf("Taksowka %d odjezdza z miejsca postojowego nr %d\n", nr_taksowki, ilosc_zajetych_miejsc);
      ilosc_zajetych_miejsc--;
    }
    if (status == 3) {
      printf("Taksowka %d wyruszyla! \n", nr_taksowki);
    }
    if (status == 4) {
      if (ilosc_zajetych_miejsc < ilosc_miejsc) {
        ilosc_zajetych_miejsc++;
        MPI_Send( & ZJEZDZAJ, 1, MPI_INT, nr_taksowki, tag, MPI_COMM_WORLD);
      } else {
        MPI_Send( & NIE_ZJEZDZAJ, 1, MPI_INT, nr_taksowki, tag, MPI_COMM_WORLD);
      }
    }
    if (status == 5) {
      ilosc_taksowek--;
      printf("Ilosc taksowek %d \n", ilosc_taksowek);
    }
  } //zamykam while
  printf("Program zakonczyl dzialanie \n");
}
void Taksowka() {
  int stan, suma, i;
  stan = KURS; //to chyba jedyny rozsadny stan z jakiego warto startowac
  while (1) {
    if (stan == 1) {
      if (rand() % 2 == 1) {
        stan = ROZPOCZeCIE_KURSU;
        paliwo = TANKUJ;
        printf("Jestem gotowy do przyjecia zgloszenia, taksowka %d\n", nr_procesu);
        Wyslij(nr_procesu, stan);
      } else {
        Wyslij(nr_procesu, stan);
      }
    } else if (stan == 2) {
      printf("WyruszyLem, taksowka %d\n", nr_procesu);
      stan = KURS;
      Wyslij(nr_procesu, stan);
    } else if (stan == 3) {
      paliwo -= rand() % 500;
      if (paliwo <= REZERWA) {
        stan = KONIEC_KURSU;
        printf("Zjezdzam na postoj \n");
        Wyslij(nr_procesu, stan);
      } else {
        for (i = 0; rand() % 10000; i++);
      }
    } else if (stan == 4) {
      int temp;
      MPI_Recv( & temp, 1, MPI_INT, 0, tag, MPI_COMM_WORLD, & mpi_status);
      if (temp == ZJEZDZAJ) {
        stan = POSToJ;
        printf("Zjechalem na postoj, taksowka %d\n", nr_procesu);
      } else {
        paliwo -= rand() % 500;
        if (paliwo > 0) {
          Wyslij(nr_procesu, stan);
        } else {
          stan = WYPADEK;
          printf("Mamy wypadek! Niby zawodowy kierowca, a auto rozwalone :( \n");
          Wyslij(nr_procesu, stan);
          return;
        }
      }
    }
  }
}
int main(int argc, char * argv[]) {
  MPI_Init( & argc, & argv);
  MPI_Comm_rank(MPI_COMM_WORLD, & nr_procesu);
  MPI_Comm_size(MPI_COMM_WORLD, & liczba_procesow);

  srand(time(NULL));

  if (nr_procesu == 0)
    KorporacjaTaksowkarska(liczba_procesow);
  else
    Taksowka();
    
  MPI_Finalize();
  return 0;
}