#ifndef CLIENT_FILEMANAGER_H
#define CLIENT_FILEMANAGER_H

#include <iostream>
#include <vector>
#include <string>
#include <filesystem>

#include <windows.h>
#include <iterator>
#include "Converter.h"

#include "json.hpp"
#include "Handler.h"
#include <thread>

class FileManager : public Handler {

public:

    explicit FileManager(ClientSocket &clientSocket);

    void sendDirectory(nlohmann::json jsonObject);

    static nlohmann::json readDirectory(const std::filesystem::path &directory);

    static std::vector<std::string> getDisks();

    void sendDisks();

    void copyFiles(const std::vector<std::string>&,const std::vector<std::string>&);

    void moveFiles(const std::vector<std::string>&,const std::string&);

    void deleteFiles(const std::vector<std::string>&);

    void runFiles(const std::vector<std::string> &);


    void copyFilesThread(nlohmann::json jsonObject);

    void moveFilesThread(nlohmann::json jsonObject);

    void deleteFilesThread(nlohmann::json jsonObject);

    void runFilesThread(nlohmann::json jsonObject);

};


#endif
