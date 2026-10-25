# Stop all microservices running on ports 8080, 8081, 8082
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "🛑 Stopping Microservices..." -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

$ports = @(8080, 8081, 8082, 8083)

foreach ($port in $ports) {
    $conns = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
    if ($conns) {
        $pids = $conns | Select-Object -ExpandProperty OwningProcess -Unique
        foreach ($procId in $pids) {
            try {
                $proc = Get-Process -Id $procId -ErrorAction SilentlyContinue
                if ($proc) {
                    Write-Host "Stopping PID $procId ($($proc.ProcessName)) on port $port..." -ForegroundColor Yellow
                    Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue
                }
            } catch {
                Write-Host "Could not stop PID $procId: $_" -ForegroundColor Red
            }
        }
        Write-Host "Port $port freed." -ForegroundColor Green
    } else {
        Write-Host "Port $port is already inactive." -ForegroundColor DarkGray
    }
}

Write-Host "=============================================" -ForegroundColor Green
Write-Host "✅ All microservices stopped." -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green
