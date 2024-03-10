#ifndef WAVEWRITER_H
#define WAVEWRITER_H

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
#include <string.h>

typedef unsigned char BYTE;

class WaveWriter {
    public:
        static const size_t MIN_FORMATCHUNK_SIZE = 16;
        static const size_t BYTE_BIT_WIDTH = 8;
        static const size_t DWORD = 4;
        static const size_t WORD = 2;

        std::ofstream outFile;

        WaveWriter();
        void write_wav(uint16_t audioFormat, uint16_t numOfChannels, uint32_t sampleRate, uint32_t byteRate, uint16_t blockAlign, uint16_t bitsPerSample,
            std::vector<BYTE> & additionalFormatChunkBytes, std::vector<int16_t> & data, std::vector<BYTE> & additionalDataChunkBytes, std::string outputFile);
        void Write_FourByteString(std::ofstream & outFile, std::string str);
        void littleEndianWrite_uint32_t(std::ofstream & outFile, uint32_t value);
        void littleEndianWrite_uint16_t(std::ofstream & outFile, uint16_t value);
        void littleEndianWrite_int16_t(std::ofstream & outFile, int16_t value);
        
};
#endif