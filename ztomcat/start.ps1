$ErrorActionPreference = 'Stop'

$ZTomcatHome = Split-Path -Parent $MyInvocation.MyCommand.Path
$CatalinaHome = Join-Path $ZTomcatHome 'apache-tomcat-10.1.34'

if (-not (Test-Path (Join-Path $CatalinaHome 'bin\catalina.bat'))) {
    Write-Host '[ztomcat] Tomcat not found. Run install-tomcat.bat first.'
    exit 1
}

$jdk21 = Join-Path $env:USERPROFILE '.jdks\temurin-21.0.4'
if (Test-Path $jdk21) {
    $env:JAVA_HOME = $jdk21
} elseif (-not $env:JAVA_HOME) {
    Write-Host '[ztomcat] JDK 21 not found at' $jdk21
    exit 1
}

$env:CATALINA_HOME = $CatalinaHome
$env:CATALINA_BASE = $CatalinaHome

& (Join-Path $ZTomcatHome 'apply-config.ps1')

Write-Host "[ztomcat] Starting Tomcat on http://localhost:8080"
& (Join-Path $CatalinaHome 'bin\catalina.bat') start
