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

  constructor(private loginService: LoginService, private router: Router) {
    this.status = Status.disconnected;
    if (this.loginService.getUser() == undefined) {
      return
    }
    this.name = this.loginService.getUser()!.firstName;
    this.surname = this.loginService.getUser()!.lastName;
    if (this.name != undefined && this.surname != undefined)

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

    if (target.id !== "connexionButton" && !target.classList.contains("profile-picture") && target.id !== "infoMenu") {
      this.visibility["infoMenu"] = false;
      this.menuShown = false
    }
  }

  ngAfterViewChecked(): void {
    if (this.loginService.getUser() == undefined || this.loginService.getUser() == null) {
      this.status = Status.disconnected;


      this.visibility["connexion"] = true;
    }
    else {
      this.status = Status.connected;
      this.visibility["profilePicture"] = true;
      this.visibility["connexion"] = false;
      // this.visibility["infoMenu"] = true;
      this.name = this.loginService.getUser()!.firstName;
      this.surname = this.loginService.getUser()!.lastName;
      if (this.name != undefined && this.surname != undefined)

        this.initials = this.name.charAt(0) + this.surname.charAt(0);
    }
  }

  connexionClicked() {
    console.log(this.status);

    if (this.status == 1) {
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
  logout() {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

}


