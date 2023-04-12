import { Person } from './../../person';
import { AdminService } from './../../service/admin.service';
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
  showPopup: boolean = false;
  showCreatePopup: boolean = false;
  constructor(private adminService: AdminService) {
    this.adminService.getMembers().subscribe(
      (response) => {
        this.listMembers = response as Person[];
        console.log(this.listMembers);
      }
    );
    this.listMembers.push(
      {
        "firstName": "Raphael",
        "lastName": "Caldwell",
        "email": "test",
        "login": "test",
        "position": "Directeur Commercial",
      }
    )
    console.log('====================================');
    console.log(this.listMembers);
    console.log('====================================');

  }

  ngOnInit() {

  }

  toggleShowPopup() {
    this.showPopup = !this.showPopup;
  }

  toggleShowCreatePopup() {
    this.showCreatePopup = !this.showCreatePopup;
  }
}
