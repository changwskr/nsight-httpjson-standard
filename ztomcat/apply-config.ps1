$ErrorActionPreference = 'Stop'

$ZTomcatHome = Split-Path -Parent $MyInvocation.MyCommand.Path
$CatalinaHome = Join-Path $ZTomcatHome 'apache-tomcat-10.1.34'
$ServerXml = Join-Path $CatalinaHome 'conf\server.xml'
$SetenvSrc = Join-Path $ZTomcatHome 'conf\setenv.bat'
$SetenvDst = Join-Path $CatalinaHome 'bin\setenv.bat'

if (-not (Test-Path $ServerXml)) {
    Write-Host '[ztomcat] server.xml not found. Run install-tomcat.bat first.'
    exit 1
}

Copy-Item -Path $SetenvSrc -Destination $SetenvDst -Force

$content = Get-Content -Path $ServerXml -Raw -Encoding UTF8
if ($content -notmatch 'URIEncoding="UTF-8"') {
    $content = $content -replace '(<Connector port="8080" protocol="HTTP/1\.1"\s*)', '$1URIEncoding="UTF-8" useBodyEncodingForURI="true" '
    Set-Content -Path $ServerXml -Value $content -Encoding UTF8 -NoNewline
    Write-Host '[ztomcat] Applied UTF-8 Connector settings to server.xml'
}

Write-Host '[ztomcat] Encoding config applied.'
