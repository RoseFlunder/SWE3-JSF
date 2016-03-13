"use strict";

var CASEW = 3000;

// physics contants
var snapX = 0;
var randomNum = 0.0;
var R = 0.999;
var S = 0.01;
var tf = 0;
var vi = 0;
var animStart = 0;
var isMoving = false;
var LOGR = Math.log(R);

// settings
var SCROLL = true;
var LANG = 1;
var IGNORE = [];

var sounds_rolling = new Audio('resources/sounds/rolling.mp3');
var sounds_finish = new Audio('resources/sounds/tone.mp3');
function play_sound(x) {
	if (x == "roll") {
		sounds_rolling.play();
	} else if (x == "finish") {
		sounds_finish.play();
	}
}

function snapRender(x) {
	CASEW = $("#case").width();
	if (isMoving) {
		return;
	} else if (typeof x === 'undefined') {
		view(snapX);
	} else {
		var order = [ 1, 14, 2, 13, 3, 12, 4, 0, 11, 5, 10, 6, 9, 7, 8 ];
		var index = 0;
		for (var i = 0; i < order.length; i++) {
			if (x == order[i]) {
				index = i;
				break;
			}
		}
		var w = Math.floor((randomNum * 201) - 100);

		var dist = (index * 200) + 100 + w;
		dist += 3000 * 5;
		snapX = dist;
		view(snapX);
	}
}
function spin(m) {
	randomNum = Math.random(); 
	var x = m;
	play_sound("roll");
	var order = [ 1, 14, 2, 13, 3, 12, 4, 0, 11, 5, 10, 6, 9, 7, 8 ];
	var index = 0;
	for (var i = 0; i < order.length; i++) {
		if (x == order[i]) {
			index = i;
			break;
		}
	}
	var w = Math.floor((randomNum * 201) - 100);

	var dist = (index * 200) + 100 + w;
	dist += 3000 * 5;
	animStart = new Date().getTime();
	vi = getVi(dist);
	tf = getTf(vi);
	isMoving = true;

	setTimeout(function() {
		finishRoll(m);
	}, tf);
	render();
	snapRender();
}
function d_mod(vi, t) {
	return vi * (Math.pow(R, t) - 1) / LOGR;
}
function getTf(vi) {
	return (Math.log(S) - Math.log(vi)) / LOGR;
}
function getVi(df) {
	return S - df * LOGR;
}
function v(vi, t) {
	return vi * Math.pow(R, t);
}
function render() {
	var t = new Date().getTime() - animStart;
	if (t > tf)
		t = tf;
	var deg = d_mod(vi, t);
	view(deg);
	if (t < tf) {
		requestAnimationFrame(render);
	} else {
		snapX = deg;
		isMoving = false;
		updateText();
	}
}
function view(offset) {
	offset = -((offset + 3000 - CASEW / 2) % 3000);
	$("#case").css("background-position", offset + "px 0px");
}

function finishRoll(roll) {
	play_sound("finish");
	addBall(roll);
}

function addBall(roll){
	var count = $("#last .ball").length;
	if(count>=10){
		$("#past .ball").first().remove();
	}
	switch(roll) {
    case 0:
    	$("#past").append("<img class='ball' src='resources/images/0.png' style='width:100px;height:100px;'/>");
        break;
    case 1:
    	$("#past").append("<img class='ball' src='resources/images/1.png' style='width:100px;height:100px;'/>");
        break;
    case 2:
    	$("#past").append("<img class='ball' src='resources/images/2.png' style='width:100px;height:100px;'/>");
        break;
    case 3:
    	$("#past").append("<img class='ball' src='resources/images/3.png' style='width:100px;height:100px;'/>");
        break;
    case 4:
    	$("#past").append("<img class='ball' src='resources/images/4.png' style='width:100px;height:100px;'/>");
        break;
    case 5:
    	$("#past").append("<img class='ball' src='resources/images/5.png' style='width:100px;height:100px;'/>");
        break;
    case 6:
    	$("#past").append("<img class='ball' src='resources/images/6.png' style='width:100px;height:100px;'/>");
        break
    case 7:
    	$("#past").append("<img class='ball' src='resources/images/7.png' style='width:100px;height:100px;'/>");
        break;
    case 8:
    	$("#past").append("<img class='ball' src='resources/images/8.png' style='width:100px;height:100px;'/>");
        break;
    case 9:
    	$("#past").append("<img class='ball' src='resources/images/9.png' style='width:100px;height:100px;'/>");
        break;
    case 10:
    	$("#past").append("<img class='ball' src='resources/images/10.png' style='width:100px;height:100px;'/>");
        break;
    case 11:
    	$("#past").append("<img class='ball' src='resources/images/11.png' style='width:100px;height:100px;'/>");
        break;
    case 12:
    	$("#past").append("<img class='ball' src='resources/images/12.png' style='width:100px;height:100px;'/>");
        break;
    case 13:
    	$("#past").append("<img class='ball' src='resources/images/13.png' style='width:100px;height:100px;'/>");
        break;
    case 14:
    	$("#past").append("<img class='ball' src='resources/images/14.png' style='width:100px;height:100px;'/>");
        break;
        
    default:
    	$("#past").append("<img class=ball src='resources/images/RED.png' style='width:100px;height:100px;'/>");
	} 
}

window.addEventListener('resize', function(event) {
	snapRender();
});




