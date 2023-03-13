import { Component } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  status = ""
  imageLink = ""
  name = "jean"
  surname= "paul"

  visibility = {
    "profilePicture" : false,
    "connexion" : true,
    "infoMenu" : false
  }
}
