import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiURL } from './apiURL';

@Injectable({
  providedIn: 'root'
})
export class AdminService {


  constructor(private http: HttpClient) { }

  public getMembers() {
    return this.http.get(apiURL + "/public/search/person", { params: { name: "a ", page: 0, perPage: 30 } });
  }


  getGroups() {
    return this.http.get(apiURL + "/admin/group/all");
  }

  getFilters(){
    return this.http.get(apiURL+"/admin/allFilters");
  }

  createGroup(name:String){
    return this.http.post(apiURL + "/admin/group/create",{cn:name});
  }

  getMembersOfGroup(name:string){
    return this.http.get(apiURL + "/admin/group/members/"+name);
  }

  deleteGroup(cn:String){
    console.log("delete group : " + cn);
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {
        cn: cn
      },
    };
    return this.http.delete(apiURL + "/admin/group/delete", options);
  }

  deleteUser(dn:string){
    console.log("delete user : " + dn);
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    };
    return this.http.delete(apiURL + "/admin/member/delete?dn="+dn, options);
  }

  createMember(firstName:string, lastName:string,structureDN:string, title:string, personalPhone:string, professionalPhone:string, dateOfBirth:string, address:string,managerDN:string){
    const options = {
      firstName: firstName,
      lastName: lastName,
      structureDN: structureDN,
      title: title,
      personalPhone: personalPhone,
      professionalPhone: professionalPhone,
      dateOfBirth: dateOfBirth,
      address: address,
      managerDN: managerDN
    }
    return this.http.post(apiURL + "/admin/member/create", options);
  }
  removeMemberFromGroup(cn:string, dnUser:string)
  {
    return this.http.delete(apiURL + "/admin/group/removeUser",{params:{
      groupCN:cn,
      dn:dnUser
    }})
  }
  searchAll()
  {
    return this.http.get(apiURL + "/public/search/person", { params: { name: "*", page: 0, perPage: 100 } })
  }
}
