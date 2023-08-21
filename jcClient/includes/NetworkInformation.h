#ifndef CLIENT_NETWORKINFORMATION_H
#define CLIENT_NETWORKINFORMATION_H
#include "Stream.h"
#include <string>
#include <windows.h>
#include <wininet.h>
#include "Sender.h"


class NetworkInformation : public Sender {
public:
    explicit NetworkInformation(const Stream &stream);
    void send() override;
    static std::string getNetworkInformation();

};


#endif //CLIENT_NETWORKINFORMATION_H
