import { Component, OnInit , Input, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-list-event',
  templateUrl: './list-event.component.html',
  styleUrls: ['./list-event.component.scss'],
})

@Injectable()
export class ListEventComponent implements OnInit {

  @Input() arrayEvents ;
  @Input() listFilter : string;

  constructor() {
      console.log(this.arrayEvents)
   }


  ngOnInit() {}

}
