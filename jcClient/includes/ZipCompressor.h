#ifndef CLIENT_ZIPCOMPRESSOR_H
#define CLIENT_ZIPCOMPRESSOR_H

#include <string>
#include <iostream>
#include <fstream>
#include <filesystem>
#include <zipper/zipper.h>
#include <zipper/unzipper.h>
#include "TimeCS.h"
#include "Converter.h"
#include <sstream>
#include <windows.h>


class ZipCompressor {
private:
    static void zipItems(const std::string &zipFilename, const std::vector<std::filesystem::path> &inputPaths);
    static std::vector<uint8_t> zipItemInMemory(const std::filesystem::path &inputPath);
public:
    static std::vector<uint8_t> createZipInMemory(const std::vector<std::filesystem::path>& ,const std::vector<BYTE>& ,const std::string& extraFileName);
    static std::string compressPaths(const std::vector<std::string> &paths);
    static std::vector<uint8_t> compressPathInMemory(const std::string &path);
    static void decompressFromMemory(const std::vector<uint8_t>& memoryData, const std::string& destinationPath);
};


#endif
