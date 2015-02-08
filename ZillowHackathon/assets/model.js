function getSchoolRate(house){
	var average = (house.SRP + house.SRM + house.SRH)/3 * 10;
	var score;
	if(average <= 50) score = 0;
	else {
		score = (average - 50) / 50 * 100;
	}
	house.school = -0.15 + score / 100 * 0.3;
	checkCompleteness(house);
	return house.school;
}


var hospitals = null;
function getHealthcareRate(house){
	var latitude = house.latitude;
	var longitude = house.longitude;
	if(hospitals == null) {
		var url = "https://data.medicare.gov/resource/ypbt-wvdk.json?county_name=KING";
		var xhr = new XMLHttpRequest();
		xhr.open('GET', url, false);
		xhr.send(null);
		//console.log(xhr.responseText);
		hospitals = JSON.parse(xhr.responseText);
	}
	var sum = 0;
	for(var i in hospitals) {
		var hospital = hospitals[i];
		var score = hospital.total_performance_score;
		//console.log(JSON.stringify(hospital));
		var lat2 = Number(hospital.location.latitude);
		var lng2 = Number(hospital.location.longitude);
		//console.log("lat2 " + lat2 + "--lng2 " + lng2);
		var distance = google.maps.geometry.spherical.computeDistanceBetween(new google.maps.LatLng(lat2,lng2), new google.maps.LatLng(latitude, longitude));
		distance = distance / 1000;
		//console.log("Distance: " + distance + "--score: " + score);
		if(distance >=20) continue;
		sum += Number(score) / (distance + 3);
	}
	console.log("Health sum: " + sum);
	if(sum >= 50) sum = 50;
	console.log("Health score: " + (-0.1 + sum/50 * 0.2));
	house.healthcare = -0.1 + sum/50 * 0.2;
	checkCompleteness(house);
	return house.healthcare;
}

function getTransporationRate(house) {
	var average = (house.WS + house.TS)/2;
	var score;
	if(average <= 30) score = 0;
	else {
		score = (average - 30) / 70 * 100;
	}
	house.transportation = -0.1 + score / 100 * 0.2;
	checkCompleteness(house);
	return house.transportation;
}

function getSafetyRate(house) {
	var latitude = house.latitude;
	var longitude = house.longitude;
	var where = "latitude>" + Math.floor(latitude * 1000 - 1) / 1000
	+ " AND " + "latitude<" + Math.floor(latitude * 1000 + 1) / 1000
	+ " AND " + "longitude>" + Math.floor(longitude * 1000 - 1) / 1000
	+ " AND " + "longitude<" + Math.floor(longitude * 1000 + 1) / 1000
	var url = "https://data.seattle.gov/resource/7ais-f98f.json?$select=offense_type&$where=" + where;
	//console.log("Safety url: " + url);
	var xhr = new XMLHttpRequest();
	xhr.open('GET', url, false);
	xhr.send(null);
	//console.log(xhr.responseText);
	var count = JSON.parse(xhr.response).length;
	console.log("Safety count: " + count);
	if(count>300) count=300;
	house.safety = 0.1 - count/300.0 * 0.2;
	console.log("Safety score: " + house.safety);
	checkCompleteness(house);
	return house.safety;
}

function checkCompleteness(house) {
	//console.log(JSON.stringify(house));
	if(house.safety && house.school && house.transportation && house.healthcare) {
		//house.callback();
		document.dispatchEvent(new CustomEvent("house_ready", {"detail": house}));
	}
}
