#ifndef CLIENT_NETWORKINFORMATION_H
#define CLIENT_NETWORKINFORMATION_H

#include <string>
#include <windows.h>
#include <wininet.h>

#include "json.hpp"


class NetworkInformation {
public:
    static nlohmann::json getNetworkInformation();
};


#endif //CLIENT_NETWORKINFORMATION_H
