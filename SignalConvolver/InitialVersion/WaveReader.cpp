#include <WaveReader.hpp>

//Constructor
WaveReader::WaveReader() {
    //DEFAULT CONSTRUCTOR
}

void WaveReader::read_wav(std::string inputFile) {
    //clear previous wave file read in
    clear_wav();

    inFile.exceptions(std::ifstream::badbit);
    inFile.exceptions(std::ifstream::failbit);
    try {
        //initialize file input stream (open .wav file in binary read mode)
        inFile.open(inputFile, std::ios::in | std::ios::binary);

        //read RIFF header
        Read_FourByteString(inFile, chunkID);
        chunkSize = littleEndianRead_uint32_t(inFile);
        Read_FourByteString(inFile, format);

        //read format subchunk
        Read_FourByteString(inFile, subChunk1_ID);
        subChunk1_size = littleEndianRead_uint32_t(inFile);
        audioFormat = littleEndianRead_uin16_t(inFile);
        numOfChannels = littleEndianRead_uin16_t(inFile);
        sampleRate = littleEndianRead_uint32_t(inFile);
        byteRate = littleEndianRead_uint32_t(inFile);
        blockAlign = littleEndianRead_uin16_t(inFile);
        bitsPerSample = littleEndianRead_uin16_t(inFile);
        if (subChunk1_size > MIN_FORMATCHUNK_SIZE) {
            size_t numOf_additionalFmtBytes = subChunk1_size - MIN_FORMATCHUNK_SIZE;
            additionalFormatChunkBytes.resize(numOf_additionalFmtBytes);
            inFile.read(reinterpret_cast<char*>(&additionalFormatChunkBytes.front()), numOf_additionalFmtBytes);
        }

        //read data subchunk
        Read_FourByteString(inFile, subChunk2_ID);
        subChunk2_size = littleEndianRead_uint32_t(inFile);
        size_t numOfSamples = (subChunk2_size / numOfChannels) / (bitsPerSample / BYTE_BIT_WIDTH);
        data.reserve(numOfSamples);
        for (size_t i = 0; i < numOfSamples; i++) {
            data.push_back(littleEndianRead_in16_t(inFile));
        }
        size_t actualWaveFileSize = ( DWORD + ( (2*DWORD) + subChunk1_size) + ( (2*DWORD) + subChunk2_size));
        if (chunkSize > actualWaveFileSize) {
            size_t numOf_additionalDataBytes = chunkSize - actualWaveFileSize;
            additionalDataChunkBytes.resize(numOf_additionalDataBytes);
            inFile.read(reinterpret_cast<char*>(&additionalDataChunkBytes.front()), numOf_additionalDataBytes);
        }



    } catch (const std::ifstream::failure& e) {
        std::cout << "\nError reading Input file: " << e.what() << "\n";
        inFile.close();
        exit(0);
    }

    inFile.close();
}

std::vector<int16_t> & WaveReader::get_audioData() {
    return this->data;
} 

void WaveReader::display_wav() {
    //generate output string
    std::string output = "";
    output.append("\nWAVE FILE READ:\n");
    //---
    output.append("chunk ID: " + chunkID + "\n");
    output.append("chunk size: " + std::to_string(chunkSize) + "\n");
    output.append("format: " + format + "\n");
    //---
    output.append("format chunk ID: " + subChunk1_ID + "\n");
    output.append("format chunk size:  " + std::to_string(subChunk1_size) + "\n");
    output.append("audio format: " + std::to_string(audioFormat) + "\n");
    output.append("number of channels: " + std::to_string(numOfChannels) + "\n");
    output.append("sample rate: " + std::to_string(sampleRate) + "\n");
    output.append("byte rate: " + std::to_string(byteRate) + "\n");
    output.append("block align: " + std::to_string(blockAlign) + "\n");
    output.append("bits per sample: " + std::to_string(bitsPerSample) + "\n");
    //---
    output.append("data chunk ID: " + subChunk2_ID + "\n");
    output.append("data chunk size: " + std::to_string(subChunk2_size) + "\n");
    std::cout << output;
}

void WaveReader::clear_wav() {
    //clear data of wave file previously read in
    this->chunkID.clear();
    this->chunkSize = 0;
    this->format.clear();

    this->subChunk1_ID.clear();
    this->subChunk1_size = 0;
    this->audioFormat = 0;
    this->numOfChannels = 0;
    this->sampleRate = 0;
    this->byteRate = 0;
    this->blockAlign = 0;
    this->bitsPerSample = 0;

    this->subChunk2_ID.clear();
    this->subChunk2_size = 0;

    this->bytesRead = 0;

    additionalFormatChunkBytes.clear();
    data.clear();
    additionalDataChunkBytes.clear();
}

void WaveReader::Read_FourByteString(std::ifstream & inFile, std::string & str) {
    str.resize(4);
    inFile.read(reinterpret_cast<char *>(&str.front()), DWORD);
}

uint32_t WaveReader::littleEndianRead_uint32_t(std::ifstream & inFile) {
    uint32_t value;
    uint8_t buf[4];
    inFile.read(reinterpret_cast<char *>(&buf[0]), sizeof(buf));
    value = (uint32_t)(buf[3] << 24);
    value |= (uint32_t)(buf[2] << 16);
    value |= (uint32_t)(buf[1] << 8);
    value |= (uint32_t)(buf[0]);
    return value;
}

uint16_t WaveReader::littleEndianRead_uin16_t(std::ifstream & inFile) {
    uint16_t value;
    uint8_t buf[2];
    inFile.read(reinterpret_cast<char *>(&buf[0]), sizeof(buf));
    value = (uint32_t)(buf[1] << 8);
    value |= (uint32_t)(buf[0]);
    return value;
}


int16_t WaveReader::littleEndianRead_in16_t(std::ifstream & inFile) {
    int16_t value;
    uint8_t buf[2];
    inFile.read(reinterpret_cast<char *>(&buf[0]), sizeof(buf));
    value = (uint32_t)(buf[1] << 8);
    value |= (uint32_t)(buf[0]);
    return value;
}