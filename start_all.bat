@echo off
chcp 65001 >nul
echo ========================================
echo    饿了么商家后台管理系统 - 一键启动
echo ========================================
echo.

echo [1/4] 检查 MySQL 服务状态...
sc query mysql >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ MySQL 服务未启动，请先启动 MySQL 服务
    pause
    exit /b 1
)
echo ✅ MySQL 服务运行正常

echo.
echo [2/4] 检查端口占用情况...
netstat -ano | findstr ":8080" >nul 2>&1
if %errorlevel% equ 0 (
    echo ⚠️  警告: 8080 端口已被占用，后端可能无法启动
) else (
    echo ✅ 8080 端口可用（后端）
)

netstat -ano | findstr ":8081" >nul 2>&1
if %errorlevel% equ 0 (
    echo ⚠️  警告: 8081 端口已被占用，前端可能无法启动
) else (
    echo ✅ 8081 端口可用（前端）
)

echo.
echo [3/4] 启动后端服务（Spring Boot）...
echo 正在后台启动后端，请稍候...
cd /d "%~dp0backend"
start "ELM-Backend" cmd /c "mvn spring-boot:run > backend.log 2>&1"

echo 等待后端启动（预计 30 秒）...
timeout /t 5 /nobreak >nul
echo 后端正在启动中... (5/30秒)
timeout /t 5 /nobreak >nul
echo 后端正在启动中... (10/30秒)
timeout /t 5 /nobreak >nul
echo 后端正在启动中... (15/30秒)
timeout /t 5 /nobreak >nul
echo 后端正在启动中... (20/30秒)
timeout /t 5 /nobreak >nul
echo 后端正在启动中... (25/30秒)
timeout /t 5 /nobreak >nul

echo.
echo [4/4] 启动前端服务（Vue）...
echo 正在后台启动前端，请稍候...
cd /d "%~dp0frontend"
start "ELM-Frontend" cmd /c "npm run serve > frontend.log 2>&1"

echo 等待前端启动（预计 20 秒）...
timeout /t 10 /nobreak >nul
echo 前端正在启动中... (10/20秒)
timeout /t 10 /nobreak >nul

echo.
echo ========================================
echo    启动完成！
echo ========================================
echo.
echo 📌 后端地址: http://localhost:8080
echo 📌 前端地址: http://localhost:8081
echo.
echo 📋 查看日志:
echo    - 后端日志: backend\backend.log
echo    - 前端日志: frontend\frontend.log
echo.
echo 🔍 进行健康检查...
cd /d "%~dp0"
call check_services.bat

pause
