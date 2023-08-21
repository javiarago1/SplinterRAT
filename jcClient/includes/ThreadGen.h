#ifndef CLIENT_THREADGEN_H
#define CLIENT_THREADGEN_H

#include <tuple>
#include <functional>
#include <iostream>
#include <thread>

class ThreadGen {
public:
    template <typename T, typename... Args>
    void runInNewThread(T* object, void (T::*method)(Args...), Args... args) {
        auto func = [object, method, args...] { (object->*method)(args...); };
        std::thread t(func);
        t.detach();
    }
};

#endif
