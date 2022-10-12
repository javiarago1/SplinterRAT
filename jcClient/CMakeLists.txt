cmake_minimum_required(VERSION 3.22)
project(Client)

set(CMAKE_CXX_STANDARD 20)

set(OpenCV_DIR "C:\\opencv\\mingw-build\\install")

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/cmake/")

find_package(OpenCV REQUIRED)

include_directories(${OpenCV_INCLUDE_DIRS})

add_executable(Client client.cpp video_audio/DeviceEnumerator.cpp webcam/WebcamManager.cpp stream/Stream.cpp stream/Stream.h time/Time.cpp time/Time.h converter/Converter.cpp download/Download.cpp download/Download.h file/FileManager.cpp file/FileManager.h information/system/SystemInformation.cpp information/system/SystemInformation.h information/network/NetworkInformation.cpp information/network/NetworkInformation.h)

set(OpenCV_LIBS opencv_core opencv_imgproc opencv_highgui opencv_imgcodecs)


target_link_libraries(Client ${OpenCV_LIBS})
target_link_libraries(Client ws2_32)
target_link_libraries(Client wininet)
target_link_libraries(Client stdc++fs)
target_link_libraries(Client strmiids)

