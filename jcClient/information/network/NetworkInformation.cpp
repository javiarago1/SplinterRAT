#include "NetworkInformation.h"

std::string NetworkInformation::requestNetworkInformation() {

    HINTERNET net = InternetOpen("IP retriever",
                                 INTERNET_OPEN_TYPE_PRECONFIG,
                                 nullptr,
                                 nullptr,
                                 0);

    HINTERNET conn = InternetOpenUrl(net,
                                     "http://ip-api.com/json/?fields=9629695",
                                     nullptr,
                                     0,
                                     INTERNET_FLAG_RELOAD,
                                     0);

    char buffer[4096];
    DWORD read;
    InternetReadFile(conn, buffer, sizeof(buffer) / sizeof(buffer[0]), &read);
    InternetCloseHandle(net);
    std::string str(buffer, read);
    return str;
}


NetworkInformation::NetworkInformation()=default;
