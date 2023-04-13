import { AfterContentInit, Component, ElementRef, ViewChild } from '@angular/core';
import * as mermaid from "mermaid";
import { PassThrough } from "stream";
import { parse } from "csv-parse";
import {fs} from "fs";

@Component({
  selector: 'app-organigramme',
  templateUrl: './organigramme.component.html',
  styleUrls: ['./organigramme.component.css']
})
export class OrganigrammeComponent {
  @ViewChild('mermaid')
  public mermaid: any;
  constructor()
  {
    const csvToMermaid = async (csvData:any) => {
      let stream = new PassThrough()
      stream.write(csvData)
      stream.end()

      let parser = stream.pipe(parse())

      let filtered = []

      let i = 0
      // filtrage
      for await (let row of parser) {
          filtered.push([row[0], row[1] + " " + row[0], row[14],i])
          i++
      }

      // On supprime le header du csv
      filtered.shift()
      // console.log(filtered)
      // contruction de l'arbre



      return this.recursive(filtered,filtered[0],"flowchart TD\n");
  }
  const createPerson = (row:any) => {
    return {
        "name": row[0],
        "fullName": row[1],
        "sub": []
    }
}
let csvData = fs.readFileSync("Users.csv", "utf-8")
// console.log(csvData)
csvToMermaid(csvData).then((res) => {
    console.log(res)
});

  }
  recursive(filtered:any,responsable:any,string:any) {
    string +=responsable[3] + "[" + responsable[1] + "]\n"
    for (let current of filtered) {
        if (current[2] === responsable[0]) {
            // console.log(string)
            string += current[3] + "[" + current[1] + "]\n"
            string += responsable[3] + "-->" + current[3] + "\n"
            string = this.recursive(filtered,current,string)
        }
    }
    return string
}

}
