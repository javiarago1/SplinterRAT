
#ifndef CLIENT_SYSTEMSTATE_H
#define CLIENT_SYSTEMSTATE_H
#include <windows.h>
#include "json.hpp"

class SystemState {
public:
    static void setState(nlohmann::json &json);
};


#endif
