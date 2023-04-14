Import-Module ActiveDirectory
# Création des groupes de la Direction
New-ADGroup -Name "Direction Generale" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Direction Generale Adj" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Presidence" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secretaire General" -GroupScope Global -GroupCategory Security

# Création des groupes du département Administratif et juridique
New-ADGroup -Name "Direction AJ" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Administratif" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Juridique" -GroupScope Global -GroupCategory Security

# Création des groupes du département Commercial
New-ADGroup -Name "Direction Commerciale" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Marketing" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Produit" -GroupScope Global -GroupCategory Security

# Création des groupes du département Finance
New-ADGroup -Name "Direction Financiere" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Comptabilite" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Paie" -GroupScope Global -GroupCategory Security

# Création des groupes du département Ressources Humaines
New-ADGroup -Name "Direction RH" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Gestion du Personnel" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Recrutement" -GroupScope Global -GroupCategory Security

# Création des groupes du département Technique
New-ADGroup -Name "Direction Technique" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur Logistique" -GroupScope Global -GroupCategory Security
New-ADGroup -Name "Secteur R&D" -GroupScope Global -GroupCategory Security
