#ifndef CLIENT_NETWORKINFORMATION_H
#define CLIENT_NETWORKINFORMATION_H

#include <string>
#include <windows.h>
#include <wininet.h>

class NetworkInformation {
public:
    NetworkInformation();
    static std::string requestNetworkInformation();

};


#endif //CLIENT_NETWORKINFORMATION_H
