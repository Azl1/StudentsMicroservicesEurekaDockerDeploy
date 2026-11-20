@echo off
chcp 65001 > nul
cls

:: Переходим в папку, где лежит сам скрипт (чтобы не зависеть от текущей директории)
cd /d "%~dp0"

:: ===================================================================
:: НАСТРОЙКИ
:: ===================================================================
set SERVER_IP=194.113.34.190
set USERNAME=root2
set KEY_FILE=.\id_rsa
set REMOTE_DIR=/home/root2/app
:: ===================================================================

echo ===================================================================
echo  🚀 АВТОМАТИЧЕСКИЙ ДЕПЛОЙ ПРОЕКТА
echo ===================================================================
echo.

:: ---------------------------------------------------------------
:: Шаг 1: Права на SSH-ключ
:: ---------------------------------------------------------------
echo [Шаг 1/3] Проверка безопасности SSH-ключа в Windows...
if not exist "%KEY_FILE%" (
    echo  ❌ ОШИБКА: Файл ключа "%KEY_FILE%" не найден в текущей папке!
    pause
    exit /b 1
)
icacls "%KEY_FILE%" /inheritance:r > nul
icacls "%KEY_FILE%" /grant:r "%USERNAME%":(R) > nul 2>&1
echo  - Права успешно проверены и обновлены.
echo.

:: ---------------------------------------------------------------
:: Шаг 2: Копирование файлов на сервер
:: ---------------------------------------------------------------
echo [Шаг 2/3] Копирование исходного кода на сервер Cloud.ru...
echo  - Отправка файлов: EurekaServer, AutosMicroservice, StudentsMicroservice, docker-compose.yml, config...

scp -i "%KEY_FILE%" -r ^
    EurekaServer ^
    AutosMicroservice ^
    StudentsMicroservice ^
    docker-compose.yml ^
    config ^
    %USERNAME%@%SERVER_IP%:%REMOTE_DIR%/

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo  ❌ ОШИБКА: Не удалось скопировать файлы на сервер! Проверьте сеть, IP или наличие файлов.
    pause
    exit /b 1
)
echo  - Файлы успешно скопированы в %REMOTE_DIR%
echo.

:: ---------------------------------------------------------------
:: Шаг 3: Пересборка и запуск контейнеров
:: ---------------------------------------------------------------
echo [Шаг 3/3] Пересборка и запуск Spring Boot в Docker на сервере...
echo  - Подключение по SSH и выполнение перезапуска контейнеров...
ssh -i "%KEY_FILE%" %USERNAME%@%SERVER_IP% "cd %REMOTE_DIR% && sudo docker compose up --build -d"
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo  ❌ ОШИБКА: Не удалось запустить контейнеры на сервере!
    pause
    exit /b 1
)
echo.
echo ===================================================================
echo  ✅ ДЕПЛОЙ УСПЕШНО ЗАВЕРШЕН!
echo  Приложение обновляется в фоновом режиме на сервере.
echo.
echo  Ссылки на сервисы:
echo  - Eureka:   http://%SERVER_IP%:8761
echo  - Students: http://%SERVER_IP%:8081/students
echo  - Autos:    http://%SERVER_IP%:8082/autos
echo ===================================================================
echo.

set /p choice="Хотите посмотреть логи запуска? (y/n): "
if /i "%choice%"=="y" (
    echo.
    echo Отображение логов (Для выхода нажмите Ctrl+C)...
    echo.
    ssh -i "%KEY_FILE%" %USERNAME%@%SERVER_IP% "cd %REMOTE_DIR% && sudo docker compose logs -f"
)

exit /b 0