@echo off
chcp 65001 >nul
echo.
echo ========================================
echo    系统健康检查
echo ========================================
echo.

echo [检查 1/5] 测试后端 API 连通性...
curl -s http://localhost:8080/api/admin/businesses >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ 后端 API 响应正常
) else (
    echo ❌ 后端 API 无响应，请检查后端日志
    echo    日志位置: backend\backend.log
)

echo.
echo [检查 2/5] 测试前端服务连通性...
curl -s http://localhost:8081 >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ 前端服务响应正常
) else (
    echo ❌ 前端服务无响应，请检查前端日志
    echo    日志位置: frontend\frontend.log
)

echo.
echo [检查 3/5] 测试管理员登录接口...
curl -s -X POST http://localhost:8080/api/admin/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"123456\"}" > temp_login.json 2>&1
if %errorlevel% equ 0 (
    findstr /C:"\"success\":true" temp_login.json >nul 2>&1
    if %errorlevel% equ 0 (
        echo ✅ 管理员登录接口测试通过
    ) else (
        echo ❌ 管理员登录失败，请检查数据库连接
    )
) else (
    echo ❌ 登录接口无响应
)
del temp_login.json >nul 2>&1

echo.
echo [检查 4/5] 测试数据库连接...
curl -s http://localhost:8080/api/admin/businesses > temp_db.json 2>&1
if %errorlevel% equ 0 (
    findstr /C:"\"success\":true" temp_db.json >nul 2>&1
    if %errorlevel% equ 0 (
        echo ✅ 数据库连接正常
    ) else (
        echo ❌ 数据库查询失败
    )
) else (
    echo ❌ 数据库连接测试失败
)
del temp_db.json >nul 2>&1

echo.
echo [检查 5/5] 测试 CORS 配置...
curl -s -H "Origin: http://localhost:8081" -I http://localhost:8080/api/admin/businesses | findstr /C:"Access-Control-Allow-Origin" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ CORS 配置正常
) else (
    echo ⚠️  CORS 配置可能有问题（前端可能遇到跨域错误）
)

echo.
echo ========================================
echo    检查完成！
echo ========================================
echo.
echo 💡 提示:
echo    - 如果所有检查都通过，可以访问 http://localhost:8081 使用系统
echo    - 如果有检查失败，请查看对应的日志文件排查问题
echo.
