import { Component, OnInit } from '@angular/core';
import { MenuController } from '@ionic/angular';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page implements OnInit {

  token:any ;

  constructor(private menu: MenuController, private authService: AuthService) {
    this.menu.enable(true);
  }

  ngOnInit() {

  }

  ionViewWillEnter() {
    this.token = this.authService.storage.getItem('token');
    console.log(this.token);
    /*

    this.authService.user().subscribe(
      user => {
        this.user = user;
      }
    );*/
  }

}
