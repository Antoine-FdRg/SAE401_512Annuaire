import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {LoginService} from "../../service/login.service";

@Component({
  selector: 'app-sudo-popup',
  templateUrl: './sudo-popup.component.html',
  styleUrls: ['./sudo-popup.component.css']
})
export class SudoPopupComponent {
  constructor(private loginService: LoginService) {
  }

  @Output() showPopup: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() alertPopup: EventEmitter<boolean> = new EventEmitter<boolean>();

  toggleShowPopup() {
    this.showPopup.emit(false);
  }

  showAlert() {
    this.alertPopup.emit(true);
  }

  @ViewChild("emailInput") emailInput: ElementRef | undefined;
  @ViewChild("passwordInput") passwordInput: ElementRef | undefined;

  handleSudoConnexion() {
    if (this.emailInput !== undefined && this.passwordInput !== undefined) {
      let email = this.emailInput.nativeElement.value;
      let password = this.passwordInput.nativeElement.value;
      this.loginService.loginConfirmationAndAdmin(email, password).then((value) => {
        if (value) {
          this.validateOperation();
        } else {
          this.showAlert();
        }
      });
    }
  }

  validateOperation() {

  }
}

