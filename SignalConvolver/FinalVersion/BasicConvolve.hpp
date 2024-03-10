/*
CPSC501 FALL2023 Assignment 4 Submission 
Brett Gattinger 30009390
*/
#ifndef BASICCONVOLVE_H
#define BASICCONVOLVE_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <cstdint>

#include <WaveReader.hpp>
#include <WaveWriter.hpp>

typedef unsigned char BYTE;

class BasicConvolve {
    static const int16_t SAMPLE_MAX = 32767;
    static const int16_t SAMPLE_MIN = -32768;
    public:
        BasicConvolve();
        std::vector<int16_t> convoleSignals(std::vector<int16_t> & inputSignal, std::vector<int16_t> & impulseResponse);
        std::vector<double> normalizeSignal(std::vector<int16_t> & inSignal);
        std::vector<int16_t> denormalizeSignal(std::vector<double> & inSignal);
};


#endif