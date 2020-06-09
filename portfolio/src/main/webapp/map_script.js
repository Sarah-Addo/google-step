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
let map;

document.addEventListener("DOMContentLoaded", function() {
  createMap();
});

function createMap() {
  map = new google.maps.Map(
    document.getElementById('map'),
    { center: { lat: 37.422403, lng: -122.088073 }, zoom: 11 });

  infoWindow = new google.maps.InfoWindow;

  // Try HTML5 geolocation.
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      map.setCenter(pos);
      getGymResults(pos);
    }, function() {
      handleLocationError(true, infoWindow, map.getCenter());
    });
  } else {
    // Browser doesn't support Geolocation
    handleLocationError(false, infoWindow, map.getCenter());
  }
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pos);
  infoWindow.setContent(browserHasGeolocation ?
    'Error: The Geolocation service failed.' :
    'Error: Your browser doesn\'t support geolocation.');
  infoWindow.open(map);
}

function getGymResults(pos) {
  let request = {
    location: pos,
    radius: '2000',
    query: 'gym'
  };

  let service = new google.maps.places.PlacesService(map);
  service.textSearch(request, callback);
}

function callback(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    let max;
    const placesArea = document.getElementById("results-container");

    if (results.length < 10) {
      max = results.length;
    } else {
      max = 10;
    }

    for (let i = 0; i < max; i++) {
      let place = results[i];
      placesArea.appendChild(addPlace(place));
      createMarker(place);
    }
  }
}

function addPlace(place) {
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

  return placeElement;
}

function createMarker(place) {
  marker = new google.maps.Marker({
    map: map,
    title: place.name,
    animation: google.maps.Animation.DROP,
    position: place.geometry.location,
  });
}
