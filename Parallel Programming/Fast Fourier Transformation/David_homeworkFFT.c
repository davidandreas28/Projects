#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <math.h>
#include <complex.h>

#define MIN(x, y) (((x) < (y)) ? (x) : (y))

pthread_t* tid;
int* thread_id;
int count = 0;

struct myData{
	int N, P, padding, step;
	double complex* inputVec;
	double complex* outputVec;
	int id;
};

struct myData readInput(char** argv) {
	FILE *f1 = fopen(argv[1], "rt");
	if (f1 == NULL) {
    fprintf(stdout, "Failed to open at least one file.\n");
    exit(1);
    }

	double currDouble;
	struct myData* aux = malloc(sizeof(struct myData));
	int ret1 = fscanf(f1, "%d", &((*aux).N));
	if (ret1 != 1) {
		fprintf(stdout, "Unable to read N from the input file\n");
	}

	(*aux).P = atoi(argv[3]);
	(*aux).step = 1;

	for ((*aux).padding = 1; (*aux).padding < (*aux).N; (*aux).padding <<= 1);

	(*aux).inputVec = (double complex*) malloc (sizeof(double complex) * (*aux).padding);
	(*aux).outputVec = (double complex*) malloc (sizeof(double complex) * (*aux).padding);

	for (int i = 0; i < (*aux).N; ++i) {

		int retVal = fscanf(f1, "%lf", &currDouble);
		if (retVal == 0) {
			fprintf(stdout, "Unable to read double value from file\n");
		}
		(*aux).inputVec[i] = currDouble;
	}

	if ((*aux).N < (*aux).padding) {
		for (int i = (*aux).N; i < (*aux).padding; ++i) {
			(*aux).inputVec[i] = 0.0;
		}
	}

	fclose(f1);
	return *aux;
}

void completeGraph(struct myData* data) {
	if ((*data).P == 2) {
		for (int i = 0; i < (*data).padding; i += 2 * (*data).step) {
				double complex t = cexp(-I * M_PI * i / (*data).padding) * (*data).outputVec[i + (*data).step];
				(*data).inputVec[i / 2]     = (*data).outputVec[i] + t;
				(*data).inputVec[(i + (*data).padding)/2] = (*data).outputVec[i] - t;
		}
	} else if ((*data).P == 4) {
		
		for (int i = 0; i < (*data).padding; i += 4 * (*data).step) {
				double complex t = cexp(-I * M_PI * i / (*data).padding) * (*data).inputVec[i + 2 * (*data).step];
				(*data).outputVec[i / 2]     = (*data).inputVec[i] + t;
				(*data).outputVec[(i + (*data).padding)/2] = (*data).inputVec[i] - t;
		}

		for (int i = 0; i < (*data).padding; i += 4 * (*data).step) {
				double complex t = cexp(-I * M_PI * i / (*data).padding) * (*data).inputVec[i + 2 * (*data).step + 1];
				(*data).outputVec[i / 2 + 1]     = (*data).inputVec[i + 1] + t;
				(*data).outputVec[(i + (*data).padding)/2 + 1] = (*data).inputVec[i + 1] - t;
		}

		for (int i = 0; i < (*data).padding; i += 2 * (*data).step) {
				double complex t = cexp(-I * M_PI * i / (*data).padding) * (*data).outputVec[i + (*data).step];
				(*data).inputVec[i / 2]     = (*data).outputVec[i] + t;
				(*data).inputVec[(i + (*data).padding)/2] = (*data).outputVec[i] - t;
		}
	}
}

void* _fft1(void* mydata)
{	struct myData data = *(struct myData*) mydata;
	int n = data.N;
	int P = data.P;
	int padd = data.padding;
	int step = data.step;
	double complex* out = data.outputVec;
	double complex* buff = data.inputVec;
	
	struct myData fft1 = (struct myData){n, P, padd, step * 2, out, buff, 0};
	struct myData fft2 = (struct myData){n, P, padd, step * 2, out + step, buff + step, 0};

	if (step < padd) {
		_fft1(&fft1);
		_fft1(&fft2);

		for (int i = 0; i < padd; i += 2 * step) {
			double complex t = cexp(-I * M_PI * i / padd) * out[i + step];
			buff[i / 2]     = out[i] + t;
			buff[(i + padd)/2] = out[i] - t;
		}
	}
	return NULL;
}

void* _fft2(void* mydata)
{	struct myData data = *(struct myData*) mydata;
	int n = data.N;
	int P = data.P;
	int padd = data.padding;
	int step = data.step;
	double complex* out = data.outputVec;
	double complex* buff = data.inputVec;
	int id = data.id;
	
	struct myData fft1 = (struct myData){n, P, padd, step * 2, out, buff, 1};
	struct myData fft2 = (struct myData){n, P, padd, step * 2, out + step, buff + step, 0};

	if (id == 3) {
		pthread_create(tid + 2, NULL, _fft1, &fft1);	
	} else {
		pthread_create(tid + 1, NULL, _fft1, &fft1);
	}
	_fft1(&fft2);

	return NULL;
}

void* _fft3(void* mydata)
{	struct myData data = *(struct myData*) mydata;
	int n = data.N;
	int P = data.P;
	int padd = data.padding;
	int step = data.step;
	double complex* out = data.outputVec;
	double complex* buff = data.inputVec;
	
	struct myData fft1 = (struct myData){n, P, padd, step * 2, out, buff, 3};
	struct myData fft2 = (struct myData){n, P, padd, step * 2, out + step, buff + step, 0};

		pthread_create(tid + 3, NULL, _fft2, &fft1);
		_fft2(&fft2);

	return NULL;
}

void* fft(struct myData* data)
{
	for (int i = 0; i < (*data).padding; i++) (*data).outputVec[i] = (*data).inputVec[i];

	(*data).id = 0;

	if ((*data).P == 1) {
		pthread_create(tid + 0, NULL, _fft1, data);
	} else if ((*data).P == 2) {
		pthread_create(tid + 0, NULL, _fft2, data);
	} else if ((*data).P == 4) {
		pthread_create(tid + 0, NULL, _fft3, data);
	}

	return NULL;

}

void writeOutput(char** argv, struct myData data) {
	FILE *f2 = fopen(argv[2], "wt");
	
	if (f2 == NULL) {
    fprintf(stdout, "Failed to open at least one file.\n");
    exit(1);
    }

	fprintf(f2, "%d\n", data.N);

	for (int i = 0; i < (data).N; ++i) {
		fprintf(f2, "%lf %lf\n", creal(data.inputVec[i]), cimag(data.inputVec[i]));
	}
	fclose(f2);
}

void destruct(struct myData* data) {
	free((*data).inputVec);
	free((*data).outputVec);
}

int main(int argc, char * argv[]) {
	if (argc < 3) {
		fprintf(stdout, "Usage %s <file1> <file2> <NmbrOfThreads\n>", argv[0]);
		exit(-1);
	}

	struct myData data = readInput(argv);

	tid = (pthread_t*)malloc(sizeof(pthread_t) * data.P);
	thread_id = (int*)malloc(sizeof(int) * data.P);

	fft(&data);
	
	for (int i = 0; i < data.P; i++) {
        pthread_join(tid[i], NULL);
    }

	completeGraph(&data);
	writeOutput(argv, data);

	destruct(&data);
	free(tid);
	free(thread_id);
	return 0;
}
