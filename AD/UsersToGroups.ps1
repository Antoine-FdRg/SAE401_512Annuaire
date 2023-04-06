Import-Module ActiveDirectory
# Set the path to the CSV file
$csvPath = ".\groups.csv"

# Import the CSV file
$users = Import-Csv $csvPath
#The CSV as a name, surname and a group column name is the User LastName and surname is the first name

# Loop through each user in the CSV file
foreach ($user in $users) {
    # Get the user's name
    $name = $user.name
    $surname = $user.surname
    # Get the user's group
    $group = $user.group

    Write-Host "Getting user $name $surname"

    $fullname = $surname + " " + $name

    # Get the user's object
    $user = Get-ADUser -Filter {Name -eq $fullname}


    # Add the user to the group
    Add-ADGroupMember -Identity $group -Members $user
}

# Display a message when the script is complete
Write-Host "All users have been added to their respective groups."
