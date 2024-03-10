#ifndef WAVEREADER_H
#define WAVEREADER_H

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

class WaveReader {
    public:
        static const size_t MIN_FORMATCHUNK_SIZE = 16;
        static const size_t BYTE_BIT_WIDTH = 8;
        static const size_t DWORD = 4;
        static const size_t WORD = 2;

        std::string chunkID;
        uint32_t chunkSize;
        std::string format;

        std::string subChunk1_ID;
        uint32_t subChunk1_size;
        uint16_t audioFormat;
        uint16_t numOfChannels;
        uint32_t sampleRate;
        uint32_t byteRate;
        uint16_t blockAlign;
        uint16_t bitsPerSample;
        std::vector<BYTE> additionalFormatChunkBytes;

        std::string subChunk2_ID;
        uint32_t subChunk2_size;
        std::vector<int16_t> data;
        std::vector<BYTE> additionalDataChunkBytes;

        std::ifstream inFile;
        size_t bytesRead;

        WaveReader();
        void read_wav(std::string inputFile);
        std::vector<int16_t>& get_audioData();
        void display_wav();
        void clear_wav();
        void Read_FourByteString(std::ifstream & inFile, std::string & str);
        uint32_t littleEndianRead_uint32_t(std::ifstream & inFile);
        uint16_t littleEndianRead_uin16_t(std::ifstream & inFile);
        int16_t littleEndianRead_in16_t(std::ifstream & inFile);
};
#endif
