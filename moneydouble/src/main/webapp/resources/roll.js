"use strict";

var CASEW = 3000;

//physics contants
var snapX = 0;
var R = 0.999;
var S = 0.01;
var tf = 0;
var vi = 0;
var animStart = 0;
var isMoving = false;
var LOGR = Math.log(R);

//settings
var SCROLL = true;
var LANG = 1;
var IGNORE = [];

var sounds_rolling = new Audio('resources/sounds/rolling.wav');
sounds_rolling.volume = 0.5;
function play_sound(x){
	var conf = $("#settings_sounds").is(":checked");
	if(conf){
		if(x=="roll"){
			sounds_rolling.play();
		}		
	}		
}

function snapRender(x){
	
	if(isMoving){
		return;
	}else if(typeof x === 'undefined'){
		view(snapX);					
	}else{
		var order = [1,14,2,13,3,12,4,0,11,5,10,6,9,7,8];
		var index = 0;
		for(var i=0;i<order.length;i++){
			if(x==order[i]){
				index = i;
				break;
			}
		}  
		var max = 100;
		var min = -100;
		var w = Math.floor((max-min+1)+min);
		
		var dist = index*200 + 100 + w;  
		dist += CASEW*5;
		snapX = dist;
		view(snapX);
	}
}
function spin(m){
	CASEW = $("#case").width();
	var x = m;
	play_sound("roll");
	var order = [1,14,2,13,3,12,4,0,11,5,10,6,9,7,8];
	var index = 0;
	for(var i=0;i<order.length;i++){
		if(x==order[i]){
			index = i;
			break;
		}
	}  
	var max = 100;
	var min = -100;
	var w = Math.floor((max-min+1)+min);
	
	var dist = index*200 + 100 + w;  
	dist += CASEW*5;                
    animStart = new Date().getTime();                  
    vi = getVi(dist);
    tf = getTf(vi);
    isMoving = true;
    
    setTimeout(function(){
    	finishRoll();
    },tf);
    render();
    snapRender();
}
function d_mod(vi,t){
    return vi*(Math.pow(R,t)-1)/LOGR;
}
function getTf(vi){
    return (Math.log(S)-Math.log(vi))/LOGR;
}
function getVi(df){
    return S-df*LOGR;
}
function v(vi,t){
    return vi*Math.pow(R,t);
}
function render(){
    var t = new Date().getTime() - animStart;
    if(t>tf)
        t = tf;
    var deg = d_mod(vi,t);
    view(deg);
    if(t<tf){                   
        requestAnimationFrame(render);
    }else{
    	snapX = deg;
        isMoving = false;           
    }
}
function view(offset){
	offset = -((offset+CASEW-CASEW/2)%CASEW);
	$("#case").css("background-position",offset+"px 0px");   
}

function finishRoll(){
	play_sound("finish");
}

$(document).ready(function(){
	$(window).resize(function(){
		snapRender();
	});
});

	