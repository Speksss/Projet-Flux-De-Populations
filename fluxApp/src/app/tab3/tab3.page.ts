import { AfterContentInit,Component, OnInit , ViewChild } from '@angular/core';
import { Geolocation } from '@ionic-native/geolocation';

declare var google;

@Component({
  selector: 'app-tab3',
  templateUrl: 'tab3.page.html',
  styleUrls: ['tab3.page.scss']
})


export class Tab3Page implements OnInit , AfterContentInit {
  map;
 geolocation: Geolocation;
  @ViewChild('mapElement',{static: true}) mapElement;
  constructor() {}
  marker;
  lngMark: Number[] = new Array(0);
  latMark: Number[] = new Array(0);
ngOnInit():void{
}

ngAfterContentInit(): void{

  this.map = new google.maps.Map(
    this.mapElement.nativeElement,
    {
      //Centrer sur notre campus
      center : {lat: 50.321683, lng: 3.513588},
      zoom: 20,
      heading: 90,
      tilt: 45
    });

    //Clic listener : A chaque clic on ajoute un marqueur sur la -position
    google.maps.event.addListener(this.map, 'click', <LeafletMouseEvent>(e) => {
      this.addMarker(this.map,e.latLng);
    });

    //Creation d'une zoone selon les coordonnées.
    var triangleCoords = [
              {lat:50.32087142621793,lng: 3.5134653484242335 },
              {lat:50.32087142621793, lng: 3.513956192673959},
              {lat:50.320328542569726, lng:  3.513956192673959},
              {lat:50.320328542569726, lng: 3.5134653484242335}
            ];

            // Istv3
            var bermudaTriangle = new google.maps.Polygon({
              paths: triangleCoords,
              strokeColor: '#FF0000',
              strokeOpacity: 0.8,
              strokeWeight: 2,
              fillColor: '#FF0000',
              fillOpacity: 0.35
            });
            bermudaTriangle.setMap(this.map);


          }


//Ajoute un marqueur sur lA map
  addMarker(map:any,location){

  new google.maps.Marker({
  map: map,
  animation: google.maps.Animation.DROP,
  position:  location,
});
 var latitude = location.lat();
 var longitude = location.lng();
 this.lngMark.push(longitude);
 this.latMark.push(latitude);
for(var i = 0;i<this.lngMark.length;i++) {
   console.log("marker n°"+i+" : longitude :" +this.lngMark[i]+" latitude  :" +this.latMark[i]);
    }
  }
}
