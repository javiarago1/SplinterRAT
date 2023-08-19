#ifndef CLIENT_FILEMANAGER_H
#define CLIENT_FILEMANAGER_H

#include <iostream>
#include <vector>
#include <string>
#include <filesystem>
#include "../stream/Stream.h"
#include <windows.h>
#include <iterator>
#include "../converter/Converter.h"
#include "../Sender/Sender.h"
#include <thread>

class FileManager : public Sender {

public:

    explicit FileManager(const Stream &stream);

    std::string getFilesAndFolders();

    static std::string readDirectory(const std::filesystem::path &directory, bool folder, bool file);

    static std::vector<std::string> getDisks();

    void sendDisks();

    void copyFiles(const std::vector<std::string>&,const std::vector<std::string>&);

    void moveFiles(const std::vector<std::string>&,const std::string&);

    void deleteFiles(const std::vector<std::string>&);

    void runFiles(const std::vector<std::string> &);

    void uploadFiles();

    void send() override;

    void copyFilesThread();

    void moveFilesThread();

    void deleteFilesThread();

    void runFilesThread();
};


#endif
