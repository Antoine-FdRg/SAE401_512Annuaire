import {Component, EventEmitter, Output, ViewChild} from '@angular/core';
import {AdminService} from "../../../service/admin.service";

@Component({
  selector: 'app-create-user-form',
  templateUrl: './create-user-form.component.html',
  styleUrls: ['./create-user-form.component.css']
})
export class CreateUserFormComponent {

  constructor(private adminService: AdminService) { }
  @Output() showCreatePopup: EventEmitter<boolean> = new EventEmitter<boolean>();
  ngOnInit(): void {
  }

  toggleShowCreatePopup() {
    this.showCreatePopup.emit(false);
  }

  @ViewChild("name") name: string | undefined;
  @ViewChild("email") email: string | undefined;
  @ViewChild("firstname") firstname: string | undefined;
  @ViewChild("addresse") addressed: string | undefined;
  @ViewChild("telephone") telephone: string | undefined;
  @ViewChild("login") login: string | undefined;

  onCreatePressed() {
    if (this.name === undefined || this.email === undefined || this.firstname === undefined || this.addressed === undefined || this.telephone === undefined || this.login === undefined) {
      return;
    }
    let name = this.name;
    let email = this.email;
    let firstname = this.firstname;
    let addresse = this.addressed;
    let telephone = this.telephone;
    let login = this.login;

    this.adminService.createMember(firstname,name,"", "",telephone,"","",addresse,"").subscribe(
      (response) => {
        console.log(response);
      }
    );

  }

}
