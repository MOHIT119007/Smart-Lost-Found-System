$ErrorActionPreference = "Stop"
$jdkDir = "C:\Users\Lenovo\.jdk17"
$zipPath = "$env:TEMP\jdk17.zip"
$extractTemp = "$env:TEMP\jdk_extract"

if (Test-Path "$jdkDir\bin\javac.exe") {
    Write-Output "JDK 17 already installed at $jdkDir"
    exit 0
}

Write-Output "Starting JDK 17 download from Adoptium..."
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
$url = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.20.1%2B1/OpenJDK17U-jdk_x64_windows_hotspot_17.0.20.1_1.zip"

$webClient = New-Object System.Net.WebClient
$webClient.DownloadFile($url, $zipPath)
Write-Output "Downloaded to $zipPath. Extracting..."

if (Test-Path $extractTemp) {
    Remove-Item -Path $extractTemp -Recurse -Force
}
Expand-Archive -Path $zipPath -DestinationPath $extractTemp -Force

$subDir = (Get-ChildItem -Path $extractTemp -Directory)[0].FullName
if (-not (Test-Path $jdkDir)) {
    New-Item -ItemType Directory -Path $jdkDir -Force | Out-Null
}

Copy-Item -Path "$subDir\*" -Destination $jdkDir -Recurse -Force
Remove-Item -Path $extractTemp -Recurse -Force
Remove-Item -Path $zipPath -Force

Write-Output "JDK 17 setup complete at $jdkDir"
& "$jdkDir\bin\javac.exe" -version
