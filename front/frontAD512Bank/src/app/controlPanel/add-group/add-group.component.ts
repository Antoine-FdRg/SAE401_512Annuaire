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
    if(this.form.value == ''){
      return this.popup(false, "Le nom du groupe ne peut pas être vide.");
    }
    console.log("createGroup() called");
    console.log(this.form.value);
    this.adminService.createGroup(this.form.value).subscribe((response) => {
      console.log("succes"+response);
    },
    (error:HttpErrorResponse) => {
      let success:boolean = error.status == 200 || error.status == 201;
      this.popup(success, success?error.message:error.error.error);
    });
    this.form.setValue('');
  }

  cancel(){
    console.log("cancel() called");
  }

  popup(sucess:boolean, msg: string) {
    let popup:HTMLElement;
    let content:HTMLElement
    if(sucess){
      popup = document.getElementById("popupSuccess")!;
      content= document.getElementById("popupSuccessContent")!;
    }else{
      popup = document.getElementById("popupError")!;
      content = document.getElementById("popupErrorContent")!;
    }
    content.innerHTML = msg;
    popup.style.display = "flex";
    setTimeout(() => {
      popup.classList.add("fadeout");
    }, 1500);
    setTimeout(() => {
      popup.classList.remove("fadeout");
      popup.style.display = "none";
    },3000);
  }
}
