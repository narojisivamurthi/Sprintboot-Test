# Start all microservices in separate Windows terminal windows
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "🚀 Launching Microservices Architecture..." -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

$root = $PSScriptRoot

# 1. Start Auth Service (Port 8081)
Write-Host "Starting Auth Service on port 8081..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root'; Write-Host '--- AUTH SERVICE (Port 8081) ---' -ForegroundColor Green; .\mvnw.cmd spring-boot:run -pl auth-service"

# 2. Start Employee Service (Port 8082)
Write-Host "Starting Employee Service on port 8082..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root'; Write-Host '--- EMPLOYEE SERVICE (Port 8082) ---' -ForegroundColor Green; .\mvnw.cmd spring-boot:run -pl employee-service"

# 3. Start Notification Service (Port 8083)
Write-Host "Starting Notification Service on port 8083..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root'; Write-Host '--- NOTIFICATION SERVICE (Port 8083) ---' -ForegroundColor Green; .\mvnw.cmd spring-boot:run -pl notification-service"

# Wait a few seconds for downstream services to initialize
Start-Sleep -Seconds 5

# 4. Start API Gateway (Port 8080)
Write-Host "Starting API Gateway on port 8080..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root'; Write-Host '--- API GATEWAY (Port 8080) ---' -ForegroundColor Green; .\mvnw.cmd spring-boot:run -pl api-gateway"

Write-Host "=============================================" -ForegroundColor Green
Write-Host "✅ All 4 microservices launched!" -ForegroundColor Green
Write-Host "   Gateway:      http://localhost:8080" -ForegroundColor White
Write-Host "   Auth:         http://localhost:8081" -ForegroundColor White
Write-Host "   Employee:     http://localhost:8082" -ForegroundColor White
Write-Host "   Notification: http://localhost:8083" -ForegroundColor White
Write-Host "=============================================" -ForegroundColor Green
