import { Component } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-tabs',
  templateUrl: 'tabs.page.html',
  styleUrls: ['tabs.page.scss']
})
export class TabsPage {

  constructor(private authService: AuthService, private navCtrl: NavController) {}

/*  logOut() {
    this.authService.logout();
    this.navCtrl.navigateRoot('/welcome');
  }*/

}
