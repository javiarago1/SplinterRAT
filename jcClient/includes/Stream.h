#ifndef CLIENT_STREAM_H
#define CLIENT_STREAM_H

#include <iostream>
#include <winsock2.h>
#include <string>
#include <filesystem>
#include <fstream>
#include <vector>

#include "Converter.h"


enum class RESULT { SR_ERROR,
    SR_SUCCESS, SR_UNDEFINED};

class Stream {
private:
    inline static RESULT defaultResult = RESULT::SR_UNDEFINED;
    SOCKET sock;


public :
    SOCKET getSock() const;
    //Packet packet;
    //Packet readProtoMessage(RESULT& result);

    explicit Stream(SOCKET);

    void sendString(const char *c_str) const;

    void sendSize(int) const;

    static std::string extractRelativePath(const wchar_t *, const wchar_t *) ;

    static std::string extractFileName(const wchar_t *) ;

    void sendFile(const wchar_t *,RESULT& result=defaultResult) const;

    void sendFile(const wchar_t *,const wchar_t *,RESULT& result=defaultResult) const;

    void readFile(const std::string & destination,RESULT&) const;

    int readSize(RESULT& result=defaultResult) const;

    void sendList(const std::vector<std::string>&) const;

    std::vector<std::string> readList() const;

    std::string readString() const;

};


#endif //CLIENT_STREAM_H
