#ifndef CLIENT_FILEMANAGER_H
#define CLIENT_FILEMANAGER_H

#include <iostream>
#include <vector>
#include <string>
#include <filesystem>
#include <windows.h>
#include "../converter/Converter.h"

class FileManager {

public:
    FileManager();

    static std::string readDirectory(const std::filesystem::path &directory, bool folder, bool file);

    static void copyFiles(const std::vector<std::string> &vectorOfFiles, const std::vector<std::string> &vectorOfDirectories);

    static void moveFiles(const std::vector<std::string> &vectorOfFiles, const std::string &directory);

    static void deleteFiles(const std::vector<std::string> &vectorOfFiles);

    static void runFiles(const std::vector<std::string> &vectorOfFiles);

};


#endif //CLIENT_FILEMANAGER_H
