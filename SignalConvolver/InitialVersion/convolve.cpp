/*
CPSC501 FALL2023 Assignment 4 Submission 
Brett Gattinger 30009390
*/

#include <filesystem>

#include <WaveReader.hpp>
#include <WaveWriter.hpp>
#include <BasicConvolve.hpp>
#include <FFTconvolve.hpp>

int main (int argc, char *argv[]) {
    /*Check if required number command line arguments have been supplied*/
    if (argc == 4) {
        fprintf(stdout, "Beginning Convolution Program...\n");

        /*get system file separator and current working directory*/
        std::string workingDirectory_path = std::filesystem::current_path().generic_string();
        std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

        /*process command line arguments*/
        std::string inputSignalFileName = std::string(argv[1]);
        std::string impulseResponseFileName = std::string(argv[2]);
        std::string outputSignalFileName = std::string(argv[3]);
        std::string inputSignalFilePath = std::string(workingDirectory_path + systemFileSeparator + inputSignalFileName);
        std::string impulseResponseFilePath = std::string(workingDirectory_path + systemFileSeparator + impulseResponseFileName);
        std::string outputSignalFilePath = std::string(workingDirectory_path + systemFileSeparator + outputSignalFileName);

        /*initialize program objects*/
        WaveReader wav_reader = WaveReader();
        WaveWriter wav_writer = WaveWriter();
        FFTconvolve fft_convolver = FFTconvolve();

        /*read in wave files and extract input signal and impulse response*/
        wav_reader.read_wav(inputSignalFilePath);
        std::vector<int16_t> inputSignal = wav_reader.get_audioData();
        wav_reader.read_wav(impulseResponseFilePath);
        std::vector<int16_t> impulseResponse = wav_reader.get_audioData();

        /*convolve the input signal and impulse response using FFT convolution*/
        std::vector<int16_t> convolvedSignal = fft_convolver.convolveSignals(inputSignal, impulseResponse);
        
        /*write the convolved out to an output wave file*/
        std::vector<BYTE> additionalFormatBytes;
        std::vector<BYTE> additionalBytes;
        wav_writer.write_wav(int16_t(1), int16_t(1), uint32_t(44100), uint32_t(88200), uint16_t(2), uint16_t(16), 
            additionalFormatBytes, convolvedSignal, additionalBytes, outputSignalFilePath);

        fprintf(stdout, "Signal Convolution Completed!\n");

    } else {
        /*user did not supply correct number of command line arguments*/
        fprintf(stderr, "Usage : %s InputFileName IRfilename OutputFilename\n", argv[0]);
    }
}
