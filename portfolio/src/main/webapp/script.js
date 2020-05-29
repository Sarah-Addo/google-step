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
  let currSlide = 1;
  let numImgs = 3;

  createSlide(currSlide, numImgs);

  const prev = document.getElementById("prev");
  const next = document.getElementById("next");

  function changeSlide (direction) {
    currSlide += direction;
    if(currSlide < 1) {
      currSlide = numImgs;
    }
    else if(currSlide > numImgs) {
      currSlide = 1;
    }
    document.getElementById("slide-img").src = `images/img${currSlide}.jpg`;
    document.getElementById("slide-number").textContent = `${currSlide} of ${numImgs}`;
  }

  prev.onclick = () => {
    changeSlide(-1);
  };

  next.onclick = () => {
    changeSlide(1);
  };
});

function createSlide(currSlide, numImgs) {
  const slide = document.getElementById("slide");

  const prev = document.createElement("a");
  prev.setAttribute("id", "prev");
  prev.textContent = "❮";
  slide.appendChild(prev);

  const next = document.createElement("a");
  next.setAttribute("id", "next");
  next.textContent = "❯";
  slide.appendChild(next);

  const img = document.createElement("img");
  img.setAttribute("src", `images/img${currSlide}.jpg`)
  img.setAttribute("id", "slide-img");
  slide.appendChild(img);

  const slideNum = document.createElement("div");
  slideNum.textContent = `${currSlide} of ${numImgs}`;
  slideNum.setAttribute("id", "slide-number");
  slide.appendChild(slideNum);
}

