import { Component, OnInit } from '@angular/core';
import { ModalController, NavController } from '@ionic/angular';
import { LoginPage } from '../login/login.page';
import { AuthService } from '../../services/auth.service';
import { NgForm, FormsModule } from '@angular/forms';
import { AlertService } from '../../services/alert.service';
import { NgModule } from '@angular/core';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.page.html',
  styleUrls: ['./signup.page.scss'],
})
@NgModule({
  imports: [
    FormsModule
  ]
})
export class SignupPage implements OnInit {
   modalB: boolean = false;

  constructor(
    private modalController: ModalController,
    private authService: AuthService,
    public navCtrl: NavController,
    private alertService: AlertService
  ) { }

  ngOnInit() {
  }

  // Dismiss Register Modal
  dismissRegister() {
    if(this.modalB == true){
    this.modalController.dismiss();
    this.modalB=false;
    }
  }

  async loginModal() {
    this.dismissRegister();
    const loginModal = await this.modalController.create({
      component: LoginPage,
    });
    this.modalB = true
    return await loginModal.present();
  }

  register(form: NgForm) {
    console.log(form.value.email);
    console.log(form.value.nom);
    console.log(form.value.prenom);
    console.log(form.value.password);
    this.authService.register(form.value.email, form.value.nom, form.value.prenom,
      form.value.password).subscribe(
        data => {
          this.authService.login(form.value.email, form.value.password).subscribe(
            data => {
              console.log(data);
            },
            error => {
              console.log(error);
            },
            () => {
              this.dismissRegister();
              this.navCtrl.navigateRoot('/tabs');
            }
          );
          this.alertService.presentToast(data['message']);
        },
        error => {
          console.log(error);
        },
        () => {

        }

      );
  }
}
