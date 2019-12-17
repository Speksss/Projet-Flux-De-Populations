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
		//luminosité
		var brightness = detailsSensorBlocks[i].getElementsByClassName("td-brightness")[0].textContent;
		//niveau batterie
		var battery = detailsSensorBlocks[i].getElementsByClassName("td-battery")[0].textContent;
		console.log(battery);
		
		/* température idéal entre 15 et 25 °c*/
		var temperature1 = new JustGage({
		    id: "temperature"+i,
		    value: temp,
		    min: 0,
		    max: 100,
		    label : "°C",
		    levelColors: ["#5aa3ca"],
		    gaugeColor: "#f7f7f7",
		    title: "Température (en °C)"
		    
		});
		
		var brightness1 = new JustGage({
		    id: "brightness"+i,
		    value: brightness,
		    min: 0,
		    max: 5000,
		    label : "lux",
		    levelColors: ["#5aa3ca"],
		    gaugeColor: "#f7f7f7",
		    title: "Luminance (en lux)"
		});
		
		var battery1 = new JustGage({
			id: "battery"+i,
			value: battery,
			min: 0,
			max: 100,
			label : "%",
			levelColors: ["#ba1e1e","#f7b72b","#1eba2f"],
			gaugeColor: "#f7f7f7",
			title: "Niveau de Batterie (en %)"
		});
	}
}


    