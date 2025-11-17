#pragma once

class InitializationManager {
public:
    // Отримання інстансу Singleton
    static InitializationManager& getInstance() {
        static InitializationManager instance;
        return instance;
    }

    // Статичний метод ініціалізації програми
    static void initialize();

public:
    // Забороняємо копіювання та присвоєння (Clang-Tidy: повинні бути public)
    InitializationManager(const InitializationManager&) = delete;
    InitializationManager& operator=(const InitializationManager&) = delete;

private:
    InitializationManager() = default;
};
