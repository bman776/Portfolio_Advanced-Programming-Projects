/*
CPSC501 FALL2023 Assignment 4 Submission 
Brett Gattinger 30009390
*/

#include <FFTconvolve.hpp>

FFTconvolve::FFTconvolve() {
    //DEFAULT CONSTRUCTOR
}

std::vector<int16_t> FFTconvolve::convolveSignals(std::vector<int16_t> & inputSignal, std::vector<int16_t> & impulseResponse) {

    //The result to be returned
    std::vector<int16_t> convolvedSignal;
    std::vector<double> convolvedSignal_normalized;

    //normalize input and impluse response signals
    std::vector<double> normalizedInputSignal = normalizeSignal_unrolled_optimized(inputSignal);
    std::vector<double> normalizedImpulseResponse = normalizeSignal_unrolled_optimized(impulseResponse);

    //calcualte size of arrays and quantities used in FFT and IFFT operations
    size_t expectedConvolvedArraySegmentSize = SEGMENT_SIZE + normalizedImpulseResponse.size()-1;
    size_t FFT_dataOpSize = nextPowOfTwo(expectedConvolvedArraySegmentSize);
    size_t FFT_arrayOpSize = 2 * FFT_dataOpSize;

    //precompute calculations used in FFT alogrithm and store them in cache to be reused
    initialize_cachedFFTSegmentCalculations(expectedConvolvedArraySegmentSize);
    initialize_cachedIFFTSegmentCalculations(expectedConvolvedArraySegmentSize);

    //apply FFT to impulse response to get frequency response
    double *frequencyResponse = new double[FFT_arrayOpSize]();
    size_t endOfLoadedDataIndex = 0;
    for (size_t i = 0, j = 0; j < normalizedImpulseResponse.size(); i+=2, j++) {
        frequencyResponse[i] = normalizedImpulseResponse[j];
        frequencyResponse[i+1] = 0;
        endOfLoadedDataIndex = i+1;
    }
    for (size_t i = endOfLoadedDataIndex; i < FFT_arrayOpSize; i++) {
        frequencyResponse[i] = 0;
    }
    FFT_cached(frequencyResponse-1, FFT_dataOpSize, 1);

    //pad input signals with 0's so that it matches next greatest multiple of segment size (1024 samples per segement)
    for (int i = 0; i < int(normalizedInputSignal.size() % SEGMENT_SIZE); i++) {
        normalizedInputSignal.push_back(0);
    }
    int numberOfSegments = int(normalizedInputSignal.size()/SEGMENT_SIZE);

    //apply FFT to input signal in segment sizes of 1024 samples complex-multiply with the frequency response and overlap add the result into convolved signal array
    std::vector<double>::iterator segmentCursor = normalizedInputSignal.begin();
    for (int i = 0; i < numberOfSegments; i++) {

        //load in next segment of input signal
        double *frequencySpectrumSegment = new double[FFT_arrayOpSize]();
        endOfLoadedDataIndex = 0;
        for (size_t j = 0, k = 0; k < SEGMENT_SIZE; j+=2, k++) {
            frequencySpectrumSegment[j] = *segmentCursor;
            frequencySpectrumSegment[j+1] = 0;
            endOfLoadedDataIndex = j+1;
            std::advance(segmentCursor, 1);
        }
        for (size_t j = endOfLoadedDataIndex; j < FFT_arrayOpSize; j++) {
            frequencySpectrumSegment[j] = 0;
        }
        FFT_cached(frequencySpectrumSegment-1, FFT_dataOpSize, 1);

        //multiply in the frequency domain to convolve
        double *convolvedSpectrumSegment = new double[FFT_arrayOpSize]();
        for (size_t j = 0; j < FFT_arrayOpSize; j+=2) {
            convolvedSpectrumSegment[j] = (frequencySpectrumSegment[j] * frequencyResponse[j]) - (frequencySpectrumSegment[j+1] * frequencyResponse[j+1]);
            convolvedSpectrumSegment[j+1] = (frequencySpectrumSegment[j] * frequencyResponse[j+1]) + (frequencySpectrumSegment[j+1] * frequencyResponse[j]);
        }

        //get back the convolved segment of the input signal
        FFT_cached(convolvedSpectrumSegment-1, FFT_dataOpSize, -1);

        //extract and scale real part 
        double convolvedSpectrumSegment_real[expectedConvolvedArraySegmentSize];
        for (int j = 0, k = 0; k < expectedConvolvedArraySegmentSize; j+=2, k++) {
            convolvedSpectrumSegment_real[k] = convolvedSpectrumSegment[j] / double(normalizedInputSignal.size());
        }

        //overlap add to normalized output
        if (convolvedSignal_normalized.empty()) {
            convolvedSignal_normalized.insert(convolvedSignal_normalized.end(), 
                &convolvedSpectrumSegment_real[0], 
                &convolvedSpectrumSegment_real[expectedConvolvedArraySegmentSize]);
        } else {
            for (int j = 0; j < normalizedImpulseResponse.size(); j++) {
                convolvedSignal_normalized[ ((convolvedSignal_normalized.size() - 1) - (normalizedImpulseResponse.size())) + j] += convolvedSpectrumSegment_real[j]; 
            }
            convolvedSignal_normalized.insert(convolvedSignal_normalized.end(), 
                &convolvedSpectrumSegment_real[normalizedImpulseResponse.size()-1], 
                &convolvedSpectrumSegment_real[expectedConvolvedArraySegmentSize]);
        }
    }

    //denormalize output and return
    convolvedSignal = denormalizeSignal_unrolled_optimized(convolvedSignal_normalized);
    return convolvedSignal;
}

std::vector<double> FFTconvolve::normalizeSignal(std::vector<int16_t> & inSignal) {
    std::vector<double> normalizedSignal(inSignal.size());
    normalizedSignal.reserve(inSignal.size());
    for (int i = 0; i < inSignal.size(); i++) {
        double sample = inSignal[i];
        normalizedSignal[i] = ( double(2) * ( (sample - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1); 
    }
    return normalizedSignal;
}

std::vector<double> FFTconvolve::normalizeSignal_unrolled(std::vector<int16_t> & inSignal) {
    size_t inSignalSize = inSignal.size();
    std::vector<double> normalizedSignal(inSignalSize);
    normalizedSignal.reserve(inSignalSize);
    size_t i;
    for (i = 0; i < inSignalSize-3; i+=4) {
        double sample1 = inSignal[i];
        double sample2 = inSignal[i+1];
        double sample3 = inSignal[i+2];
        double sample4 = inSignal[i+3];
        normalizedSignal[i] = ( double(2) * ( (sample1 - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
        normalizedSignal[i+1] = ( double(2) * ( (sample2 - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
        normalizedSignal[i+2] = ( double(2) * ( (sample3 - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
        normalizedSignal[i+3] = ( double(2) * ( (sample4 - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1); 
    }
    if (i == inSignalSize - 3) {
        double sampleB = inSignal[inSignalSize - 3]; 
        double sampleC = inSignal[inSignalSize - 2];
        double sampleD = inSignal[inSignalSize - 1];
        normalizedSignal[inSignalSize-3] = ( double(2) * ( (sampleB - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
        normalizedSignal[inSignalSize-2] = ( double(2) * ( (sampleC - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
        normalizedSignal[inSignalSize-1] = ( double(2) * ( (sampleD - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
    } else if (i == inSignalSize - 2) {
        double sampleC = inSignal[inSignalSize - 2];
        double sampleD = inSignal[inSignalSize - 1]; 
        normalizedSignal[inSignalSize-2] = ( double(2) * ( (sampleC - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
        normalizedSignal[inSignalSize-1] = ( double(2) * ( (sampleD - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
    } else if (i == inSignalSize - 1) {
        double sampleD = inSignal[inSignalSize - 1]; 
        normalizedSignal[inSignalSize-1] = ( double(2) * ( (sampleD - double(SAMPLE_MIN)) / (double(SAMPLE_MAX) - double(SAMPLE_MIN)) ) ) - double(1);
    }
    return normalizedSignal;
}

std::vector<double> FFTconvolve::normalizeSignal_unrolled_optimized(std::vector<int16_t> & inSignal) {
    size_t inSignalSize = inSignal.size();
    std::vector<double> normalizedSignal(inSignalSize);
    normalizedSignal.reserve(inSignalSize);
    size_t i;
    for (i = 0; i < inSignalSize-3; i+=4) {
        normalizedSignal[i] = ( NORM_TWO * ( (inSignal[i] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
        normalizedSignal[i+1] = ( NORM_TWO * ( (inSignal[i+1] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
        normalizedSignal[i+2] = ( NORM_TWO * ( (inSignal[i+2] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
        normalizedSignal[i+3] = ( NORM_TWO * ( (inSignal[i+3] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE; 
    }
    if (i == inSignalSize - 3) {
        normalizedSignal[inSignalSize-3] = ( NORM_TWO * ( (inSignal[inSignalSize - 3] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
        normalizedSignal[inSignalSize-2] = ( NORM_TWO * ( (inSignal[inSignalSize - 2] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
        normalizedSignal[inSignalSize-1] = ( NORM_TWO * ( (inSignal[inSignalSize - 1] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
    } else if (i == inSignalSize - 2) { 
        normalizedSignal[inSignalSize-2] = ( NORM_TWO * ( (inSignal[inSignalSize - 2] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
        normalizedSignal[inSignalSize-1] = ( NORM_TWO * ( (inSignal[inSignalSize - 1] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
    } else if (i == inSignalSize - 1) {
        normalizedSignal[inSignalSize-1] = ( NORM_TWO * ( (inSignal[inSignalSize - 1] - SAMPLE_MIN_D) / NORM_DENOM ) ) - NORM_ONE;
    }
    return normalizedSignal;
}

std::vector<int16_t> FFTconvolve::denormalizeSignal(std::vector<double> & inSignal) {
    std::vector<int16_t> denormalizedSignal(inSignal.size());
    denormalizedSignal.reserve(inSignal.size());
    for (int i = 0; i < inSignal.size(); i++) {
        double normalizedSample = inSignal[i];
        denormalizedSignal[i] = int16_t( (normalizedSample - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
    }
    return denormalizedSignal;
}

std::vector<int16_t> FFTconvolve::denormalizeSignal_unrolled(std::vector<double> & inSignal) {
    size_t inSignalSize = inSignal.size();
    std::vector<int16_t> denormalizedSignal(inSignalSize);
    size_t i;
    denormalizedSignal.reserve(inSignalSize);
    for (i = 0; i < inSignalSize-3; i+=4) {
        double normalizedSample1 = inSignal[i];
        double normalizedSample2 = inSignal[i+1];
        double normalizedSample3 = inSignal[i+2];
        double normalizedSample4 = inSignal[i+3];
        denormalizedSignal[i] = int16_t( (normalizedSample1 - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
        denormalizedSignal[i+1] = int16_t( (normalizedSample2 - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
        denormalizedSignal[i+2] = int16_t( (normalizedSample3 - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
        denormalizedSignal[i+3] = int16_t( (normalizedSample4 - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
    }
    if (i == inSignalSize - 3) {
        double sampleB = inSignal[inSignalSize - 3]; 
        double sampleC = inSignal[inSignalSize - 2];
        double sampleD = inSignal[inSignalSize - 1];
        denormalizedSignal[inSignalSize-3] = int16_t( (sampleB - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
        denormalizedSignal[inSignalSize-2] = int16_t( (sampleC - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
        denormalizedSignal[inSignalSize-1] = int16_t( (sampleD - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
    } else if (i == inSignalSize - 2) {
        double sampleC = inSignal[inSignalSize - 2];
        double sampleD = inSignal[inSignalSize - 1]; 
        denormalizedSignal[inSignalSize-2] = int16_t( (sampleC - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
        denormalizedSignal[inSignalSize-1] = int16_t( (sampleD - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
    } else if (i == inSignalSize - 1) {
        double sampleD = inSignal[inSignalSize - 1]; 
        denormalizedSignal[inSignalSize-1] = int16_t( (sampleD - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
    }
    return denormalizedSignal;
}

std::vector<int16_t> FFTconvolve::denormalizeSignal_unrolled_optimized(std::vector<double> & inSignal) {
    size_t inSignalSize = inSignal.size();
    std::vector<int16_t> denormalizedSignal(inSignalSize);
    size_t i;
    denormalizedSignal.reserve(inSignalSize);
    for (i = 0; i < inSignalSize-3; i+=4) {
        denormalizedSignal[i] = ( (inSignal[i] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
        denormalizedSignal[i+1] = ( (inSignal[i+1] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
        denormalizedSignal[i+2] = ( (inSignal[i+2] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
        denormalizedSignal[i+3] = ( (inSignal[i+3] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
    }
    if (i == inSignalSize - 3) {
        denormalizedSignal[inSignalSize-3] = ( (inSignal[inSignalSize - 3] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
        denormalizedSignal[inSignalSize-2] = ( (inSignal[inSignalSize - 2] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
        denormalizedSignal[inSignalSize-1] = ( (inSignal[inSignalSize - 1] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
    } else if (i == inSignalSize - 2) {
        denormalizedSignal[inSignalSize-2] = ( (inSignal[inSignalSize - 2] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
        denormalizedSignal[inSignalSize-1] = ( (inSignal[inSignalSize - 1] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
    } else if (i == inSignalSize - 1) {
        denormalizedSignal[inSignalSize-1] = ( (inSignal[inSignalSize - 1] - NORM_ONE) * (NORM_DENOM / NORM_TWO_NEG) ) + SAMPLE_MIN_D;
    }
    return denormalizedSignal;
}

void FFTconvolve::FFT(double data[], int nn, int isign) {
    unsigned long n, mmax, m, j, istep, i;
    double wtemp, wr, wpr, wpi, wi, theta;
    double tempr, tempi;

    n = nn << 1;
    j = 1;

    for (i = 1; i < n; i += 2) {
        if (j > i) {
            SWAP(data[j], data[i]);
            SWAP(data[j+1], data[i+1]);
        }
        m = nn;
        while (m >= 2 && j > m) {
            j -= m;
            m >>= 1;
        }
        j += m;
    }

    mmax = 2;
    while (n > mmax) {
	istep = mmax << 1;
	theta = isign * (6.28318530717959 / mmax);
	wtemp = sin(0.5 * theta);
	wpr = -2.0 * wtemp * wtemp;
	wpi = sin(theta);
	wr = 1.0;
	wi = 0.0;
	for (m = 1; m < mmax; m += 2) {
	    for (i = m; i <= n; i += istep) {
		j = i + mmax;
		tempr = wr * data[j] - wi * data[j+1];
		tempi = wr * data[j+1] + wi * data[j];
		data[j] = data[i] - tempr;
		data[j+1] = data[i+1] - tempi;
		data[i] += tempr;
		data[i+1] += tempi;
	    }
	    wr = (wtemp = wr) * wpr - wi * wpi + wr;
	    wi = wi * wpr + wtemp * wpi + wi;
	}
	mmax = istep;
    }
}

void FFTconvolve::FFT_cached(double data[], int nn, int isign) {
    unsigned long n, mmax, m, j, istep, i;
    double wtemp, wr, wpr, wpi, wi, theta;
    double tempr, tempi;

    n = nn << 1;
    j = 1;

    for (i = 1; i < n; i += 2) {
        if (j > i) {
            SWAP(data[j], data[i]);
            SWAP(data[j+1], data[i+1]);
        }
        m = nn;
        while (m >= 2 && j > m) {
            j -= m;
            m >>= 1;
        }
        j += m;
    }

    mmax = 2;
    size_t cacheIndex = 0;
    while (n > mmax) {
        istep = mmax << 1;
        
        if (isign == 1) {
            wtemp = std::get<0>(cachedFFTSegmentCalculations[cacheIndex]);
            wpr = std::get<1>(cachedFFTSegmentCalculations[cacheIndex]);
            wpi = std::get<2>(cachedFFTSegmentCalculations[cacheIndex]);
        } else if (isign == -1) {
            wtemp = std::get<0>(cachedIFFTSegmentCalculations[cacheIndex]);
            wpr = std::get<1>(cachedIFFTSegmentCalculations[cacheIndex]);
            wpi = std::get<2>(cachedIFFTSegmentCalculations[cacheIndex]);
        }
        
        wr = 1.0;
        wi = 0.0;
        for (m = 1; m < mmax; m += 2) {
            for (i = m; i <= n; i += istep) {
                j = i + mmax;
                tempr = wr * data[j] - wi * data[j+1];
                tempi = wr * data[j+1] + wi * data[j];
                data[j] = data[i] - tempr;
                data[j+1] = data[i+1] - tempi;
                data[i] += tempr;
                data[i+1] += tempi;
            }
            wr = (wtemp = wr) * wpr - wi * wpi + wr;
            wi = wi * wpr + wtemp * wpi + wi;
        }
	    mmax = istep;
        cacheIndex += 1;
    }
}

void FFTconvolve::initialize_cachedFFTSegmentCalculations(size_t expectedConvolvedArraySegmentSize) {
    unsigned long n, mmax, istep;
    double theta, wtemp, wpr, wpi;

    n = (nextPowOfTwo(expectedConvolvedArraySegmentSize) << 1);
    mmax = 2;
    while (mmax < n) {
        istep = mmax << 1;

        theta = (1) * (6.28318530717959 / mmax);
        wtemp = sin(0.5 * theta);
        wpr = -2.0 * wtemp * wtemp;
        wpi = sin(theta);

        cachedFFTSegmentCalculations.push_back(
            std::tuple<double, double, double>(wtemp, wpr, wpi)
        );

        mmax = istep;
    }
}

void FFTconvolve::initialize_cachedIFFTSegmentCalculations(size_t expectedConvolvedArraySegmentSize) {
    unsigned long n, mmax, istep;
    double theta, wtemp, wpr, wpi;

    n = (nextPowOfTwo(expectedConvolvedArraySegmentSize) << 1);
    mmax = 2;
    while (mmax < n) {
        istep = mmax << 1;

        theta = (-1) * (6.28318530717959 / mmax);
        wtemp = sin(0.5 * theta);
        wpr = -2.0 * wtemp * wtemp;
        wpi = sin(theta);

        cachedIFFTSegmentCalculations.push_back(
            std::tuple<double, double, double>(wtemp, wpr, wpi)
        );

        mmax = istep;
    }
}


inline size_t FFTconvolve::nextPowOfTwo(size_t x) {
    return pow(2, ceil(log((x))/log(2)));
}