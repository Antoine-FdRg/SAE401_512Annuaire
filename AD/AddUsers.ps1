Import-Module activedirectory
  
$ADUsers = Import-csv ".\Users.csv" -Delimiter "," -Encoding UTF8
$Domain = "EQUIPE1B.local"

foreach ($User in $ADUsers) {

    $FullName = "$($User.firstname) $($User.lastname)"
    $Upn = "$($User.username)@$Domain"

    if ((Get-AdUser -Filter "SamAccountName -eq '$($User.username)'")) {
        Write-Warning "A user account with username $($User.username) already exist in Active Directory."
    }
    elseif (([string]::IsNullOrEmpty($User.password))) {
        Write-Warning "The password for $($User.username) is null or empty."
    }
    elseif (($User.username).Length -gt 19) {
        Write-Warning "The username $($User.username) is too long (Greater than 20)."
    }
    else {

        $Manager = Get-ADUser -Filter "Name -eq '$($User.Responsable)'"

        try {
            New-ADUser `
                -SamAccountName $User.username `
                -UserPrincipalName $Upn `
                -GivenName $User.firstname `
                -Surname $User.lastname `
                -Name $FullName `
                -DisplayName $FullName `
                -Path $User.ou `
                -Company $User.company `
                -State $User.state `
                -City $User.city `
                -StreetAddress $User.streetaddress `
                -OfficePhone $User.telephone_pro `
                -MobilePhone $User.telephone_perso `
                -EmailAddress $User.email `
                -Title $User.jobtitle `
                -Department $User.department `
                -Manager $Manager.DistinguishedName `
                -AccountPassword (convertto-securestring $User.password -AsPlainText -Force) `
                -Enabled $True `
                -ChangePasswordAtLogon $False `
                -PasswordNeverExpires $True `
                -CannotChangePassword $False
            Write-Host "The user $($User.firstname) $($User.lastname) ($($User.username)) was created."
        }
        catch {
            Write-Error "The user $($User.firstname) $($User.lastname) ($($User.username)) was not created."
        }
    }
}
