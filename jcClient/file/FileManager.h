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

class FileManager : public Sender {

public:

    explicit FileManager(const Stream &stream);

    std::string readAll();

    static std::string readDirectory(const std::filesystem::path &directory, bool folder, bool file);

    void copyFiles();

    void moveFiles();

    void deleteFiles();

    void runFiles();

    void uploadFiles();

    void send() override;

};


#endif
