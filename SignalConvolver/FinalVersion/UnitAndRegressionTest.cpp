/*
CPSC501 FALL2023 Assignment 4 Submission 
Brett Gattinger 30009390
*/

#define BOOST_TEST_MODULE My Test
#include <boost/test/included/unit_test.hpp>

#include <filesystem>
#include <windows.h>
#include <bit>

#include <random>
#include <math.h>
#include <cstdint>

#include <WaveReader.hpp>
#include <WaveWriter.hpp>
#include <BasicConvolve.hpp>
#include <FFTconvolve.hpp>

//To deactivate a test:
// BOOST_AUTO_TEST_CASE(sometest, *boost::unit_test::disabled())

template<typename InputIterator1, typename InputIterator2>
bool
range_equal(InputIterator1 first1, InputIterator1 last1, 
  InputIterator2 first2, InputIterator2 last2) {
    while(first1 != last1 && first2 != last2) {
      if(*first1 != *first2) {
        std::cout << "first1: " << *first1 << " | first2: " << *first2 << "\n\n";
        return false;
      } 
      ++first1;
      ++first2;
    }
    return (first1 == last1) && (first2 == last2);
}

bool compare_files(const std::string& filename1, const std::string& filename2) {
    std::ifstream file1(filename1);
    std::ifstream file2(filename2);

    std::istreambuf_iterator<char> begin1(file1);
    std::istreambuf_iterator<char> begin2(file2);

    std::istreambuf_iterator<char> end;

    return range_equal(begin1, end, begin2, end);
}

bool compareFiles(const std::string& p1, const std::string& p2) {
  std::ifstream f1(p1, std::ifstream::binary|std::ifstream::ate);
  std::ifstream f2(p2, std::ifstream::binary|std::ifstream::ate);

  if (f1.fail() || f2.fail()) {
    return false; //file problem
  }

  if (f1.tellg() != f2.tellg()) {
    return false; //size mismatch
  }

  //seek back to beginning and use std::equal to compare contents
  f1.seekg(0, std::ifstream::beg);
  f2.seekg(0, std::ifstream::beg);
  return std::equal(std::istreambuf_iterator<char>(f1.rdbuf()),
                    std::istreambuf_iterator<char>(),
                    std::istreambuf_iterator<char>(f2.rdbuf()));
}









BOOST_AUTO_TEST_CASE(first_test, *boost::unit_test::disabled()) {

  std::cout << "\n---RUNNING UNIT TEST 1: WAVE FILE READING/WRITING---\n";

  WaveReader wav_reader = WaveReader();
  WaveWriter wav_writer = WaveWriter();

  std::string currentWorkingDirectory = std::filesystem::current_path().generic_string();
  std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

  std::string inputSignalFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "DrySounds" + systemFileSeparator 
    + "DrumsDry.wav");
  std::string outputFile = std::string(
    currentWorkingDirectory 
    + systemFileSeparator 
    + "out.wav");

  wav_reader.read_wav(inputSignalFile);
  wav_reader.display_wav();

  wav_writer.write_wav(
    wav_reader.audioFormat, 
    wav_reader.numOfChannels,
    wav_reader.sampleRate,
    wav_reader.byteRate,
    wav_reader.blockAlign,
    wav_reader.bitsPerSample,
    wav_reader.additionalFormatChunkBytes,
    wav_reader.data,
    wav_reader.additionalDataChunkBytes,
    outputFile
  );

  wav_reader.read_wav(outputFile);
  wav_reader.display_wav();
  
  bool filesEqual (compare_files(inputSignalFile, outputFile) && compareFiles(inputSignalFile, outputFile));

  BOOST_TEST(filesEqual);
}



BOOST_AUTO_TEST_CASE(second_test, *boost::unit_test::disabled()) {

  std::cout << "\n---RUNNING UNIT TEST 2: AUDIO DATA NORMALIZATION---\n";

  WaveReader wav_reader = WaveReader();
  BasicConvolve basic_convolver = BasicConvolve();

  std::string currentWorkingDirectory = std::filesystem::current_path().generic_string();
  std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

  std::string inputSignalFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "DrySounds" + systemFileSeparator 
    + "DrumsDry.wav");
  std::string outputFile = std::string(
    currentWorkingDirectory 
    + systemFileSeparator 
    + "out.wav");
  
  wav_reader.read_wav(inputSignalFile);
  wav_reader.display_wav();
  std::vector<int16_t> inputSignal = wav_reader.get_audioData();

  std::vector<double> normalizedInputSignal = basic_convolver.normalizeSignal(inputSignal);

  boolean absValGreaterThanOne_detected = false;
  for(int i = 0; i < normalizedInputSignal.size(); i++) {
    if (fabs(normalizedInputSignal[i]) > 1) {
      absValGreaterThanOne_detected = true;
      break;
    }
  }

  BOOST_TEST(!absValGreaterThanOne_detected);
}



BOOST_AUTO_TEST_CASE(third_test, *boost::unit_test::disabled()) {

  std::cout << "\n---RUNNING UNIT TEST 3: BASIC CONVOLUTION---\n";

  WaveReader wav_reader = WaveReader();
  WaveWriter wav_writer = WaveWriter();
  BasicConvolve basic_convolver = BasicConvolve();

  std::string currentWorkingDirectory = std::filesystem::current_path().generic_string();
  std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

  std::string inputSignalFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "DrySounds" + systemFileSeparator 
    + "DrumsDry.wav");
  std::string impulseResponseFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "ImpulseResponses" + systemFileSeparator
    + "Mono" + systemFileSeparator 
    + "BIG HALL E001 M2S.wav");
  std::string outputFile = std::string(
    currentWorkingDirectory 
    + systemFileSeparator 
    + "out.wav");

  wav_reader.read_wav(inputSignalFile);
  wav_reader.display_wav();
  std::vector<int16_t> inputSignal = wav_reader.get_audioData();

  wav_reader.read_wav(impulseResponseFile);
  wav_reader.display_wav();
  std::vector<int16_t> impulseResponse = wav_reader.get_audioData();
  
  std::vector<int16_t> convolvedSignal = basic_convolver.convoleSignals(inputSignal, impulseResponse);  

  std::vector<BYTE> additionalFormatBytes;
  std::vector<BYTE> additionalBytes;
  wav_writer.write_wav(int16_t(1), int16_t(1), uint32_t(44100), uint32_t(88200), uint16_t(2), uint16_t(16), 
    additionalFormatBytes, convolvedSignal, additionalBytes, outputFile);
}



BOOST_AUTO_TEST_CASE(fourth_test, *boost::unit_test::disabled()) {
  std::cout << "\n---RUNNING UNIT TEST 4: FFT CONVOLUTION---\n";

  WaveReader wav_reader = WaveReader();
  WaveWriter wav_writer = WaveWriter();
  FFTconvolve fft_convolver = FFTconvolve();

  std::string currentWorkingDirectory = std::filesystem::current_path().generic_string();
  std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

  std::string inputSignalFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "DrySounds" + systemFileSeparator 
    + "DrumsDry.wav");
  std::string impulseResponseFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "ImpulseResponses" + systemFileSeparator
    + "Mono" + systemFileSeparator 
    + "BIG HALL E001 M2S.wav");
  std::string outputFile = std::string(
    currentWorkingDirectory 
    + systemFileSeparator 
    + "out.wav");

  wav_reader.read_wav(inputSignalFile);
  wav_reader.display_wav();
  std::vector<int16_t> inputSignal = wav_reader.get_audioData();

  wav_reader.read_wav(impulseResponseFile);
  wav_reader.display_wav();
  std::vector<int16_t> impulseResponse = wav_reader.get_audioData();
  
  std::vector<int16_t> convolvedSignal = fft_convolver.convolveSignals(inputSignal, impulseResponse);

  std::vector<BYTE> additionalFormatBytes;
  std::vector<BYTE> additionalBytes;
  wav_writer.write_wav(int16_t(1), int16_t(1), uint32_t(44100), uint32_t(88200), uint16_t(2), uint16_t(16), 
    additionalFormatBytes, convolvedSignal, additionalBytes, outputFile);
}



BOOST_AUTO_TEST_CASE(fifth_test, *boost::unit_test::disabled()) {

  std::cout << "\n---RUNNING REGRESSION TEST FOR OPTIMIZATION 1: FFT CALCULATION CACHING OPTIMIZATION---\n";

  FFTconvolve fft_convolver = FFTconvolve();

  //generate 2 copies of a random array of doubles
  double lower_bound = 0;
  double upper_bound = 10000;
  std::uniform_real_distribution<double> unif(lower_bound,upper_bound);
  std::default_random_engine re;
  size_t randomSignalSize = pow(2, 10);
  double *randomSignal1 = new double[randomSignalSize];
  double *randomSignal2 = new double[randomSignalSize];
  double *randomSignal3 = new double[randomSignalSize];
  double *randomSignal4 = new double[randomSignalSize];
  for (int i = 0; i < randomSignalSize; i++) {
    randomSignal1[i] = unif(re);
    randomSignal2[i] = randomSignal1[i];
    randomSignal3[i] = randomSignal1[i];
    randomSignal4[i] = randomSignal1[i];
  }

  fft_convolver.FFT(randomSignal1-1, (randomSignalSize/2), 1);
  fft_convolver.initialize_cachedFFTSegmentCalculations((randomSignalSize/2));
  fft_convolver.FFT_cached(randomSignal2-1, (randomSignalSize/2), 1);
  bool signalFFTTransformsEqual = true;
  for (int i = 0; i < randomSignalSize; i++) {
    if (randomSignal1[i] != randomSignal2[i]) {
      std::cout << "\nsignal 1: " << randomSignal1[i] << " | signal 2: " << randomSignal2[i] << "\n";
      signalFFTTransformsEqual = false;
      break;
    }
  }

  fft_convolver.FFT(randomSignal3-1, (randomSignalSize/2), -1);
  fft_convolver.initialize_cachedIFFTSegmentCalculations((randomSignalSize/2));
  fft_convolver.FFT_cached(randomSignal4-1, (randomSignalSize/2), -1);
  bool signalIFFTTransformsEqual = true;
  for (int i = 0; i < randomSignalSize; i++) {
    if (randomSignal3[i] != randomSignal4[i]) {
      std::cout << "\nsignal 3: " << randomSignal1[i] << " | signal 4: " << randomSignal2[i] << "\n";
      signalIFFTTransformsEqual = false;
      break;
    }
  }

  bool result = signalFFTTransformsEqual && signalIFFTTransformsEqual;

  BOOST_TEST(result);
}



BOOST_AUTO_TEST_CASE(sixth_test, *boost::unit_test::disabled()) {

  std::cout << "\n---RUNNING REGRESSION TEST FOR OPTIMIZATION 2: FREQUENCY RESPONSE ARRAY INITIALIZATION LOOP JAMMING---\n";

  size_t subArraySize = pow(2, 19);
  size_t arraySize = pow(2, 20);

  double lower_bound = 0;
  double upper_bound = 10000;
  std::uniform_real_distribution<double> unif(lower_bound,upper_bound);
  std::default_random_engine re;
  double * randomSignal1 = new double[subArraySize];
  for (int i = 0; i < subArraySize; i++) {
    randomSignal1[i] = unif(re);
  }

  
  double *outArray1 = new double[arraySize]();
  for (size_t i = 0; i < arraySize; i++) {
      outArray1[i] = 0;
  }
  for (size_t i = 0, j = 0; j < subArraySize; i+=2, j++) {
      outArray1[i] = randomSignal1[j];
  }

  double *outArray2 = new double[arraySize]();
  size_t last_index;
  for (size_t i = 0, j = 0; j < subArraySize; i+=2, j++) {
    outArray2[i] = randomSignal1[j];
    outArray2[i+1] = 0;
    last_index = i+1;
  }
  for (size_t i = last_index; i < arraySize; i++) {
    outArray2[i] = 0;
  }

  bool arraysEqual = true;
  for (int i = 0; i < arraySize; i++) {
    if (outArray1[i] != outArray2[i]) {
      std::cout << outArray1[i] << " " << outArray2[i];
      arraysEqual = false;
      break;
    }
  }

  BOOST_TEST(arraysEqual);
}



BOOST_AUTO_TEST_CASE(seventh_test, *boost::unit_test::disabled()) {
  std::cout << "\n---RUNNING REGRESSION TEST FOR OPTIMIZATION 3: DATA NORMALIZATION LOOP UNROLLING---\n";

  WaveReader wav_reader = WaveReader();
  FFTconvolve fft_convolver = FFTconvolve();

  std::string currentWorkingDirectory = std::filesystem::current_path().generic_string();
  std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

  std::string inputSignalFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "DrySounds" + systemFileSeparator 
    + "DrumsDry.wav");
  
  wav_reader.read_wav(inputSignalFile);
  wav_reader.display_wav();
  std::vector<int16_t> inputSignal = wav_reader.get_audioData();

  std::vector<double> normalizedInputSignal1 = fft_convolver.normalizeSignal(inputSignal);
  std::vector<double> normalizedInputSignal2 = fft_convolver.normalizeSignal_unrolled(inputSignal);

  bool normalizedSignalsEqual = true;
  for (int i = 0; i < inputSignal.size(); i++) {
    if (normalizedInputSignal1[i] != normalizedInputSignal2[i]) {
      std::cout << "\nsignal1: " << normalizedInputSignal1[i] << " | signal 2: " << normalizedInputSignal2[i] << "\n";
      normalizedSignalsEqual = false;
      break;
    }
  }

  std::vector<int16_t> denormalizedInputSignal1 = fft_convolver.denormalizeSignal(normalizedInputSignal1);
  std::vector<int16_t> denormalizedInputSignal2 = fft_convolver.denormalizeSignal_unrolled(normalizedInputSignal1);

  bool denormalizedSignalsEqual = true;
  for (int i = 0; i < inputSignal.size(); i++) {
    if (denormalizedInputSignal1[i] != denormalizedInputSignal2[i]) {
      std::cout << "\nsignal1: " << denormalizedInputSignal1[i] << " | signal 2: " << denormalizedInputSignal2[i] << "\n";
      denormalizedSignalsEqual = false;
      break;
    }
  }

  bool result = normalizedSignalsEqual && denormalizedSignalsEqual;

  BOOST_TEST(result);
}

BOOST_AUTO_TEST_CASE(eighth_test, *boost::unit_test::disabled()) {
  std::cout << "\n---RUNNING REGRESSION TEST FOR OPTIMIZATION 4: DATA NORMALIZATION FURTHER OPTIMIZATION---\n";

  WaveReader wav_reader = WaveReader();
  FFTconvolve fft_convolver = FFTconvolve();

  std::string currentWorkingDirectory = std::filesystem::current_path().generic_string();
  std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

  std::string inputSignalFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "DrySounds" + systemFileSeparator 
    + "DrumsDry.wav");
  
  wav_reader.read_wav(inputSignalFile);
  wav_reader.display_wav();
  std::vector<int16_t> inputSignal = wav_reader.get_audioData();

  std::vector<double> normalizedInputSignal1 = fft_convolver.normalizeSignal_unrolled(inputSignal);
  std::vector<double> normalizedInputSignal2 = fft_convolver.normalizeSignal_unrolled_optimized(inputSignal);

  bool normalizedSignalsEqual = true;
  for (int i = 0; i < inputSignal.size(); i++) {
    if (normalizedInputSignal1[i] != normalizedInputSignal2[i]) {
      std::cout << "\nNormalization:\n";
      std::cout << "signal1: " << normalizedInputSignal1[i] << " | signal 2: " << normalizedInputSignal2[i] << "\n";
      normalizedSignalsEqual = false;
      break;
    }
  }

  std::vector<int16_t> denormalizedInputSignal1 = fft_convolver.denormalizeSignal_unrolled(normalizedInputSignal1);
  std::vector<int16_t> denormalizedInputSignal2 = fft_convolver.denormalizeSignal_unrolled_optimized(normalizedInputSignal1);

  //NOTE: because of double to int16_t conversion loss of data, values returned by 
  //denormalizeSignal_unrolled_optimized will be off by 1
  bool denormalizedSignalsEqual = true;
  for (int i = 0; i < inputSignal.size(); i++) {
    if (abs(denormalizedInputSignal1[i] - denormalizedInputSignal2[i]) > 1) {
      std::cout << "\nDenormalization:\n";
      std::cout << "signal1: " << denormalizedInputSignal1[i] << " | signal 2: " << denormalizedInputSignal2[i] << "\n";
      denormalizedSignalsEqual = false;
      break;
    }
  }

  bool result = normalizedSignalsEqual && denormalizedSignalsEqual;

  BOOST_TEST(result);
}

BOOST_AUTO_TEST_CASE(fileComparisonTest) {
  std::cout << "\n---RUNNING GENERAL REGRESSION TEST---\n";

  WaveReader wav_reader = WaveReader();
  WaveWriter wav_writer = WaveWriter();
  FFTconvolve fft_convolver = FFTconvolve();

  std::string currentWorkingDirectory = std::filesystem::current_path().generic_string();
  std::string systemFileSeparator = std::string(1, std::filesystem::path::preferred_separator);

  std::string inputSignalFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "DrySounds" + systemFileSeparator 
    + "GuitarDry.wav");
  std::string impulseResponseFile = std::string(
    currentWorkingDirectory + systemFileSeparator 
    + "Resources" + systemFileSeparator 
    + "ImpulseResponses" + systemFileSeparator
    + "Mono" + systemFileSeparator 
    + "BIG HALL E001 M2S.wav");
  std::string outputFile = std::string(
    currentWorkingDirectory 
    + systemFileSeparator 
    + "out.wav");

  std::string compFile = std::string(
    currentWorkingDirectory 
    + systemFileSeparator 
    + "comp.wav");

  wav_reader.read_wav(inputSignalFile);
  wav_reader.display_wav();
  std::vector<int16_t> inputSignal = wav_reader.get_audioData();

  wav_reader.read_wav(impulseResponseFile);
  wav_reader.display_wav();
  std::vector<int16_t> impulseResponse = wav_reader.get_audioData();
  
  std::vector<int16_t> convolvedSignal = fft_convolver.convolveSignals(inputSignal, impulseResponse);

  std::vector<BYTE> additionalFormatBytes;
  std::vector<BYTE> additionalBytes;
  wav_writer.write_wav(int16_t(1), int16_t(1), uint32_t(44100), uint32_t(88200), uint16_t(2), uint16_t(16), 
    additionalFormatBytes, convolvedSignal, additionalBytes, outputFile);
  
  bool filesEqual (compare_files(outputFile, compFile) && compareFiles(outputFile, compFile));

  BOOST_TEST(filesEqual);
}