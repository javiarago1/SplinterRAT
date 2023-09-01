#ifndef CLIENT_NETWORKINFORMATION_H
#define CLIENT_NETWORKINFORMATION_H
#include "Stream.h"
#include <string>
#include <windows.h>
#include <wininet.h>
#include "Sender.h"


class NetworkInformation {
public:
    static std::string getNetworkInformation();
};


#endif //CLIENT_NETWORKINFORMATION_H
