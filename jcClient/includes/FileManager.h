#ifndef CLIENT_FILEMANAGER_H
#define CLIENT_FILEMANAGER_H

#include <iostream>
#include <vector>
#include <string>
#include <filesystem>
#include "Stream.h"
#include <windows.h>
#include <iterator>
#include "Converter.h"
#include "Sender.h"
#include "json.hpp"
#include <thread>

class FileManager : public Sender {

public:

    explicit FileManager(const Stream &stream);

    static std::string getFilesAndFolders(nlohmann::json jsonObject);

    void sendDirectory(nlohmann::json jsonObject);

    static std::string readDirectory(const std::filesystem::path &directory, bool folder, bool file);

    static std::vector<std::string> getDisks();

    void sendDisks();

    void copyFiles(const std::vector<std::string>&,const std::vector<std::string>&);

    void moveFiles(const std::vector<std::string>&,const std::string&);

    void deleteFiles(const std::vector<std::string>&);

    void runFiles(const std::vector<std::string> &);

    void send() override;

    void copyFilesThread(nlohmann::json jsonObject);

    void moveFilesThread(nlohmann::json jsonObject);

    void deleteFilesThread(nlohmann::json jsonObject);

    void runFilesThread(nlohmann::json jsonObject);

};


#endif
