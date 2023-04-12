import { FormControl } from '@angular/forms';
import { Component } from '@angular/core';
import { AdminService } from 'src/app/service/admin.service';
import { HtmlParser } from '@angular/compiler';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-group',
  templateUrl: './add-group.component.html',
  styleUrls: ['./add-group.component.css']
})
export class AddGroupComponent {
  form:FormControl=new FormControl('');

  constructor(private adminService: AdminService) { }

  createGroup(){
    console.log("createGroup() called");
    console.log(this.form.value);
    this.adminService.createGroup(this.form.value).subscribe((response) => {
      console.log(response);
    },
    (error:HttpErrorResponse) => {
      if(error.status==200){
        this.popup("Le groupe a bien été créé.");
      }else if(error.status==409){
        this.popup("Un groupe avec ce nom existe déjà.");
      }else{
        this.popup("Une erreur est survenue lors de la création du groupe.");
      }
    });
    this.form.setValue('');
  }

  cancel(){
    console.log("cancel() called");
  }

  popup(msg:String){
    //TODO : replace with a better popup
    window.alert(msg);
  }
}
