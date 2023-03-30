$ErrorActionPreference = "Stop"

# On ve dans le repertoire du service qui tourne
$here = Split-Path $MyInvocation.MyCommand.Path
Set-Location $here

# On stop completement le service
Write-Output "[SERVEUR] Arret du service..."
.\backend-service.exe stop
Write-Output ""

Write-Output "[SERVEUR] Desinstallation du service..."
.\backend-service.exe uninstall
Write-Output ""

# On supprime les fichiers
Write-Output "[SERVEUR] Suppression des anciens fichiers..."
Remove-Item * -Force -Recurse
Write-Output ""

Set-Location ..

# On copie les nouveaux fichiers
Write-Output "[SERVEUR] Copie des nouveaux fichiers..."
Copy-Item "winservice_temp\*" "winservice" -Recurse
Write-Output ""

# On supprime les fichiers temporaires
Write-Output "[SERVEUR] Suppression des fichiers temporaires..."
Remove-Item "winservice_temp" -Force -Recurse
Write-Output ""

Set-Location "winservice"

# On peut créer et lancer le service
Write-Output "[SERVEUR] Installation du service..."
.\backend-service.exe install
Write-Output ""

Write-Output "[SERVEUR] Demarage du service..."
.\backend-service.exe start
Write-Output ""
