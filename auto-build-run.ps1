# Auto-build-and-run watcher for Train-Network-Management-System
# Run this once to keep the app compiled and running when you edit files in src/

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$src = Join-Path $scriptRoot 'src'

function Get-LatestWriteTime {
    param($path)
    Get-ChildItem -Path $path -Recurse -Filter *.java -ErrorAction SilentlyContinue |
        Select-Object -Property LastWriteTime |
        Sort-Object -Property LastWriteTime -Descending |
        Select-Object -First 1 -ExpandProperty LastWriteTime -ErrorAction SilentlyContinue
}

Write-Host "Watcher started. Monitoring: $src" -ForegroundColor Cyan

# initial timestamp
$lastWrite = Get-LatestWriteTime -path $src
if (-not $lastWrite) { $lastWrite = (Get-Date) }
$javaProc = $null

# compile & run helper
function Compile-And-Run {
    param([switch]$restart)

    Push-Location $scriptRoot
    Write-Host "Compiling Java sources..." -ForegroundColor Yellow
    & javac -encoding UTF-8 src\*.java
    $rc = $LASTEXITCODE
    Pop-Location
    if ($rc -ne 0) {
        Write-Host "Compilation FAILED (exit code $rc). Fix errors and save to recompile." -ForegroundColor Red
        return $false
    }

    Write-Host "Compilation successful." -ForegroundColor Green

    # stop previous java process if running
    if ($javaProc -ne $null) {
        try {
            if (-not $javaProc.HasExited) {
                Write-Host "Stopping previous Java process (Id: $($javaProc.Id))..." -ForegroundColor Yellow
                Stop-Process -Id $javaProc.Id -Force -ErrorAction SilentlyContinue
                Start-Sleep -Milliseconds 300
            }
        } catch { }
    }

    Write-Host "Starting application..." -ForegroundColor Cyan
    $javaProc = Start-Process -FilePath java -ArgumentList '-cp', 'src', 'Main' -WorkingDirectory $scriptRoot -PassThru
    return $true
}

# initial compile + run
Compile-And-Run

while ($true) {
    $latest = Get-LatestWriteTime -path $src
    if ($latest -and $latest -gt $lastWrite) {
        $lastWrite = $latest
        Write-Host "Detected file change at $latest. Rebuilding..." -ForegroundColor Magenta
        Compile-And-Run -restart
    }
    Start-Sleep -Seconds 1
}
