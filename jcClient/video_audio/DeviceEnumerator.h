#include "../Sender/Sender.h"
#include <Windows.h>
#include <initguid.h>
#include <dshow.h>
#include <map>
#include <string>
#include <vector>
#include <string>



struct Device  {
	int id; // This can be used to open the device in OpenCV
	std::string devicePath;
	std::string deviceName; // This can be used to show the devices to the user
};

class DeviceEnumerator : public Sender {

public:
    void send() override;

    explicit DeviceEnumerator(const Stream &stream);
    static std::map<int, Device> getDevicesMap(GUID deviceClass);
	static std::map<int, Device> getVideoDevicesMap();
    static int getIndexOfWebcamByName(const std::string&);
private:
    std::vector<std::string> getVectorDevicesNames();
	static std::string ConvertBSTRToMBS(BSTR bstr);
	static std::string ConvertWCSToMBS(const wchar_t* pstr, long wslen);

};
