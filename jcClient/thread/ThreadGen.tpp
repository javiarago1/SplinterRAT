#include "ThreadGen.h"

template <typename T>
void ThreadGen::runInNewThread(T* object, void (T::*method)()) {
    auto func = [object, method] { (object->*method)(); };
    std::thread t(func);
    t.detach();
}




