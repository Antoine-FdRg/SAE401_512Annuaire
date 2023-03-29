Write-Output "[SERVEUR] Redémarage du service..."
./backend-service.exe restart

if ($LastExitCode -ne 0) {
    Write-Error "[SERVEUR] Erreur lors du redémarrage du service (regardez plus haut)."
    exit 1
}
Write-Output "[SERVEUR] Service redémaré !"
