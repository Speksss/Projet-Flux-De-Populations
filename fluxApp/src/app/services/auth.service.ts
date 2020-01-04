import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import { NativeStorage } from '@ionic-native/native-storage/ngx';
import { EnvService } from './env.service';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLoggedIn = false;
  token:any;
  password: any;

  constructor(
    private http: HttpClient,
    public storage: NativeStorage,
    private env: EnvService,
  ) { }
  login(email: String, password: String) {
    return this.http.get<User>(this.env.PROXY_URL + this.env.API_URL + 'login?email=' + email + '&password=' + password

    /*,
      {email: email, password: password} ,*//*
      {responseType: 'text'}*/
    ).pipe(
      tap(
        token => {
        this.storage.setItem('password', password);
        this.storage.setItem('token', token)
        .then(
          () => {
            console.log(token);
            console.log('Token Stored');
          },
          error => console.error('Error storing item', error)
        );
        this.token = token;
        console.log("Password :");
        console.log(password);
        this.password = password;
        this.token["password"] = password;
        this.isLoggedIn = true;
        return token;
      },
      user => {

      }),
    );
  }

  register(email: String, nom: String, prenom: String, password: String) {
    console.log(this.env.PROXY_URL + this.env.API_URL + 'register?email=' + email + '&lastName=' + nom +
      '&firstName=' + prenom + '&password=' + password);

    return this.http.post(this.env.PROXY_URL + this.env.API_URL + 'register?email=' + email + '&lastName=' + nom +
      '&firstName=' + prenom + '&password=' + password ,

     {email: email, nom: nom, prenom: prenom, password: password},
     {responseType: "text" }
    )
  }

  update(email: String, requete: String) {
    return this.http.post(this.env.PROXY_URL + this.env.API_URL + 'message/new?emailTransmitter=' + email + '&messageBody=' + requete,
    {emailTransmitter: email, messageBody: requete},
    {responseType: "text" }
  )
  }

  logout(email: String, password: String) {
    const headers = new HttpHeaders({
      'Authorization' : this.token["token_type"]+" "+this.token["access_token"]
    });
    return this.http.get(this.env.PROXY_URL + this.env.API_URL  + 'login?email=' + email + '&password=' + password, { headers: headers })
    .pipe(
      tap(data => {
        this.storage.remove("token");
        this.isLoggedIn = false;
        delete this.token;
        return data;
      })
    )
  }

  user() {
    const headers = new HttpHeaders({
      'Authorization': this.token["token_type"]+" "+this.token["access_token"]
    });
    return this.http.get<User>(this.env.PROXY_URL + this.env.API_URL  + 'login?email=' + this.token["email"] + '&password=' + this.token["password"] , { headers: headers })
    .pipe(
      tap(user => {
        return user;
      })
    )
  }

  getToken() {
    return this.storage.getItem('token').then(
      data => {
        this.token = data;
        if(this.token != null) {
          this.isLoggedIn=true;
        } else {
          this.isLoggedIn=false;
        }
      },
      error => {
        this.token = null;
        this.isLoggedIn=false;
      }
    );
  }
}
