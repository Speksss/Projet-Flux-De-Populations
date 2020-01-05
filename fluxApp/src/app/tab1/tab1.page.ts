import { User } from 'src/app/models/user';
import { Component } from '@angular/core';
import { AndroidPermissions } from '@ionic-native/android-permissions/ngx';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { LocationAccuracy } from '@ionic-native/location-accuracy/ngx';
import { HttpClient , HttpHeaders } from '@angular/common/http';
import {AuthService} from 'src/app/services/auth.service';
import { AlertService } from '../services/alert.service';
import { MenuController} from '@ionic/angular';
import { EnvService } from '../services/env.service';
import { AfterContentInit, OnInit , ViewChild } from '@angular/core';
import { Platform, NavController } from '@ionic/angular';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
declare var google;

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page implements OnInit , AfterContentInit{

    arreas : Array<any>;
    coordinate : Array<any>;
    API_URL = 'http://35.206.157.216:8080/';
    PROXY_URL = 'https://cors-anywhere.herokuapp.com/';
  positionSubscription;
    timetest: any;
    options : any;
    token : any;
    locationCoords:any;
    newEmail : String;
    isTracking:boolean;
    map;
   geolocation: Geolocation;
    @ViewChild('mapElement',{static: true}) mapElement;
    marker;
    constructor(
      private platform: Platform,
      private splashScreen: SplashScreen,
      private statusBar: StatusBar,
      private navCtrl: NavController,
      private env:EnvService,
      private menu: MenuController,
      private  http: HttpClient,
      private alertService: AlertService,
      private androidPermissions: AndroidPermissions,
      private locationAccuracy: LocationAccuracy,
      private authService : AuthService
    ) {
      this.token = this.authService.storage.getItem('token');
      this.locationCoords = {
        latitude: "",
        longitude: "",
        accuracy: "",
        timestamp: ""
        }
        this.menu.enable(true);
      }


      ionViewWillEnter(){
        this.token = this.authService.storage.getItem('token');
        console.log(this.newEmail);
        this.checkGPSPermission();
        this.trackMe();
      }
      ngOnInit():void{
      }

      ngAfterContentInit(): void{

        this.addMarker(this.map,this.locationCoords.latLng);
        this.map = new google.maps.Map(
          this.mapElement.nativeElement,
          {
            //Centrer sur notre campus
            center : {lat: 50.321683, lng: 3.513588},
            zoom: 20,
            heading: 90,
            tilt: 45
          });

          console.log('View Enter');
          this.getArreasData();
          console.log('Area done');

          }

      //Centre la map sur la position de l'utilisateur
      centerOnUser(){
        this.map = new google.maps.Map(
          this.mapElement.nativeElement,
          {
            //Centrer sur l'utilisateur
            center : {lat: this.locationCoords.latitude, lng: this.locationCoords.longitude},
            zoom: 20,
            heading: 90,
            tilt: 45
          });
      }

      //Recupere le JSON correspondant aux données de chaque zone.
        getArreasData(){
            console.log('Getting Area data from API');
            let request = this.env.PROXY_URL + this.env.API_URL + "/area/all";
            this.http.get(request , {responseType : "text"}).subscribe(
              (response) => {
                console.log('reussi');
              this.arreas = JSON.parse(response);
              this.createZone();
              },
              (error) => {
                console.log(error);
              }
            );
          }

        //Recupere les coordonnées de l'api pour creer les oznes.
        createZone(){
          for(let i = 0;i<this.arreas.length;i++){
          var coords  = JSON.parse(this.arreas[i].coordinates);
          console.log(coords.x1);
          var zoneCoord =  [
                    {lat:parseFloat(coords.x1),lng:parseFloat(coords.y1)},
                    {lat:parseFloat(coords.x2),lng:parseFloat(coords.y2)},
                    {lat:parseFloat(coords.x3),lng:parseFloat(coords.y3)},
                    {lat:parseFloat(coords.x4),lng:parseFloat(coords.y4)}
                  ];
          this.createZoneOnCoord(zoneCoord);
        }
       }
       //Creation d'une zone selon les coordonnées
        createZoneOnCoord(coords)  {
            var zone = new google.maps.Polygon({
              paths: coords,
              strokeColor: '#FF0000',
              strokeOpacity: 0.8,
              strokeWeight: 2,
              fillColor: '#3AFF33',
              fillOpacity: 0.2
            });
            zone.setMap(this.map);
          }  //Ajoute un marqueur sur lA map
        addMarker(map:any,location){

        new google.maps.Marker({
        map: map,
        animation: google.maps.Animation.DROP,
        position:  location,
      });
        }
    //Check if application having GPS access permission
    checkGPSPermission() {
      this.sendPos(this.locationCoords.longitude,this.locationCoords.latitude).subscribe(data =>{
        console.log("");
      },
      error => {
        console.log("no access to position ");
            });
    this.androidPermissions.checkPermission(this.androidPermissions.PERMISSION.ACCESS_COARSE_LOCATION).then(
        result => {
          if (result.hasPermission) {

            //If having permission show 'Turn On GPS' dialogue
            this.askToTurnOnGPS();
          } else {

            //If not having permission ask for permission
            this.requestGPSPermission();
          }
        },
        err => {
          console.log(err);
        }
      );
    }

    requestGPSPermission() {
      this.locationAccuracy.canRequest().then((canRequest: boolean) => {
        if (canRequest) {
          console.log("");
        } else {
          //Show 'GPS Permission Request' dialogue
          this.androidPermissions.requestPermission(this.androidPermissions.PERMISSION.ACCESS_COARSE_LOCATION)
            .then(
              () => {
                // call method to turn on GPS
                this.askToTurnOnGPS();
              },
              error => {
                //Show alert if user click on 'No Thanksh
                console.log('requestPermission Error requesting location permissions ' + error)
              }
            );
        }
      });
    }

    askToTurnOnGPS() {
      this.locationAccuracy.request(this.locationAccuracy.REQUEST_PRIORITY_HIGH_ACCURACY).then(
        () => {
          // When GPS Turned ON call method to get Accurate location coordinates
          this.getLocationCoordinates();

        },
        error => console.log('Error requesting location permissions ' + JSON.stringify(error))
      );
    }


    trackMe() {
   if (navigator.geolocation) {
     this.isTracking = true;
     navigator.geolocation.watchPosition((position) => {
       this.sendPos(position.coords.longitude,position.coords.latitude).subscribe(data =>{
         console.log(data);
       } ,
       error => {
         console.log("Error tracking");
             });
     });
   } else {
     console.log("Geolocation is not supported by this browser.");
   }
 }

    // Methos to get device accurate coordinates using device GPS
    getLocationCoordinates() {
      this.geolocation.getCurrentPosition().then((resp) => {
        this.locationCoords.latitude = resp.coords.latitude;
        this.locationCoords.longitude = resp.coords.longitude;
        this.locationCoords.accuracy = resp.coords.accuracy;
        this.locationCoords.timestamp = resp.timestamp;
            }).catch((error) => {
              console.log('Error getting location' + error);
      });
    }

    sendPos(longitude:number,latitude:number){
    return this.http.post(this.env.PROXY_URL+this.env.API_URL +
       'location/update?userMail='+this.token["__zone_symbol__value"]["email"]+'&latitude='+latitude+'&longitude='+longitude,
        {},
        {responseType: "text" });
  }

  // When logout button is pressed
  logout() {
    console.log("inside logout");
    console.log("Email :");
    console.log(this.token["__zone_symbol__value"]["email"]);
    console.log("Password :");
    console.log(this.authService.password);
    this.authService.logout(this.token["__zone_symbol__value"]["email"], this.authService.password).subscribe(
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
