import { FormControl } from '@angular/forms';
import { Component } from '@angular/core';

@Component({
  selector: 'app-add-group',
  templateUrl: './add-group.component.html',
  styleUrls: ['./add-group.component.css']
})
export class AddGroupComponent {
  form:FormControl=new FormControl('');

  constructor() { }

  createGroup(){
    console.log("createGroup() called");

  }

  cancel(){
    console.log("cancel() called");
  }
}
