#ifndef CLIENT_ZIPCOMPRESSOR_H
#define CLIENT_ZIPCOMPRESSOR_H

#include <string>
#include <iostream>
#include <fstream>
#include <filesystem>
#include <zipper/zipper.h>
#include "TimeCS.h"
#include "Converter.h"
#include <sstream>


class ZipCompressor {
private:
    static void zipItem(const std::string &zipFilename, const std::filesystem::path &inputPath);
    static std::vector<uint8_t> zipItemInMemory(const std::filesystem::path &inputPath);
public:
    static std::string compressPath(const std::string &path); // could be folder or file
    static std::vector<uint8_t> compressPathInMemory(const std::string &path);
};


#endif
