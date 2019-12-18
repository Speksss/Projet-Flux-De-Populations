import { Injectable } from '@angular/core';
@Injectable({
  providedIn: 'root'
})
export class EnvService {
  API_URL = 'http://35.206.157.216:8080/';
  PROXY_URL = 'https://cors-anywhere.herokuapp.com/';
  constructor() { }
}
