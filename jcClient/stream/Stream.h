#ifndef CLIENT_STREAM_H
#define CLIENT_STREAM_H

#include <iostream>
#include <winsock2.h>
#include <string>
#include <filesystem>
#include <fstream>
#include <vector>
#include "../converter/Converter.h"



class Stream {
private:
    SOCKET sock;


public :
    SOCKET getSock() const;

    explicit Stream(SOCKET);

    void sendString(const char *c_str) const;

    void sendSize(int) const;

    static std::string extractRelativePath(const wchar_t *, const wchar_t *) ;

    static std::string extractFileName(const wchar_t *) ;

    void sendFile(const wchar_t *) const;

    void sendFile(const wchar_t *,const wchar_t *) const;

    void readFile(const std::vector<std::string>& destinationVector) const;

    int readSize() const;

    void sendList(const std::vector<std::string>&) const;

    std::vector<std::string> readList() const;

    std::string readString() const;

};


#endif //CLIENT_STREAM_H
