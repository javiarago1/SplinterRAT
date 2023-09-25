CXX = g++
CXXFLAGS = -Iincludes -IC:opencv_static/include -I./zlib/include -I./zipper/include -I./websocketpp-0.8.2 -I./boost.asio/include/boost-1_83/ -Os -ffunction-sections -fdata-sections -std=c++17
LDFLAGS = -Lopencv_static/lib -L./zlib/lib -L./zipper/lib -Wl,--gc-sections
LIBS = -lopencv_gapi460 -lopencv_highgui460 -lopencv_ml460 -lopencv_objdetect460 \
       -lopencv_photo460 -lopencv_stitching460 -lopencv_video460 -lopencv_calib3d460 \
       -lopencv_features2d460 -lopencv_dnn460 -lopencv_flann460 -lopencv_videoio460 \
       -lopencv_imgcodecs460 -lopencv_imgproc460 -lopencv_core460 -llibprotobuf -lade \
       -llibjpeg-turbo -llibwebp -llibpng -llibtiff -llibopenjp2 -lIlmImf -lzlib \
       -lquirc -lwsock32 -lcomctl32 -lgdi32 -lole32 -lsetupapi -lws2_32 -loleaut32 \
       -luuid -lcomdlg32 -lwininet -static-libgcc -static-libstdc++ -Wl,-Bstatic \
       -lstdc++ -lpthread -lgdiplus -lCrypt32 -Wl,-Bdynamic -lws2_32 -lwininet -lstrmiids \
       -lgdiplus -lCrypt32 -lZipper-static -lzlibstatic -lstdc++fs

SOURCES = $(wildcard src/*.cpp) $(wildcard src/*/*.cpp) $(wildcard src/*/*/*.cpp)
OBJECTS = $(SOURCES:.cpp=.o)
TARGET = client.exe

$(TARGET): $(OBJECTS)
	$(CXX) $(OBJECTS) -o $(TARGET) $(LDFLAGS) $(LIBS)
	strip -s $(TARGET)

%.o: %.cpp
	$(CXX) -c $(CXXFLAGS) $< -o $@

clean:
	del /F /Q $(subst /,\,$(OBJECTS)) $(subst /,\,$(TARGET))
