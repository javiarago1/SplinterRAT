#include "FileManager.h"
#include "../converter/Converter.h"

std::vector<std::string> FileManager::readDirectory(const std::filesystem::path &directory, bool folder, bool file) {
    std::vector<std::string> paths;
    try {
        for (const auto &entry: std::filesystem::directory_iterator(directory)) {
            if (std::filesystem::is_directory(entry) && folder) {
                std::cout << "Is directory -> " << entry.path().filename() << std::endl;
                paths.push_back(entry.path().filename().string());
            } else if (std::filesystem::is_regular_file(entry) && file) {
                std::cout << "Is file -> " << entry.path().filename() << std::endl;
                paths.push_back(entry.path().filename().string());
            }
        }
    } catch (const std::filesystem::__cxx11::filesystem_error &) {
        paths.emplace_back("ACCESS_DENIED");
    }
    if (folder) paths.emplace_back("/");
    return paths;
}

void FileManager::copyFiles(const std::vector<std::string> &vectorOfFiles, const std::vector<std::string> &vectorOfDirectories) {
    for (const auto &directory: vectorOfDirectories) {
        for (const auto &file: vectorOfFiles) {
            std::filesystem::path pathI = std::filesystem::u8path(file);
            std::filesystem::path pathF = std::filesystem::u8path(directory);
            pathF /= pathI.filename();
            if (!std::filesystem::exists(pathF)) {
                std::filesystem::copy(pathI, pathF, std::filesystem::copy_options::overwrite_existing |
                                                    std::filesystem::copy_options::recursive);
            }
        }
    }
}

void FileManager::moveFiles(const std::vector<std::string> &vectorOfFiles, const std::string &directory) {
    for (const auto &file: vectorOfFiles) {
        std::filesystem::path pathI = std::filesystem::u8path(file);
        std::filesystem::path pathF = std::filesystem::u8path(directory);
        pathF /= pathI.filename();
        if (!std::filesystem::exists(pathF)) {
            std::filesystem::rename(pathI, pathF);
        }
    }
}

void FileManager::deleteFiles(const std::vector<std::string> &vectorOfFiles) {
    for (const auto &file: vectorOfFiles) {
        std::filesystem::remove_all(std::filesystem::u8path(file));
    }
}

void FileManager::runFiles(const std::vector<std::string> &vectorOfFiles) {
    for (const auto &file: vectorOfFiles) {
        STARTUPINFOW process_startup_info{};
        process_startup_info.cb = sizeof(process_startup_info);
        PROCESS_INFORMATION process_info{};

        std::wstring convertedString = Converter::string2wstring(file); // converting string to wstring
        std::wstring commandLine;
        if (std::filesystem::is_directory(convertedString)) {
            commandLine.append(L"explorer.exe ");
        } else {
            commandLine.append(L"cmd.exe /c ");
        }
        commandLine.append(L"\"").append(convertedString).append(L"\"");
        if (CreateProcessW(nullptr, (LPWSTR) commandLine.data(), nullptr, nullptr, TRUE, 0, nullptr, nullptr,
                           &process_startup_info, &process_info)) {
            WaitForSingleObject(process_info.hProcess, INFINITE);
            CloseHandle(process_info.hProcess);
            CloseHandle(process_info.hThread);
        }
    }
}

FileManager::FileManager() = default;
