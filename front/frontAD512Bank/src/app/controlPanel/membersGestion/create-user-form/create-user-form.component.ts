import {Component, EventEmitter, Output, ViewChild} from '@angular/core';
import {AdminService} from "../../../service/admin.service";
import { FormControl } from '@angular/forms';
import { Person } from 'src/app/person';
import { SearchService } from 'src/app/service/search.service';

@Component({
  selector: 'app-create-user-form',
  templateUrl: './create-user-form.component.html',
  styleUrls: ['./create-user-form.component.css']
})
export class CreateUserFormComponent {
lastname :FormControl= new FormControl("");
email :FormControl= new FormControl("");
firstname :FormControl= new FormControl("");
address :FormControl= new FormControl("");
phone :FormControl= new FormControl("");
title :FormControl= new FormControl("");
dateOfBirth :FormControl= new FormControl("");
manager :FormControl= new FormControl("");

personList:Person[] = []


  constructor(private adminService: AdminService) {
    this.adminService.searchAll().subscribe((data)=>{
      console.log(data);

      this.personList = data as [];
    });
  }

  @Output() showCreatePopup: EventEmitter<boolean> = new EventEmitter<boolean>();
  ngOnInit(): void {
  }

  toggleShowCreatePopup() {
    this.showCreatePopup.emit(false);
  }


  onCreatePressed() {
    console.log(this.manager.value);

    if(this.lastname.value == "" || this.email.value == "" || this.firstname.value == "" || this.address.value == "" || this.phone.value == "" || this.title.value == "" || this.dateOfBirth.value == "" || this.manager.value == ""){
      alert("Veuillez remplir tous les champs");
      return;
    }

    this.adminService.createMember(this.firstname.value,this.lastname.value,"OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local", this.title.value,this.phone.value,this.phone.value,this.dateOfBirth.value,this.address.value,this.manager.value).subscribe(
       (response) => {
         console.log(response);
       }
     );

  }

}
