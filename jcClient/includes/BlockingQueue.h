#ifndef CLIENT_BLOCKINGQUEUE_H
#define CLIENT_BLOCKINGQUEUE_H


#include <queue>
#include <mutex>
#include <condition_variable>

template <typename T>
class BlockingQueue {
private:
    std::queue<T> queue_;
    mutable std::mutex mutex_;
    std::condition_variable cond_;

public:
    void push(T const& value) {
        {
            std::unique_lock<std::mutex> lock(mutex_);
            queue_.push(value);
        }
        cond_.notify_one();
    }

    T pop() {
        std::unique_lock<std::mutex> lock(mutex_);
        cond_.wait(lock, [this] { return !queue_.empty(); });
        T val = queue_.front();
        queue_.pop();
        return val;
    }

    bool empty() const {
        std::lock_guard<std::mutex> lock(mutex_);
        return queue_.empty();
    }

    size_t size() const {
        std::lock_guard<std::mutex> lock(mutex_);
        return queue_.size();
    }
};


#endif
