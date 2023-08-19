#ifndef CLIENT_THREADGEN_H
#define CLIENT_THREADGEN_H

#include <functional>
#include <thread>

class ThreadGen {
public:
    template <typename T>
    void runInNewThread(T* object, void (T::*method)());
};

#include "ThreadGen.tpp"


#endif
