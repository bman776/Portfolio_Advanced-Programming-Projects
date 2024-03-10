#include <FFTconvolve.hpp>

FFTconvolve::FFTconvolve() {
    //DEFAULT CONSTRUCTOR
    //currently nothing to initialize
}

std::vector<int16_t> FFTconvolve::convolveSignals(std::vector<int16_t> & inputSignal, std::vector<int16_t> & impulseResponse) {

    //The result to be returned
    std::vector<int16_t> convolvedSignal;
    std::vector<double> convolvedSignal_normalized;

    //normalize input and impluse response signals
    std::vector<double> normalizedInputSignal = normalizeSignal(inputSignal);
    std::vector<double> normalizedImpulseResponse = normalizeSignal(impulseResponse);

    //calcualte size of arrays used in FFT and IFFT operations
    size_t expectedConvolvedArraySegmentSize = SEGMENT_SIZE + normalizedImpulseResponse.size()-1;

    //apply FFT to impulse response to get frequency response
    size_t FR_FFT_arrayOpSize = 2 * nextPowOfTwo(expectedConvolvedArraySegmentSize);

    //DEBGUGGING: getting seg fault here, maybe running out memory on the stack
    //  SOLVED ~ yes was running out of memory on the stack so had to dynamically declare on heap
    double *frequencyResponse = new double[FR_FFT_arrayOpSize]();
    for (size_t i = 0; i < FR_FFT_arrayOpSize; i++) {
        frequencyResponse[i] = 0;
    }

    for (size_t i = 0, j = 0; j < normalizedImpulseResponse.size(); i+=2, j++) {
        frequencyResponse[i] = normalizedImpulseResponse[j];
    }

    FFT(frequencyResponse-1, nextPowOfTwo(expectedConvolvedArraySegmentSize), 1);

    //pad input signals with 0's so that it matches next greatest multiple of segment size (1024 samples per segement)
    for (int i = 0; i < int(normalizedInputSignal.size() % SEGMENT_SIZE); i++) {
        normalizedInputSignal.push_back(0);
    }
    int numberOfSegments = int(normalizedInputSignal.size()/SEGMENT_SIZE);

    //apply FFT to input signal in segment sizes of 1024 samples complex-multiply with the frequency response and overlap add the result into convolved signal array
    std::vector<double>::iterator segmentCursor = normalizedInputSignal.begin();
    for (int i = 0; i < numberOfSegments; i++) {

        //load in next segment of input signal
        size_t ISS_FFT_arrayOpSize = 2 * nextPowOfTwo(expectedConvolvedArraySegmentSize);
        //DEBGUGGING: getting seg fault here, maybe running out memory on the stack
        //  SOLVED ~ yes was running out of memory on the stack so had to dynamically declare on heap
        double *frequencySpectrumSegment = new double[ISS_FFT_arrayOpSize]();
        for (size_t j = 0; j < ISS_FFT_arrayOpSize; j++) {
            frequencySpectrumSegment[j] = 0;
        }

        for (size_t j = 0, k = 0; k < SEGMENT_SIZE; j+=2, k++) {
            frequencySpectrumSegment[j] = *segmentCursor;
            std::advance(segmentCursor, 1);
        }

        FFT(frequencySpectrumSegment-1, nextPowOfTwo(expectedConvolvedArraySegmentSize), 1);

        //multiply in the frequency domain to convolve
        double *convolvedSpectrumSegment = new double[ISS_FFT_arrayOpSize]();
        for (size_t j = 0; j < ISS_FFT_arrayOpSize; j+=2) {
            convolvedSpectrumSegment[j] = (frequencySpectrumSegment[j] * frequencyResponse[j]) - (frequencySpectrumSegment[j+1] * frequencyResponse[j+1]);
            convolvedSpectrumSegment[j+1] = (frequencySpectrumSegment[j] * frequencyResponse[j+1]) + (frequencySpectrumSegment[j+1] * frequencyResponse[j]);
        }

        //get back the convolved segment of the input signal
        FFT(convolvedSpectrumSegment-1, nextPowOfTwo(expectedConvolvedArraySegmentSize), -1);

        //extract and scale real part 
        double convolvedSpectrumSegment_real[expectedConvolvedArraySegmentSize];
        for (int j = 0, k = 0; k < expectedConvolvedArraySegmentSize; j+=2, k++) {
            convolvedSpectrumSegment_real[k] = convolvedSpectrumSegment[j] / double(normalizedInputSignal.size());
        }

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

    convolvedSignal = denormalizeSignal(convolvedSignal_normalized);
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

std::vector<int16_t> FFTconvolve::denormalizeSignal(std::vector<double> & inSignal) {
    std::vector<int16_t> denormalizedSignal(inSignal.size());
    denormalizedSignal.reserve(inSignal.size());
    for (int i = 0; i < inSignal.size(); i++) {
        double normalizedSample = inSignal[i];
        denormalizedSignal[i] = int16_t( (normalizedSample - double(1)) * (((double)SAMPLE_MAX - (double)SAMPLE_MIN) / double(-1-1)) ) + (double)SAMPLE_MIN;
    }
    return denormalizedSignal;
}

size_t FFTconvolve::nextPowOfTwo(size_t x) {
    return pow(2, ceil(log((x))/log(2)));
}

void FFTconvolve::FFT(double data[], int nn, int isign)
{
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