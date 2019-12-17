var eventListener = function(){
	sensorsDatas();
}

window.addEventListener("load", eventListener);

var sensorsDatas = function(){
	
	/* Récupération des données des classes "detailSensorBlock" afin de dynamiser la création des gauges*/
	var detailsSensorBlocks = document.getElementsByClassName("detailSensorBlock");
	for(i=0;i<detailsSensorBlocks.length;i++){
		// température
		var temp = detailsSensorBlocks[i].getElementsByClassName("td-temperature")[0].textContent;
		//humidité
		var moisture = detailsSensorBlocks[i].getElementsByClassName("td-moisture")[0].textContent;
		//luminosité
		var brightness = detailsSensorBlocks[i].getElementsByClassName("td-brightness")[0].textContent;
		console.log(brightness);
		
		/* température idéal entre 15 et 25 °c*/
		var temperature1 = new JustGage({
		    id: "temperature"+i,
		    value: temp,
		    min: 0,
		    max: 100,
		    label : "°C",
		    levelColors: ["#5aa3ca"],
		    title: "Température (en °C)"
		    
		});
		
		/* Humidité idéal entre 45 et 50% */
		var moisture1 = new JustGage({
		    id: "moisture"+i,
		    value: moisture,
		    min: 0,
		    max: 100,
		    label : "%",
		    levelColors: ["#5aa3ca"],
		    title: "Humidité (en %)"
		});
		
		var brightness1 = new JustGage({
		    id: "brightness"+i,
		    value: brightness,
		    min: 0,
		    max: 5000,
		    label : "lux",
		    levelColors: ["#5aa3ca"],
		    title: "Luminance (en lux)"
		});
	}
}


    