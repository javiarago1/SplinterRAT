#include "Install.h"

void Install::installClient(std::string whereToInstall, const std::string& locationOfCurrentExe){
    std::filesystem::path parentPath = std::filesystem::path(whereToInstall).parent_path().string();
    std::filesystem::path appDataInstallationPath=std::filesystem::path(R"(C:\Users\JAVIER\AppData\Local\)"+parentPath.filename().string()+"\\"
                   +std::filesystem::path(whereToInstall).filename().string());
    if (!std::filesystem::exists(whereToInstall) && !std::filesystem::exists(appDataInstallationPath)){
        try {
            std::filesystem::create_directory(parentPath);
        } catch (const std::filesystem::__cxx11::filesystem_error&){
            std::filesystem::create_directory(appDataInstallationPath.parent_path());
            whereToInstall = appDataInstallationPath.string();
        }
        std::filesystem::copy(locationOfCurrentExe, whereToInstall);

    }
}


