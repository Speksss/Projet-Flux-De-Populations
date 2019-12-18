import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EnvService } from './env.service';

@Injectable()
export class EventService {

    arrayEvents : Array<any>;

    constructor(private http : HttpClient , private env : EnvService) {
        let requestAPI = env.PROXY_URL + env.API_URL + "event/all"
        http.get(requestAPI , {responseType : "text"}).subscribe(
            (response) => {
                this.arrayEvents= JSON.parse(response);
                console.log(this.arrayEvents)
            },
            (error) => {
                console.log('Erreur ! : ' + error);
            }
    );

  }

  getEventById(_id : number) {
    const event = this.arrayEvents.find(
        (s) => {
          return s.id === _id;
        }
      );
      return event;
  }
}
