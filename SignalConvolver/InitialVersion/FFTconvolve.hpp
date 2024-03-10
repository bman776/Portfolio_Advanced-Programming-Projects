#ifndef FFTCONVOLVE_H
#define FFTCONVOLVE_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <cstdint>

#include <iostream>
#include <fstream>
#include <string>
#include <array>
#include <vector>

#include <algorithm>
#include <iterator>

typedef unsigned char BYTE;

#define SIZE       8
#define PI         3.141592653589793
#define TWO_PI     (2.0 * PI)
#define SWAP(a,b)  tempr=(a);(a)=(b);(b)=tempr

class FFTconvolve {
    static const int16_t SAMPLE_MAX = 32767;
    static const int16_t SAMPLE_MIN = -32768;

    //segment size used FFT operations, must be a power of 2
    //in this case we have chosen 2^10 = 1024
    static const size_t SEGMENT_SIZE = 1024;
    
    public:
        FFTconvolve();
        std::vector<int16_t> convolveSignals(std::vector<int16_t> & inputSignal, std::vector<int16_t> & impulseResponse);
        size_t nextPowOfTwo(size_t x);
        std::vector<double> normalizeSignal(std::vector<int16_t> & inSignal);
        std::vector<int16_t> denormalizeSignal(std::vector<double> & inSignal);

        void FFT(double data[], int nn, int isign);
};


#endif