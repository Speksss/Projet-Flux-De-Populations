import { Component, OnInit } from '@angular/core';
import { MenuController } from '@ionic/angular';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { AlertService } from 'src/app/services/alert.service';
import { NgForm } from '@angular/forms';
import { Platform, NavController } from '@ionic/angular';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';


@Component({
  selector: 'app-tab4',
  templateUrl: 'tab4.page.html',
  styleUrls: ['tab4.page.scss']
})
export class Tab4Page  implements OnInit {

  user: User;
  newEmail: String;
  newLastName: String;
  newFirstName: String;
  token: any;
  pass: any;
  constructor(private platform: Platform,
  private splashScreen: SplashScreen,
  private statusBar: StatusBar,
  private navCtrl: NavController,private menu: MenuController, private authService: AuthService, private alertService: AlertService) {
    this.menu.enable(true);
    this.initializeApp();
  }

  ngOnInit() {

  }

  initializeApp() {
    this.platform.ready().then(() => {
      this.statusBar.styleDefault();
      // Commenting splashScreen Hide, so it won't hide splashScreen before auth check
      //this.splashScreen.hide();
      this.token = this.authService.storage.getItem('token');
      this.pass = this.authService.storage.getItem('password');
    });
  }

  ionViewWillEnter() {
    this.token = this.authService.storage.getItem('token');
    this.newEmail = this.token["__zone_symbol__value"]["email"];
    this.newLastName = this.token["__zone_symbol__value"]["lastName"];
    this.newFirstName = this.token["__zone_symbol__value"]["firstName"];
    console.log(this.newEmail);
    console.log(this.token);
  }

  changement(form: NgForm) {
    console.log(form.value.email);
    console.log(form.value.requete);
    this.authService.update(form.value.email, form.value.requete).subscribe(
      data => {
        console.log(data);
        this.alertService.presentToast(data['message']);
      },
      error => {
        console.log(error);
      },
      () => {

      }
    );
/*    console.log(form.value.prenom);
    console.log(form.value.password);

    this.authService.update(form.value.email, form.value.requete).subscribe(
        data => {
        //  this.alertService.presentToast(data['message']);
        },
        error => {
          console.log(error);
        },
        () => {

        }

      );*/
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
