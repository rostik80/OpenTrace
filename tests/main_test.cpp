#include <iostream>
#include <cassert>
#include <filesystem>

#include "../init/fileSystem/FileSystem.h"
#include "../init/configManager/ConfigManager.h"
#include "../init/InitializationManager.h"

namespace fs = std::filesystem;

int main() {
    std::cout << "=== TESTS START ===\n";

    // -- Готуємо чистий тестовий стан --
    fs::remove_all("config");
    fs::remove_all("data");
    fs::remove_all("logs");
    fs::remove_all("tmp");

    // =========================
    // Test 1: FileSystem
    // =========================

    FileSystem& fsys = FileSystem::getInstance();
    fsys.initialize();

    assert(fsys.directoryExists("data"));
    assert(fsys.directoryExists("logs"));
    assert(fsys.directoryExists("tmp"));
    assert(fsys.directoryExists("tmp/cache"));
    std::cout << "FileSystem: directories creation test passed.\n";

    // Test clearTemporary()
    fsys.clearTemporary();
    assert(fsys.directoryExists("tmp"));
    assert(fsys.directoryExists("tmp/cache"));
    std::cout << "FileSystem: clearTemporary test passed.\n";

    // =========================
    // Test 2: ConfigManager
    // =========================

    // Ніякого loadOrCreateConfig() більше!
    ConfigManager& cfg = ConfigManager::getInstance();

    // Перший get → автоматично створює файл config.json з дефолтами
    assert(cfg.getConfigValue("db_host") == "127.0.0.1");
    assert(cfg.getConfigValue("db_port") == "5432");
    std::cout << "ConfigManager: default config load test passed.\n";

    // Test set/get
    cfg.setConfigValue("test_key", "test_value");
    assert(cfg.getConfigValue("test_key") == "test_value");
    std::cout << "ConfigManager: set/get value test passed.\n";

    // =========================
    // Test 3: InitializationManager
    // =========================

    // Викликаємо статично
    InitializationManager::initialize();

    // Перевіряємо всю інтеграцію
    assert(fsys.directoryExists("data"));
    assert(fsys.directoryExists("logs"));
    assert(fs::exists("config/config.json"));
    std::cout << "InitializationManager: integration test passed.\n";

    std::cout << "=== ALL TESTS PASSED ===\n";
    return 0;
}
