$ErrorActionPreference = 'Stop'

$ZTomcatHome = Split-Path -Parent $MyInvocation.MyCommand.Path
$CatalinaHome = Join-Path $ZTomcatHome 'apache-tomcat-10.1.34'

if (-not (Test-Path (Join-Path $CatalinaHome 'bin\catalina.bat'))) {
    Write-Host '[ztomcat] Tomcat not found.'
    exit 1
}

$jdk21 = Join-Path $env:USERPROFILE '.jdks\temurin-21.0.4'
if (Test-Path $jdk21) {
    $env:JAVA_HOME = $jdk21
}

$env:CATALINA_HOME = $CatalinaHome
$env:CATALINA_BASE = $CatalinaHome

Write-Host '[ztomcat] Stopping Tomcat...'
& (Join-Path $CatalinaHome 'bin\catalina.bat') stop
