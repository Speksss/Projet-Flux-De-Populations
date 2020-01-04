import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EnvService } from '../services/env.service';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';
import { Platform, NavController } from '@ionic/angular';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';

@Component({
  selector: 'app-tab2',
  templateUrl: 'tab2.page.html',
  styleUrls: ['tab2.page.scss']
})

export class Tab2Page {
  events : Array<any>;
  token: any
  pass: any
  constructor(private platform: Platform,
  private splashScreen: SplashScreen,
  private statusBar: StatusBar,
  private authService: AuthService,
  private navCtrl: NavController,
  private http : HttpClient , private env : EnvService, private alertService: AlertService) {
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
    this.initializeApp();
  }

  initializeApp() {
    this.platform.ready().then(() => {
      this.statusBar.styleDefault();
      // Commenting splashScreen Hide, so it won't hide splashScreen before auth check
      //this.splashScreen.hide();
      this.token = this.authService.storage.getItem('token');
      console.log("Token dans tab 2");
      console.log(this.token);
      this.pass = this.authService.storage.getItem('password');
      console.log("Password dans tab 2");
      console.log(this.pass);
    });
  }

  // When logout button is pressed
  logout() {
    console.log("inside logout");
    console.log("Email :");
    console.log(this.token["__zone_symbol__value"]["email"]);
    console.log("Password :");
    console.log(this.pass["__zone_symbol__value"]);
    this.authService.logout(this.token["__zone_symbol__value"]["email"], this.pass["__zone_symbol__value"]).subscribe(
      data => {
        this.alertService.presentToast(data['message']);
      },
      error => {
        console.log("Error");
        console.log(error);
      },
      () => {
        this.navCtrl.navigateRoot('/welcome');
      }
    );
  }

}
