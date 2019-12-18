import { Component, OnInit } from '@angular/core';
import { ModalController, NavController } from '@ionic/angular';
import { SignupPage } from '../signup/signup.page';
import { NgForm } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';
import { AppRoutingModule } from '../../app-routing.module';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage implements OnInit {

  modalB: boolean = false;

  constructor(
    private modalController: ModalController,
    private authService: AuthService,
    private navCtrl: NavController,
    private alertService: AlertService
  ) { }

  ngOnInit() {
  }

  //Dismmiss Login Modal
  dismissLogin() {
    if(this.modalB == true){
    this.modalController.dismiss();
    this.modalB=false;
  }
}

  // On register button tap, dissmiss login modal and open register modal
  async registerModal() {
    this.dismissLogin();
    const registerModal = await this.modalController.create({
      component: SignupPage
    });
    this.modalB=true;
    return await registerModal.present();
  }

  login(form: NgForm) {
    this.authService.login(form.value.email, form.value.password).subscribe(
      data => {
        console.log(data);
        this.alertService.presentToast("Logged in");
      },
      error => {
        console.log(error);
      },
      () => {
        this.dismissLogin();
        this.navCtrl.navigateRoot('/tabs');
      }
    );
  }

}
