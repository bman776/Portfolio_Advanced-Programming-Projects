/*
CPSC501 FALL2023 Assignment 4 Submission 
Brett Gattinger 30009390
*/

#include <WaveWriter.hpp>

WaveWriter::WaveWriter() {
    //DEFAULT CONSTRUCTOR
}

void WaveWriter::write_wav(uint16_t audioFormat, uint16_t numOfChannels, uint32_t sampleRate, uint32_t byteRate, uint16_t blockAlign, uint16_t bitsPerSample,
    std::vector<BYTE> & additionalFormatChunkBytes, std::vector<int16_t> & data, std::vector<BYTE> & additionalDataChunkBytes, std::string outputFile) {
        

        outFile.exceptions(std::ofstream::badbit);
        outFile.exceptions(std::ifstream::failbit);
        try {
            //initialize file output stream (open new .wav file in binary write mode)
            outFile.open(outputFile, std::ios::out | std::ios::binary);

            //calculate subchunk sizes based on supplied data
            uint32_t subChunk1_size = MIN_FORMATCHUNK_SIZE + (sizeof(BYTE)*additionalFormatChunkBytes.size());
            uint32_t subChunk2_size = (sizeof(int16_t)*data.size());
            uint32_t chunkSize = ( DWORD + ( (2*DWORD) + subChunk1_size) + ( (2*DWORD) + subChunk2_size)) + (sizeof(BYTE)*additionalDataChunkBytes.size());

            //write RIFF header 
            Write_FourByteString(outFile, "RIFF");
            littleEndianWrite_uint32_t(outFile, chunkSize);
            Write_FourByteString(outFile, "WAVE");

            //write format subchunk
            Write_FourByteString(outFile, "fmt ");
            littleEndianWrite_uint32_t(outFile, subChunk1_size);
            littleEndianWrite_uint16_t(outFile, audioFormat);
            littleEndianWrite_uint16_t(outFile, numOfChannels);
            littleEndianWrite_uint32_t(outFile, sampleRate);
            littleEndianWrite_uint32_t(outFile, byteRate);
            littleEndianWrite_uint16_t(outFile, blockAlign);
            littleEndianWrite_uint16_t(outFile, bitsPerSample);
            outFile.write(reinterpret_cast<const char *>(
                &additionalFormatChunkBytes.front()), 
                (sizeof(int8_t)*additionalFormatChunkBytes.size())
            );

            //write data subchunk
            Write_FourByteString(outFile, "data");
            littleEndianWrite_uint32_t(outFile, subChunk2_size);
            size_t numOfSamples = (subChunk2_size / numOfChannels) / (bitsPerSample / BYTE_BIT_WIDTH);
            for (size_t i = 0; i < numOfSamples; i++) {
                littleEndianWrite_int16_t(outFile, data[i]);
            }
            outFile.write(reinterpret_cast<const char *>(
                &additionalDataChunkBytes.front()), 
                (sizeof(int8_t)*additionalDataChunkBytes.size())
            );

        } catch (const std::ofstream::failure& e) {
            outFile.close();
            std::cout << "Error writing OUtput file\n";
        }

        outFile.close();
}

void WaveWriter::Write_FourByteString(std::ofstream & outFile, std::string str) {
    outFile.write(reinterpret_cast<char *>(&str.front()), DWORD);
}

void WaveWriter::littleEndianWrite_uint32_t(std::ofstream & outFile, uint32_t value) {
    uint8_t buf[4];
    buf[3] = (uint8_t)(value >> 24) &0xFF;
    buf[2] = (uint8_t)(value >> 16) &0xFF;
    buf[1] = (uint8_t)(value >> 8) &0xFF;
    buf[0] = (uint8_t)(value) &0xFF;
    outFile.write(reinterpret_cast<const char *>(&buf[0]), sizeof(buf));
}

void WaveWriter::littleEndianWrite_uint16_t(std::ofstream & outFile, uint16_t value) {
    uint8_t buf[2];
    buf[1] = (value >> 8) &0xFF;
    buf[0] = (value) &0xFF;
    outFile.write(reinterpret_cast<const char *>(&buf[0]), sizeof(buf));
}

void WaveWriter::littleEndianWrite_int16_t(std::ofstream & outFile, int16_t value) {
    uint8_t buf[2];
    buf[1] = (value >> 8) &0xFF;
    buf[0] = (value) &0xFF;
    outFile.write(reinterpret_cast<const char *>(&buf[0]), sizeof(buf));
}

