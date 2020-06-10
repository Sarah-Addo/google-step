// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
document.addEventListener("DOMContentLoaded", function() {
  const map = new Map(document.getElementById('map'));
});

class Map {
    constructor(mapElement) {
  		this.map = new google.maps.Map(mapElement,
    		{ center: { lat: 37.422403, lng: -122.088073 }, zoom: 11 });

  		this.infoWindow = new google.maps.InfoWindow;

  		// Try HTML5 geolocation.
			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition((position) => {
					this.pos = {
						lat: position.coords.latitude,
						lng: position.coords.longitude
					};
					this.map.setCenter(this.pos);
					this.getGymResults(this.pos);
				}, () => {
					this.handleLocationError(/*browserHasGeolocation*/ true, this.infoWindow, this.map.getCenter());
				});
			} else {
				// Browser doesn't support Geolocation
				this.handleLocationError(false, this.infoWindow, this.map.getCenter());
			}
    }
	
	handleLocationError(browserHasGeolocation, infoWindow, pos) {
  	this.infoWindow.setPosition(pos);
  	this.infoWindow.setContent(browserHasGeolocation ?
    	'Error: The Geolocation service failed.' :
    	'Error: Your browser doesn\'t support geolocation.');
  	this.infoWindow.open(this.map);
	}

	getGymResults(pos) {
  	const request = {
    	location: pos,
    	radius: '2000',
    	query: 'gym'
  	};
  	
		let service = new google.maps.places.PlacesService(this.map);
  	service.textSearch(request, this.callback);
	}

	callback(results, status) {
  	if (status == google.maps.places.PlacesServiceStatus.OK) {
    	let maxPlaces = Math.min(results.length, 15);
    	const placesArea = document.getElementById("results-container");

    	for (let i = 0; i < maxPlaces; i++) {
				console.log(results[i]);
      	placesArea.appendChild(this.addPlace(results[i]));
      	this.createMarker(results[i]);
    	}
  	}
	}

	addPlace(place) {
  	const placeElement = document.createElement("div");
  	placeElement.classList.add("place");

		const placeNameElement = document.createElement('div');
		placeNameElement.classList.add("place-name");
		placeNameElement.textContent = place.name;

		const placeAddressElement = document.createElement('div');
		placeAddressElement.classList.add("place-address");
		placeAddressElement.textContent = place.formatted_address;

		const placeRatingElement = document.createElement('div');
		placeRatingElement.classList.add("place-rating");
		placeRatingElement.textContent = place.rating;

		placeElement.appendChild(placeNameElement);
		placeElement.appendChild(placeAddressElement);
		placeElement.appendChild(placeRatingElement);

	console.log(placeElement);
		return placeElement;
	}

	createMarker(place) {
		marker = new google.maps.Marker({
			map: map,
			title: place.name,
			animation: google.maps.Animation.DROP,
			position: place.geometry.location,
		});
	}

}
