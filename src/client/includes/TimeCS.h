#include <string>

#ifndef CLIENT_TIME_H
#define CLIENT_TIME_H

/*
 * Class designated to provide unique time strings
 */
class TimeCS {

public:
    TimeCS();
    static std::wstring getCurrentDateTimeW();
};


#endif
