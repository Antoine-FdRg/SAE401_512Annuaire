import {Component, EventEmitter, Output, ViewChild} from '@angular/core';
import {AdminService} from "../../../service/admin.service";
import {FormControl} from '@angular/forms';
import {Person} from 'src/app/person';
import {SearchService} from 'src/app/service/search.service';

interface Secteur {
  name: string;
  department: string;
  dn: string;
}

@Component({
  selector: 'app-create-user-form',
  templateUrl: './create-user-form.component.html',
  styleUrls: ['./create-user-form.component.css']
})
export class CreateUserFormComponent {
  lastname: FormControl = new FormControl("");
  email: FormControl = new FormControl("");
  firstname: FormControl = new FormControl("");
  address: FormControl = new FormControl("");
  phone: FormControl = new FormControl("");
  title: FormControl = new FormControl("");
  dateOfBirth: FormControl = new FormControl("");
  manager: FormControl = new FormControl("");
  depSect: FormControl = new FormControl("");

  personList: Person[] = []

  secteurList: Secteur[] = []

  secteurListRaw: any[] = []


  constructor(private adminService: AdminService) {
    this.adminService.searchAll().subscribe((data) => {
      console.log(data);

      this.personList = data as [];
    });
    this.adminService.searchAllOU().subscribe((data) => {
      this.secteurListRaw = data as [];
      this.constructSecteurList();
    });
  }

  constructSecteurList() {
    if (this.secteurListRaw !== undefined) {
      for (let i = 0; i < this.secteurListRaw.length; i++) {
        let dn = this.secteurListRaw[i].dn;
        let name = dn.split(",")[0].split("=")[1];
        let department = dn.split(",")[1].split("=")[1];
        this.secteurList.push({name: name, department: department, dn: dn});
      }
    }
  }

  @Output() showCreatePopup: EventEmitter<boolean> = new EventEmitter<boolean>();

  ngOnInit(): void {
  }

  toggleShowCreatePopup() {
    this.showCreatePopup.emit(false);
  }


  onCreatePressed() {
    console.log(this.manager.value);

    if (this.lastname.value == "" || this.email.value == "" || this.firstname.value == "" || this.address.value == "" || this.phone.value == "" || this.title.value == "" || this.dateOfBirth.value == "" || this.manager.value == "") {
      alert("Veuillez remplir tous les champs");
      return;
    }
    console.log(this.depSect.value);
    this.adminService.createMember(this.firstname.value, this.lastname.value, this.depSect.value.toString(), this.title.value, this.phone.value, this.phone.value, this.dateOfBirth.value, this.address.value, this.manager.value).subscribe(
      (response) => {
        console.log(response);
      }
    );

  }

}
