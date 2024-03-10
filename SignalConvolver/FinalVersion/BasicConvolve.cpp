/*
CPSC501 FALL2023 Assignment 4 Submission 
Brett Gattinger 30009390
*/

#include <BasicConvolve.hpp>

BasicConvolve::BasicConvolve() {
    //DEFAULT CONSTRUCTOR
    //currently nothing to initalize
}

std::vector<int16_t> BasicConvolve::convoleSignals(std::vector<int16_t> & inputSignal, std::vector<int16_t> & impulseResponse) {
    std::vector<int16_t> convovledSignal;

    //normalize input and impluse response signals
    std::vector<double> normalizedInputSignal = normalizeSignal(inputSignal);
    std::vector<double> normalizedImpulseResponse = normalizeSignal(impulseResponse);

    //DEBUGGING
    size_t a = normalizedInputSignal.size();
    size_t b = normalizedImpulseResponse.size();

    //convolute
    //this might result in some values being out of [-1,1] range find max ABSOLUTE value and just divide by that to bring back in range
    //track this max as you do the convolution then just divide at the end to bring back in range
    std::vector<double> convovledSignal_normalized;
    convovledSignal_normalized.clear();

    //DEBUGGING
    size_t d = inputSignal.size();
    size_t c = impulseResponse.size();

    convovledSignal_normalized.resize( (inputSignal.size() + impulseResponse.size() - 1), 0);
    double lastAbsMax = 1;
    for(int i = 0; i < normalizedInputSignal.size(); i++) {
        for (int j = 0;  j < normalizedImpulseResponse.size(); j++) {
            convovledSignal_normalized[i+j] += normalizedInputSignal[i] * normalizedImpulseResponse[j];
            if (abs(convovledSignal_normalized[i+j]) > lastAbsMax) {
                lastAbsMax = abs(convovledSignal_normalized[i+j]);
            }
        }
    }
    for(int i = 0; i < convovledSignal_normalized.size(); i++) {
        convovledSignal_normalized[i] /= lastAbsMax;
    }

    //denormalize the normalized convoluted signal
    convovledSignal = denormalizeSignal(convovledSignal_normalized);

    return convovledSignal;
}

//DEV NOTE:
//normalization of all data will be done using double data type

//November 30th:
//this is currently returning an empty vector 
    // SOLVED ~ w/ std::vector<double> normalizedSignal(inSignal.size()); <-- but why though?
//now we see normalizeSignal[i] = -1 for all i
    // SOLVED ~ integer division was being used, casted the constants and sample value to doubles
// now the values are not normalized to range [-1,1]
    // SOLVED ~ error in normalization formula, was subtracting SAMPLE_MAX in numerator instead of SAMPLE_MIN
std::vector<double> BasicConvolve::normalizeSignal(std::vector<int16_t> & inSignal) {
    std::vector<double> normalizedSignal(inSignal.size());
    normalizedSignal.reserve(inSignal.size());
    for (int i = 0; i < inSignal.size(); i++) {
        double sample = inSignal[i];
        normalizedSignal[i] = ( double(2) * ( (sample - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1); 
    }
    return normalizedSignal;
}

std::vector<int16_t> BasicConvolve::denormalizeSignal(std::vector<double> & inSignal) {
    std::vector<int16_t> denormalizedSignal(inSignal.size());
    denormalizedSignal.reserve(inSignal.size());
    for (int i = 0; i < inSignal.size(); i++) {
        double normalizedSample = inSignal[i];
        denormalizedSignal[i] = int16_t( (normalizedSample - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
    }
    return denormalizedSignal;
}
