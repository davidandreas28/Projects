#include <stdio.h>
#include <complex.h>
#include <stdlib.h>
#include <pthread.h>
#include <math.h>

#define MIN(x, y) (((x) < (y)) ? (x) : (y))

int N, P;
double* inputVec;
double complex* outputVec;

void* threadFunction(void *args)
{
    int tid     = *(int*)args;
    int start   = tid * ceil((double)N / P);
    int end     = MIN(N, (tid + 1) * ceil((double)N / P));

    for (int i = start; i < end; ++i) {
		double complex aux = 0;
		for (int j = 0; j < N; ++j) {
			aux += inputVec[j] * cexp((((-2) * M_PI * i * I) / N) * j);
		}
		outputVec[i] = aux;
	}
    
    return NULL;
}

void seq_FT() {
	for (int i = 0; i < N; ++i) {
		double complex aux = 0;
		for (int j = 0; j < N; ++j) {
			aux += inputVec[j] * cexp((((-2) * M_PI * i * I) / N) * j);
		}
		outputVec[i] = aux;
	}
}

void readInput(char** argv) {
	FILE *f1 = fopen(argv[1], "rt");
	if (f1 == NULL) {
    fprintf(stdout, "Failed to open at least one file.\n");
    exit(1);
    }

	double currDouble;
	int ret1 = fscanf(f1, "%d", &N);
	if (ret1 != 1) {
		fprintf(stdout, "Unable to read N from the input file\n");
	}

	inputVec = (double*) malloc (sizeof(double) * N);
	outputVec = (complex*) malloc (sizeof(complex) * N);

	for (int i = 0; i < N; ++i) {

		int retVal = fscanf(f1, "%lf", &currDouble);
		if (retVal == 0) {
			fprintf(stdout, "Unable to read double value from file\n");
		}
		inputVec[i] = currDouble;
	}

	fclose(f1);
}

void writeOutput(char** argv) {
	FILE *f2 = fopen(argv[2], "wt");
	
	if (f2 == NULL) {
    fprintf(stdout, "Failed to open at least one file.\n");
    exit(1);
    }

	fprintf(f2, "%d\n", N);

	for (int i = 0; i < N; ++i) {
		fprintf(f2, "%lf %lf\n", creal(outputVec[i]), cimag(outputVec[i]));
	}
	fclose(f2);
}

void freeMem() {
	free(inputVec);
	free(outputVec);
}

int main(int argc, char * argv[]) {
	if (argc < 3) {
		fprintf(stdout, "Usage %s <file1> <file2> <NmbrOfThreads\n>", argv[0]);
		exit(-1);
	}

	P = atoi(argv[3]);

	pthread_t tid[P];
	int thread_id[P];
	

	readInput(argv);

	for (int i = 0; i < P; ++i) {
		thread_id[i] = i;
	}

	for (int i = 0; i < P; i++)
    {
        pthread_create(tid + i, NULL, threadFunction, thread_id + i);
    }
	
	for (int i = 0; i < P; i++)
    {
        pthread_join(tid[i], NULL);
    }

	// abordare secventiala
	// seq_FT();

	writeOutput(argv);

	freeMem();
	return 0;
}
