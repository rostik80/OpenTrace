#pragma once
#include <string>
#include <vector>

class FileSystem {
public:
    // Повертає єдиний екземпляр
    static FileSystem& getInstance() {
        static FileSystem instance;
        return instance;
    }

    // Ініціалізація всіх потрібних директорій
    void initialize();

    // Перевірка чи існує директорія (можна викликати без об'єкта)
    static bool directoryExists(const std::string& path);

    // Створення директорії (можна викликати без об'єкта)
    static bool createDirectory(const std::string& path);

    // Створення декількох директорій
    static void createDirectories(const std::vector<std::string>& paths);

    // Очистка тимчасової папки (tmp/)
    static void clearTemporary();

public:  // Видалені функції публічні
    FileSystem(const FileSystem&) = delete;
    FileSystem& operator=(const FileSystem&) = delete;

private:
    FileSystem() = default;

    // Шляхи до ключових директорій
    std::string dataPath = "data";
    std::string logsPath = "logs";
    std::string tmpPath = "tmp";
    std::string cachePath = "tmp/cache";
};
