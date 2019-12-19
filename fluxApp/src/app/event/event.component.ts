import { Component, OnInit , Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';
import { EnvService } from '../services/env.service';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss'],
})
export class EventComponent implements OnInit {

  @Input() eventType : any;
  @Input() area : Object;
  @Input() token: any;
  @Input() isSub: boolean = false;

  constructor(private http : HttpClient , private authService : AuthService , private env : EnvService) {

  }

  async isSubscribed() {
    let name : string = this.eventType.name;
    let mySubs = [];
    this.token = this.authService.storage.getItem('token');
    let email = this.token["__zone_symbol__value"]["email"];
    let requestAPI = this.env.PROXY_URL + this.env.API_URL + "subscription/my-subscriptions?email=" + email;
    this.http.get(requestAPI , {responseType : "text"}).subscribe(
      (response) => {
        mySubs = JSON.parse(response);
        console.log(mySubs);
        console.log(mySubs);
        for(let i=0 ; i < mySubs.length ; i++) {
          console.log("test");
          let sub = mySubs[i];
          let subName : string = sub.name;
          console.log("sub = " + sub);
          if(subName === name) {
          console.log(true);
          this.isSub = true;
          return true;
        }
      }
    console.log(mySubs.length);
    return false;
        
    }
    );
    return false;
    
  }

  subscribeToEvent() {
    console.log(this.isSubscribed())
    this.isSubscribed()
                      .then((value) => {
                          this.isSub = value;
                      },
                      (error) => {
                        console.log(error);
                      })
    let name = this.eventType.name;
    this.token = this.authService.storage.getItem('token');
    let email = this.token["__zone_symbol__value"]["email"];
    let requestAPI = this.env.PROXY_URL + this.env.API_URL + "subscription/subscribe?email=" + email + "&name=" + name;
    this.http.post(requestAPI , {email: email , name: name}, {responseType : "text"}).subscribe(
      (error) => {
        console.log('Erreur ! : ' + error);
        this.isSubscribed()
                      .then((value) => {
                          console.log(value);
                          this.isSub = value;
                      },
                      (error) => {
                        console.log(error);
                      });
      }
    )
    

  }

  unsubscribeToEvent() {
    console.log(this.isSubscribed())
    this.isSubscribed()
                      .then((value) => {
                          this.isSub = true;
                      },
                      (error) => {
                        console.log(error);
                      })
    let name = this.eventType.name;
    this.token = this.authService.storage.getItem('token');
    let email = this.token["__zone_symbol__value"]["email"];
    let requestAPI = this.env.PROXY_URL + this.env.API_URL + "subscription/unsubscribe?email=" + email + "&name=" + name;
    this.http.post(requestAPI , {email: email , name: name}, {responseType : "text"}).subscribe(
      (error) => {
        console.log('Erreur ! : ' + error);
        this.isSubscribed()
                      .then((value) => {
                          console.log(value);
                          this.isSub = false;
                      },
                      (error) => {
                        console.log(error);
                      });
      }
    )
    

  }

  ngOnInit() {
    
  }

}
