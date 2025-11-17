#include "InitializationManager.h"

// Підключаємо залежності тільки тут
#include "fileSystem/FileSystem.h"
#include "configManager/ConfigManager.h"

void InitializationManager::initialize() {
    // Ініціалізація файлової системи
    FileSystem::getInstance().initialize();

    // Ініціалізація конфігів (lazy load)
    ConfigManager::getInstance();
}
