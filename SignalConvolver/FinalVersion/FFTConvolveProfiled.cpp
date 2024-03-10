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
  WaveReader wav_reader = WaveReader();
  WaveWriter wav_writer = WaveWriter();
  FFTconvolve fft_convolver = FFTconvolve();

  std::string currentWorkingDirectory = std::filesystem::current_path().generic_string();
  std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

  std::string inputSignalFile = std::string(
        currentWorkingDirectory + systemFileSeparator 
        + "Resources" + systemFileSeparator 
        + "DrySounds" + systemFileSeparator 
        + "GuitarDry.wav"
    );
    std::string impulseResponseFile = std::string(
        currentWorkingDirectory + systemFileSeparator 
        + "Resources" + systemFileSeparator 
        + "ImpulseResponses" + systemFileSeparator
        + "Mono" + systemFileSeparator 
        + "BIG HALL E001 M2S.wav"
    );
    std::string outputFile = std::string(
        currentWorkingDirectory 
        + systemFileSeparator 
        + "out.wav"
    );

  wav_reader.read_wav(inputSignalFile);
  std::vector<int16_t> inputSignal = wav_reader.get_audioData();

  wav_reader.read_wav(impulseResponseFile);
  std::vector<int16_t> impulseResponse = wav_reader.get_audioData();

  std::vector<int16_t> convolvedSignal = fft_convolver.convolveSignals(inputSignal, impulseResponse);

  std::vector<BYTE> additionalFormatBytes;
  std::vector<BYTE> additionalBytes;
  wav_writer.write_wav(int16_t(1), int16_t(1), uint32_t(44100), uint32_t(88200), uint16_t(2), uint16_t(16), 
      additionalFormatBytes, convolvedSignal, additionalBytes, outputFile);
}