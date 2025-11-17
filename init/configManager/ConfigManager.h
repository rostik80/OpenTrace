#pragma once
#include <string>
#include <unordered_map>

class ConfigManager {
public:
    static ConfigManager& getInstance() {
        static ConfigManager instance; // Ніякої логіки всередині!
        return instance;
    }

    std::string getConfigValue(const std::string& key);
    void setConfigValue(const std::string& key, const std::string& value);

public:
    ConfigManager(const ConfigManager&) = delete;
    ConfigManager& operator=(const ConfigManager&) = delete;

private:
    ConfigManager() = default;

    std::string configPath = "config/config.json";
    std::unordered_map<std::string, std::string> configData;

    bool loaded = false;

    void loadOrCreateConfig();
    void saveConfig();
};
