#include "ConfigManager.h"
#include <filesystem>
#include <fstream>
#include <iostream>
#include "../../external/nlohmann/json.hpp"

namespace fs = std::filesystem;
using json = nlohmann::json;

void ConfigManager::loadOrCreateConfig() {
    if (!fs::exists(configPath)) {
        std::cout << "Config file not found, creating default config..." << std::endl;

        configData["db_host"] = "127.0.0.1";
        configData["db_port"] = "5432";
        configData["db_user"] = "user";
        configData["db_password"] = "password";

        saveConfig();
        loaded = true;
        return;
    }

    std::ifstream inFile(configPath);
    if (!inFile.is_open()) {
        std::cerr << "[ConfigManager] Cannot open config file: " << configPath << std::endl;
        return;
    }

    try {
        json j;
        inFile >> j;

        configData.clear();
        for (auto& [key, value] : j.items())
            configData[key] = value.get<std::string>();

        loaded = true;
    }
    catch (const json::parse_error& e) {
        std::cerr << "[ConfigManager] JSON parse error: " << e.what() << std::endl;
    }
}

std::string ConfigManager::getConfigValue(const std::string& key) {
    if (!loaded) loadOrCreateConfig();

    auto it = configData.find(key);
    return (it != configData.end()) ? it->second : "";
}

void ConfigManager::setConfigValue(const std::string& key, const std::string& value) {
    if (!loaded) loadOrCreateConfig();

    configData[key] = value;
    saveConfig();
}

void ConfigManager::saveConfig() {
    try {
        fs::create_directories(fs::path(configPath).parent_path());

        std::ofstream outFile(configPath);
        if (!outFile.is_open()) {
            std::cerr << "[ConfigManager] Cannot open config file for writing: "
                      << configPath << std::endl;
            return;
        }

        json j(configData);
        outFile << j.dump(4);
    }
    catch (const fs::filesystem_error& e) {
        std::cerr << "[ConfigManager] Filesystem error: " << e.what() << std::endl;
    }
}
