//
// Created by JAVIER on 16/11/2022.
//

#ifndef CLIENT_INSTALL_H
#define CLIENT_INSTALL_H
#include <string>
#include <filesystem>
#include <iostream>


class Install {
public:
    static void installClient(std::string whereToInstall, const std::string& locationOfCurrentExe);
};


#endif
