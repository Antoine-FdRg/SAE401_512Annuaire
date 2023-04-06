Import-Module activedirectory
  

$ADOU = Import-csv ".\OU.csv" -Delimiter "," -Encoding UTF8
$Domain = "EQUIPE1B.local"

foreach ($OU in $ADOU) {
    if ((Get-ADOrganizationalUnit -Filter "Name -eq '$($OU.name)'")) {
        Write-Warning "The OU $($OU.name) already exist in Active Directory."
    }
    else {
        try {
            New-ADOrganizationalUnit `
                -Name $OU.name `
                -Path "OU=$($OU.parentOU),DC=$Domain,DC=local" `
                -Description $OU.description
            Write-Host "The OU $($OU.name) was created."
        }
        catch {
            Write-Error "The OU $($OU.name) was not created."
        }
    }
}


