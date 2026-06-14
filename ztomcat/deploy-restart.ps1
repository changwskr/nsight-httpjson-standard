$ErrorActionPreference = 'Stop'
$ZTomcatHome = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host '[ztomcat] Stop -> deploy WARs -> start -> verify'
& (Join-Path $ZTomcatHome 'stop.ps1')
Start-Sleep -Seconds 3
& (Join-Path $ZTomcatHome 'deploy-wars.bat')
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& (Join-Path $ZTomcatHome 'start.ps1')

Write-Host '[ztomcat] Waiting for WAR deployments (~5 min for 19 modules)...'
$deadline = (Get-Date).AddMinutes(6)
$contexts = @('cc','ic','pc','bc','ms','sv','pd','cm','eb','ep','bp','bd','ss','cs','ct','mg','om','ud','et')
do {
    Start-Sleep -Seconds 15
    $up = 0
    foreach ($ctx in $contexts) {
        try {
            $null = Invoke-WebRequest -Uri "http://localhost:8080/$ctx/actuator/health" -UseBasicParsing -TimeoutSec 5
            $up++
        } catch { }
    }
    Write-Host "  health OK: $up / $($contexts.Count)"
    if ($up -eq $contexts.Count) { break }
} while ((Get-Date) -lt $deadline)

& (Join-Path $ZTomcatHome 'verify-deploy.ps1')
