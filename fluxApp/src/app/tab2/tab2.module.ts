import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Tab2Page } from './tab2.page';
import { EventComponent } from '../event/event.component';
import { ListEventComponent } from '../list-event/list-event.component';
import { Ng2SearchPipeModule } from 'ng2-search-filter';


@NgModule({
  imports: [
    IonicModule,
    FormsModule,
    CommonModule,
    Ng2SearchPipeModule,
    RouterModule.forChild([{ path: '', component: Tab2Page }])
  ],
  declarations: [Tab2Page,
                EventComponent,
              ListEventComponent]
})
export class Tab2PageModule {}
