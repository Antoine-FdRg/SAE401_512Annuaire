$ErrorActionPreference = "Stop"

if (!(Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Error "Java n'est pas detecte, impossible de build le backend."
    exit 1
}

if (!(Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Error "Maven n'est pas detecte, impossible de build le backend."
    exit 1
}

$BuildFrontend = 0
if (!(Get-Command npm -ErrorAction SilentlyContinue)) {
    Write-Warning "npm n'est pas detecte, le frontend ne va pas être build."
} else {
    $BuildFrontend = 1
}

# Build du frontend
if ($BuildFrontend) {
    Write-Output "Build du frontend..."
    Set-Location ".\front\frontAD512Bank"

    if (!(Test-Path "node_modules")) {
        Write-Output "Le dossier node_modules n'est pas detecte, installation des dependances..."
        npm i
        Write-Output ""
    }

    npm run build
    Write-Output ""

    Write-Output "Copie des fichiers du frontend..."
    Set-Location "..\.."
    Copy-Item ".\front\frontAD512Bank\dist\front-ad512-bank\*" ".\backend\src\main\resources\public" -Recurse -Force
    Write-Output ""

    Write-Output "Fini !"
    Write-Output ""
}

# Build du backend
Write-Output "Build du backend..."
Set-Location "backend"
mvn clean
mvn package -DskipTests
Set-Location ..

Move-Item "backend\target\backend-0.0.1-SNAPSHOT.jar" "winservice\backend.jar" -Force

Write-Output "Fini !"
Write-Output ""

# Copie des fichiers sur la machine
Write-Output "Copie des fichiers sur la machine..."
Write-Output "Entrez le mot de passe du serveur :"
scp.exe -r ".\winservice\" "administrateur@10.22.32.2:C:\Users\Administrateur\Desktop\backend\winservice_temp"
Write-Output ""

# Redemarage du service
Write-Output "Redemarage du service sur le serveur..."
Write-Output "Entrez le mot de passe du serveur (c'est la derniere fois) :"
ssh.exe administrateur@10.22.32.2 powershell "C:\Users\Administrateur\Desktop\backend\winservice\RestartService.ps1"

Write-Output "Fini ! Tout devrait etre bon."
