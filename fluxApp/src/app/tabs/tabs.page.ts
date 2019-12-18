import { Component } from '@angular/core';
import { AndroidPermissions } from '@ionic-native/android-permissions/ngx';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { LocationAccuracy } from '@ionic-native/location-accuracy/ngx';
import { HttpClient , HttpHeaders } from '@angular/common/http';
import {AuthService} from 'src/app/services/auth.service';
import { MenuController} from '@ionic/angular';
@Component({
  selector: 'app-tabs',
  templateUrl: 'tabs.page.html',
  styleUrls: ['tabs.page.scss']
})
export class TabsPage {


    API_URL = 'http://35.206.157.216:8080/';
    PROXY_URL = 'https://cors-anywhere.herokuapp.com/';
    positionSubscription;
      timetest: any;
      options : any;
      token : any;
      locationCoords:any;
      newEmail : String;
      isTracking:boolean;
      constructor(
        private menu: MenuController,
        private  http: HttpClient,
        private androidPermissions: AndroidPermissions,
        private geolocation: Geolocation,
        private locationAccuracy: LocationAccuracy,
        private authService : AuthService
      ) {
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
          this.newEmail = this.token["__zone_symbol__value"]["email"];
          alert(this.newEmail);
          this.checkGPSPermission();
          this.trackMe();
        }

      //Check if application having GPS access permission
      checkGPSPermission() {
        this.sendPos(this.locationCoords.longitude,this.locationCoords.latitude).subscribe(data =>{
          alert("reussi");
        },
        error => {
          alert("erreur :"+error);
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
            alert(err);
          }
        );
      }

      requestGPSPermission() {
        this.locationAccuracy.canRequest().then((canRequest: boolean) => {
          if (canRequest) {
            console.log("4");
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
                  alert('requestPermission Error requesting location permissions ' + error)
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
          error => alert('Error requesting location peràçççàmissions ' + JSON.stringify(error))
        );
      }


      trackMe() {
     if (navigator.geolocation) {
       this.isTracking = true;
       navigator.geolocation.watchPosition((position) => {
         this.sendPos(position.coords.longitude,position.coords.latitude).subscribe(data =>{
         } ,
         error => {
           alert("Track erreur,veuilez activer le gps");
               });
       });
     } else {
       alert("Geolocation is not supported by this browser.");
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
                alert('Error getting location' + error);
        });
      }

      sendPos(longitude:number,latitude:number){
        alert("Envoie à l'email :");
      alert(this.newEmail);
      return this.http.post(this.PROXY_URL+this.API_URL + 'location/update?userMail='+this.newEmail+'&latitude='+latitude+'&longitude='+longitude,
          {},
          {responseType: "text" });
    }
}
