import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { LoginPageRoutingModule } from './login-routing.module';

import { LoginPage } from './login.page';

import { TabsPageModule } from '../../tabs/tabs.module';

import { TabsPage } from '../../tabs/tabs.page';

import { SignupPageModule } from '../signup/signup.module';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    LoginPageRoutingModule,
    SignupPageModule,
    TabsPageModule
  ],
  declarations: [LoginPage]
})
export class LoginPageModule {}
