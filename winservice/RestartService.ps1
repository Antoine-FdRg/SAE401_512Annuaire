$here = Split-Path $MyInvocation.MyCommand.Path
Set-Location $here

Write-Output "[SERVEUR] Redemarage du service..."
.\backend-service.exe restart

if ($LastExitCode -ne 0) {
    Write-Error "[SERVEUR] Erreur lors du redemarrage du service (regardez plus haut)."
    exit 1
}
Write-Output "[SERVEUR] Service redemare !"
