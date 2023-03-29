$ErrorActionPreference = "Stop"

if (!(Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Error "Java n'est pas detecté, impossible de build le backend."
    exit 1
}

if (!(Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Error "Maven n'est pas detecté, impossible de build le backend."
    exit 1
}

# Rebuild du projet
Write-Output "Build du backend..."
Set-Location backend
mvn clean
mvn package -DskipTests
Set-Location ..

Move-Item backend\target\backend-0.0.1-SNAPSHOT.jar winservice\backend.jar -Force

Write-Output "Fini !"
Write-Output ""

# Copie des fichiers sur la machine
Write-Output "Copie des fichiers sur la machine..."
Write-Output "Entrez le mot de passe du serveur :"
scp.exe -v -r winservice "administrateur@10.22.32.2:C:\Users\Administrateur\Desktop\backend\"
Write-Output ""

# Redemarage du service
Write-Output "Redemarage du service sur le serveur..."
Write-Output "Entrez le mot de passe du serveur (c'est la derniere fois) :"
ssh.exe administrateur@10.22.32.2 powershell "C:\Users\Administrateur\Desktop\backend\winservice\RestartService.ps1"

Write-Output "Fini ! Tout devrait etre bon."
