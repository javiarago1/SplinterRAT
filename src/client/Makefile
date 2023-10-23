ifeq ($(OS),Windows_NT)
    override CXX = g++
    WINDRES = windres
    RM = del /F /Q
    SLASH = \\
else
    override CXX = x86_64-w64-mingw32-g++
    WINDRES = x86_64-w64-mingw32-windres
    RM = rm -f
    SLASH = /
endif

# Después de esta línea...
ifneq (,$(TARGET_PATH))
    TARGET = $(subst %20, ,$(TARGET_PATH))
endif

# Agrega esto para hacer "echo" de la ruta formada
$(info TARGET is now set to: $(TARGET))


CXXFLAGS =  -DASIO_STANDALONE -Iincludes -I./opencv_static/include -I./zlib/include -I./zipper/include -I./websocketpp-0.8.2 -I./asio/include -Os -ffunction-sections -fdata-sections -std=c++17
LDFLAGS = -Lopencv_static/lib -L./zlib/lib -L./zipper/lib -Wl,--gc-sections
LIBS = -lopencv_gapi460 -lopencv_highgui460 -lopencv_ml460 -lopencv_objdetect460 \
       -lopencv_photo460 -lopencv_stitching460 -lopencv_video460 -lopencv_calib3d460 \
       -lopencv_features2d460 -lopencv_dnn460 -lopencv_flann460 -lopencv_videoio460 \
       -lopencv_imgcodecs460 -lopencv_imgproc460 -lopencv_core460 -llibprotobuf -lade \
       -llibjpeg-turbo -llibwebp -llibpng -llibtiff -llibopenjp2 -lIlmImf -lzlib \
       -lquirc -lwsock32 -lcomctl32 -lgdi32 -lole32 -lsetupapi -lws2_32 -loleaut32 \
       -luuid -lcomdlg32 -lwininet -static-libgcc -static-libstdc++ -Wl,-Bstatic \
       -lstdc++ -lpthread -lgdiplus -Wl,-Bdynamic -lws2_32 -lwininet -lstrmiids \
       -lgdiplus -lcrypt32 -lZipper-static -lzlibstatic -liphlpapi -lstdc++fs -mwindows

SOURCES = $(wildcard src/*.cpp) $(wildcard src/*/*.cpp) $(wildcard src/*/*/*.cpp)
OBJECTS = $(SOURCES:.cpp=.o) compile_configuration/compiled_assembly.opc
TARGET ?= client.exe


all: $(OBJECTS)
	$(CXX) $(OBJECTS) -o "$(TARGET)" $(LDFLAGS) $(LIBS)
	strip -s "$(TARGET)"

%.o: %.cpp
	$(CXX) -c $(CXXFLAGS) $< -o $@

compile_configuration/compiled_assembly.opc: compile_configuration/assembly.rc
	$(WINDRES) $< $@

clean:
	$(RM) $(subst /,$(SLASH),$(OBJECTS)) $(subst /,$(SLASH),"$(TARGET)")