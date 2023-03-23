import { Router, RouterModule } from '@angular/router';
import { LoginService } from './../../service/login.service';
import { AfterViewInit, Component, OnInit, ViewChild, ElementRef, HostListener, ApplicationRef, Injector } from '@angular/core';

enum Status {
  connected,
  disconnected
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  status = Status.disconnected;
  menuShown = false;

  name = "Jean";
  surname = "Paul";
  initials: string = "";

  constructor(loginService: LoginService, private router: Router) {
    this.status = Status.disconnected;
    this.name = loginService.userBase.name;
    this.surname = loginService.userBase.surname;
    this.initials = this.name.charAt(0) + this.surname.charAt(0);
  }


  visibility = {
    "profilePicture": false,
    "connexion": true,
    "infoMenu": false
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    let target: HTMLElement = event.target as HTMLElement;
    if (target.id !== "connexionButton") {
      this.visibility["infoMenu"] = false;
      this.menuShown = false;
    }
  }

  connexionClicked() {

    if (this.status == 1) {
      // go to login page
      console.log("go to login page");

      this.router.navigate(['/login']);
    }
    else {

      if (this.menuShown) {
        this.visibility["infoMenu"] = false;
        this.menuShown = false;
        return;
      }
      this.menuShown = true;
      this.visibility["infoMenu"] = true;
    }

  }
}

