#include "ZipCompressor.h"

void ZipCompressor::decompressFromMemory(const std::vector<uint8_t>& memoryData, const std::string& destinationPath) {
    std::istringstream memoryStream(std::string(memoryData.begin(), memoryData.end()));
    zipper::Unzipper unzipper(memoryStream);

    unzipper.extract(destinationPath);
    unzipper.close();
}

void ZipCompressor::zipItems(const std::string &zipFilename, const std::vector<std::filesystem::path> &inputPaths) {
    zipper::Zipper zipper(zipFilename);

    for (const auto &inputPath : inputPaths) {
        if (std::filesystem::is_directory(inputPath)) {
            std::string rootFolderName = inputPath.filename().string();
            for (const auto &entry : std::filesystem::recursive_directory_iterator(inputPath)) {
                if (std::filesystem::is_regular_file(entry.path())) {
                    std::ifstream inputStream(entry.path(), std::ios::binary);
                    std::string relativePath = std::filesystem::relative(entry.path(), inputPath).string();
                    std::string nameInZip = rootFolderName;
                    nameInZip += "/";
                    nameInZip += relativePath;

                    if (inputStream.is_open()) {
                        zipper.add(inputStream, nameInZip);
                    }
                }
            }
        } else if (std::filesystem::is_regular_file(inputPath)) {
            std::ifstream inputStream(inputPath, std::ios::binary);
            std::string nameInZip = inputPath.filename().string();

            if (inputStream.is_open()) {
                zipper.add(inputStream, nameInZip);
            }
        }
    }

    zipper.close();
}

std::string ZipCompressor::compressPaths(const std::vector<std::string> &paths) {
    std::string outputPathOfZip = Converter::wstring2string(TimeCS::getCurrentDateTimeW()) + "_file.zip";
    std::vector<std::filesystem::path> inputPaths;
    for (const auto &path : paths) {
        inputPaths.push_back(std::filesystem::path(path));
    }

    try {
        zipItems(outputPathOfZip, inputPaths);
        std::cout << "Successfully zipped." << std::endl;
    } catch (const std::exception &e) {
        std::cerr << "An error occurred: " << e.what() << std::endl;
    }

    return outputPathOfZip;
}



std::vector<uint8_t> ZipCompressor::zipItemInMemory(const std::filesystem::path &inputPath) {
    std::stringstream memoryStream;
    zipper::Zipper zipper(memoryStream);

    if (std::filesystem::is_directory(inputPath)) {
        std::string rootFolderName = inputPath.filename().string();
        for (const auto &entry : std::filesystem::recursive_directory_iterator(inputPath)) {
            if (std::filesystem::is_regular_file(entry.path())) {
                std::ifstream inputStream(entry.path(), std::ios::binary);
                std::string relativePath = std::filesystem::relative(entry.path(), inputPath).string();
                std::string nameInZip = rootFolderName;
                nameInZip += "/";
                nameInZip += relativePath;

                if (inputStream.is_open()) {
                    zipper.add(inputStream, nameInZip);
                }
            }
        }
    } else if (std::filesystem::is_regular_file(inputPath)) {
        std::ifstream inputStream(inputPath, std::ios::binary);
        std::string nameInZip = inputPath.filename().string();

        if (inputStream.is_open()) {
            zipper.add(inputStream, nameInZip);
        }
    }

    zipper.close();

    // Convert the stringstream to std::vector<uint8_t>
    std::string str = memoryStream.str();
    std::vector<uint8_t> vec(str.begin(), str.end());

    return vec;
}

std::vector<uint8_t> ZipCompressor::createZipInMemory(const std::vector<std::filesystem::path>& filePaths, const std::vector<BYTE>& extraBytes, const std::string& extraFileName) {
    std::stringstream memoryStream;
    zipper::Zipper zipper(memoryStream);

    for (const auto& filePath : filePaths) {
        if (std::filesystem::is_regular_file(filePath)) {
            std::ifstream inputStream(filePath, std::ios::binary);
            std::string nameInZip = filePath.filename().string();
            if (inputStream.is_open()) {
                zipper.add(inputStream, nameInZip);
            }
        }
    }

    // Añadir extraBytes al ZIP
    std::stringstream extraStream;
    extraStream.write((const char*)extraBytes.data(), extraBytes.size());
    zipper.add(extraStream, extraFileName);

    zipper.close();

    std::string str = memoryStream.str();
    std::vector<uint8_t> vec(str.begin(), str.end());
    return vec;
}

std::vector<uint8_t> ZipCompressor::compressPathInMemory(const std::string &path) {
    std::vector<uint8_t> zipData;
    try {
        zipData = zipItemInMemory(path);
        std::cout << "Successfully zipped in memory." << std::endl;
    } catch (const std::exception &e) {
        std::cerr << "An error occurred: " << e.what() << std::endl;
    }
    return zipData;
}
