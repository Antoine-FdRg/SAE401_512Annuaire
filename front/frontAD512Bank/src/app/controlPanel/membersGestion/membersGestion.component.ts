import {Person} from './../../person';
import {AdminService} from './../../service/admin.service';
import {Component, OnInit, ViewChild} from '@angular/core';
import {SudoPopupComponent} from "../sudo-popup/sudo-popup.component";

@Component({
  selector: 'app-membersGestion',
  templateUrl: './membersGestion.component.html',
  styleUrls: ['./membersGestion.component.css']
})
export class MembersGestionComponent implements OnInit {
  @ViewChild(SudoPopupComponent) child: any;

  listMembers: Person[] = [];
  showSudoPopup: boolean = false;
  showCreatePopup: boolean = false;
  showAlertPopup: boolean = false;

  dnOfCurrentSelectedItem: string = "";

  constructor(private adminService: AdminService) {
    this.adminService.getMembers().subscribe(
      (response) => {
        this.listMembers = response as Person[];
      }
    );
    this.listMembers.push(
      {
        "firstName": "Raphael",
        "lastName": "Caldwell",
        "email": "test",
        "login": "test",
        "title": "Directeur Commercial",
      }
    )
    console.log('====================================');
    console.log(this.listMembers);
    console.log('====================================');
  }

  ngOnInit() {

  }

  toggleShowSudoPopup() {
    this.showSudoPopup = !this.showSudoPopup;
  }

  toggleShowAlert() {
    this.showAlertPopup = !this.showAlertPopup;
  }

  toggleShowCreatePopup() {
    this.showCreatePopup = !this.showCreatePopup;
  }

  openPopUp(option: string, itemDN: string) {
    this.dnOfCurrentSelectedItem = itemDN;
    if (option === "create") {
      this.toggleShowCreatePopup();
    } else if (option === "delete") {
      this.toggleShowSudoPopup();
    }
  }

  afterValidateOperation() {
    this.adminService.deleteUser(this.dnOfCurrentSelectedItem).subscribe(
      (response) => {
        console.log(response);
        this.toggleShowSudoPopup()
      });
  }
}
