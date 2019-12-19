import { Component, OnInit } from '@angular/core';
import { MenuController } from '@ionic/angular';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { AlertService } from 'src/app/services/alert.service';
import { NgForm } from '@angular/forms';

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
  constructor(private menu: MenuController, private authService: AuthService, private alertService: AlertService) {
    this.menu.enable(true);
  }

  ngOnInit() {

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
}
