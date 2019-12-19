import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EnvService } from '../services/env.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-tab2',
  templateUrl: 'tab2.page.html',
  styleUrls: ['tab2.page.scss']
})

export class Tab2Page {
  events : Array<any>;

  constructor(private http : HttpClient , private env : EnvService) {
    let requestAPI = env.PROXY_URL + env.API_URL + "event/all";
    http.get(requestAPI , {responseType : "text"}).subscribe(
      (response) => {
        this.events = JSON.parse(response);
        console.log(this.events)
      },
      (error) => {
        console.log(error);
      }
    );

  }

}
