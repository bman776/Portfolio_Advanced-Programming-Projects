/*
CPSC501 FALL2023 Assignment 4 Submission 
Brett Gattinger 30009390
*/
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
#include <tuple>

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
    static constexpr double SAMPLE_MAX_D = 32767.0;
    static constexpr double SAMPLE_MIN_D = -32768.0;
    static constexpr double NORM_ONE = 1.0;
    static constexpr double NORM_TWO = 2.0;
    static constexpr double NORM_TWO_NEG = -2.0;
    static constexpr double NORM_DENOM = SAMPLE_MAX_D - SAMPLE_MIN_D;

    //segment size used FFT operations, must be a power of 2
    //in this case we have chosen 2^10 = 1024
    static const size_t SEGMENT_SIZE = 1024;
    
    public:

        std::vector<std::tuple<double,double,double>> cachedFFTSegmentCalculations;
        std::vector<std::tuple<double,double,double>> cachedIFFTSegmentCalculations;

        FFTconvolve();
        std::vector<int16_t> convolveSignals(std::vector<int16_t> & inputSignal, std::vector<int16_t> & impulseResponse);
        inline size_t nextPowOfTwo(size_t x);
        std::vector<double> normalizeSignal(std::vector<int16_t> & inSignal);
        std::vector<double> normalizeSignal_unrolled(std::vector<int16_t> & inSignal);
        std::vector<double> normalizeSignal_unrolled_optimized(std::vector<int16_t> & inSignal);
        std::vector<int16_t> denormalizeSignal(std::vector<double> & inSignal);
        std::vector<int16_t> denormalizeSignal_unrolled(std::vector<double> & inSignal);
        std::vector<int16_t> denormalizeSignal_unrolled_optimized(std::vector<double> & inSignal);
        void FFT(double data[], int nn, int isign);
        void FFT_cached(double data[], int nn, int isign);
        void initialize_cachedFFTSegmentCalculations(size_t expectedConvolvedArraySegmentSize);
        void initialize_cachedIFFTSegmentCalculations(size_t expectedConvolvedArraySegmentSize);
};


#endif