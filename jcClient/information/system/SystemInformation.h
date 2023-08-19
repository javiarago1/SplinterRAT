#ifndef CLIENT_CLIENTINFORMATION_H
#define CLIENT_CLIENTINFORMATION_H
#include "../../stream/Stream.h"
#include <windows.h>
#include <string>
#include <lmcons.h>
#include <sys/stat.h>
#include <vector>
#include <iterator>
#include <sstream>
#include "../../configuration.h"
#include "../../Sender/Sender.h"
#include "../../file/FileManager.h"

class SystemInformation : public Sender {
public:
    explicit SystemInformation(const Stream &stream);

    static std::string getWindowsVersion();

    static std::string getUsername();

    static std::vector<std::string> getSystemInformation();

    static std::string vector2string (std::vector<std::string>);

    void send() override;

};


#endif
