import { AfterContentInit, Component, ElementRef, ViewChild } from '@angular/core';
import mermaid from 'mermaid';
import * as Papa from 'papaparse';
import { HttpClient } from '@angular/common/http';
import { apiURL } from '../service/apiURL';

@Component({
  selector: 'app-organigramme',
  templateUrl: './organigramme.component.html',
  styleUrls: ['./organigramme.component.css']
})
export class OrganigrammeComponent {
  mermaidDiagram: any = "\n";

  constructor(private http: HttpClient) {
    this.loadCSV();
  }

  async parseCSV(csvText: string) {
    const results = Papa.parse(csvText, {
      complete: async function (results: any) {
        function recursive(filtered: any, responsable: string[], string: string) {
          string += responsable[3] + "[" + responsable[1] + "]\n"
          for (let current of filtered) {
            if (current[2] === responsable[0]) {
              string += current[3] + "[" + current[1] + "]\n"
              string += responsable[3] + "-->" + current[3] + "\n"
              string = recursive(filtered, current, string)
            }
          }
          return string
        }
        let filtered = []

        let i = 0
        for await (let row of results.data) {
          filtered.push([row[0], row[1] + " " + row[0], row[14], i])
          i++
        }
        filtered.shift()
        var schema = recursive(filtered, filtered[0], "flowchart TD\n");
        mermaid.init({ startOnLoad: false, securityLevel: 'loose' });
        const { svg, bindFunctions } = await mermaid.render('mermaidDiagram', schema);
        document.getElementById('test')!.innerHTML = svg;
        // put svg at 500% width
        document.getElementById('test')!.style.width = "500%";
      }
    });


  }

  loadCSV() {
    this.http.get(apiURL + "/admin/getCsvOfAllUsers", { responseType: 'text' }).subscribe(
      data => this.parseCSV(data),
      error => console.error(error)
    );
  }
}
