import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-sudo-popup',
  templateUrl: './sudo-popup.component.html',
  styleUrls: ['./sudo-popup.component.css']
})
export class SudoPopupComponent {
  constructor() {

  }
  @Output() showPopup: EventEmitter<boolean> = new EventEmitter<boolean>();
  toggleShowPopup() {
    this.showPopup.emit(false);
  }

  handleSudoConnexion() {

  }

  validateOperation() {

  }
}

