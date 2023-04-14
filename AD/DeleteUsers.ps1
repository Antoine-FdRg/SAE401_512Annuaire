
$Exclusions = @("Dummy Query")

$AllADUsers = Get-ADUser -Filter * -Properties * | Select-Object -Property Name, SamAccountName, DistinguishedName

$AllADUsers | ForEach-Object {
    if ($_.Name -notlike "*$($Exclusions)*") {
        Remove-ADUser -Identity $_.DistinguishedName -Confirm:$false
        Write-Host "The user $($_.Name) ($($_.SamAccountName)) was deleted."
    }
    else {
        Write-Host "The user $($_.Name) ($($_.SamAccountName)) was not deleted."
    }
}