let imgs = [];
let imgsSize = 12;
let playerImg = new Image();

let entityImgs = [];

var tiles;
var players = [];
var moveableEntitys = [];
var loaded = false;

let currPlayerId;

let buildingMode = true;


let canvas = document.getElementsByClassName('canvas')[0];

let gameCamera ={};
gameCamera.xOffset = 0;
gameCamera.yOffset = 0;
const viewPort = 10;
let viewSizeMultiplier={};
viewSizeMultiplier.x=1;
viewSizeMultiplier.y=1;

let columns;
let rows;

let hotBarButtons = document.getElementsByClassName("hotbarButton");
let selectedButtonId = 0;
let selectedLayerId = 0;

for(var i = 0;i<imgsSize;i++){
		imgs[i] = new Image();
}
for(let i=1;i<=10;i++){
	imgs[i].src = "img/tiles/grass"+i+".png";
}
imgs[11].src = "img/tiles/wall.png";

playerImg.src = "img/tiles/player.png";

entityImgs[1] = new Image();
entityImgs.src = "img/tiles/player.png";

for(let i=0;i<hotBarButtons.length;i++){
	let element = hotBarButtons[i];
	if(i!=0){
		element.innerHTML = "<img class=\"hotbarButtonImage\" src=\""+imgs[i].src+"\"></img>";
	}
	element.addEventListener("click",function () {
		selectedButtonId = element.id;
	} );
}

document.getElementById("layer0").addEventListener("click",function () {
	selectedLayerId = 0;
} );
document.getElementById("layer1").addEventListener("click",function () {
	selectedLayerId = 1;
} );
document.getElementById("layer2").addEventListener("click",function () {
	selectedLayerId = 2;
} );
function toggleBuildingMode(){
	if(buildingMode){
		buildingMode=false;
		let hotBar = document.getElementsByClassName("hotbar")[0];
		hotBar.style.display = "none";
		let layerSelect = document.getElementsByClassName("layerSelect")[0];
		layerSelect.style.display = "none";
	}else{
		buildingMode = true;
		let hotBar = document.getElementsByClassName("hotbar")[0];
		hotBar.style.display = "block";
		let layerSelect = document.getElementsByClassName("layerSelect")[0];
		layerSelect.style.display = "block";
	}
}

function onmessage(obj){
	switch(obj.key){
		case "tiles":
			setTiles(obj);
			break;
		case "players":
			setPlayers(obj);
			break;
		case "updates":
			handleUpdates(obj.updates);
			break;
		case "msg":
			handleMessage(obj);
			break;
		case "toggleBuildingMode":
			toggleBuildingMode();
			break;
		case "pid":
			currPlayerId = obj.id;
			break;
		
	}
}

function handleMessage(obj){
	let msg = obj.msg;
	if(obj.username != 'undefined' && obj.username != null){
		msg = obj.username +":"+msg;
	}
	
	let chatbox = document.getElementsByClassName("msgContainer")[0];
	chatbox.innerHTML = chatbox.innerHTML + "<a class=\"chatMsg\">"+msg+"<br></a>"
}

function handleUpdates(updates){
	
	updates.forEach(element => {
		
		switch(element.gameUpdateKey){
			case "playerMove":
				movePlayer(element.player.pid,element.player.x,element.player.y);
				break;
			case "addPlayer":
				addPlayer(element.player);
				break;
			case "removePlayer":
				removePlayer(element.playerId);
				break;
			case "changeTileOnMap":
				changeTile(element);
				break;
			case "newMovableEntity":
				addNewMoveableEntity(element.entity);
				break;
			case "moveEntity":
				moveEntity(element.entity);
				break;
			case "removeEntity":
				removeEntity(element.id);
				break;

		}
	});
}
function removeEntity(eid){
	moveableEntitys.splice(eid);
}
function moveEntity(entity){
	if(!moveableEntitys[entity.id]!== void 0)
		moveableEntitys[entity.id] = entity;
}

function addNewMoveableEntity(entity){
	moveableEntitys[entity.id] = entity;
}

function changeTile(element){
	switch(element.layer){
		case 0:
			tiles.ground[element.x][element.y] =element.tileNew;
			
			break;
		case 1:
			tiles.belowPlayer[element.x][element.y] =element.tileNew;
			break;
		case 2:
			tiles.abovePlayer[element.x][element.y] =element.tileNew;
			break;
	}
}

function removePlayer(pid){
	//remove PLayer from players
	players.splice(pid,1);
}


function focusCamera(){
	if(currPlayerId !== void 0 && players[currPlayerId] !== void 0){
		const player = players[currPlayerId];
		
		const width = canvas.width;
		const height = canvas.height;
		let cellHeight = height/rows*viewSizeMultiplier.y,cellWidth = width/columns*viewSizeMultiplier.x;
		gameCamera.xOffset = player.x*cellWidth -width /2 +(player.width * (cellWidth/2));
		gameCamera.yOffset = player.y*cellHeight -height /2 +(player.height * (cellHeight/2));
		checkBlankSpace(cellHeight,cellWidth,width,height);
	}else{
		var obj = {};
		obj.key = "requestPlayerId";
		send(obj);
	}
}

function checkBlankSpace(cellHeight,cellWidth,width,height){
	const worldWidth = columns * cellWidth;
	const worldHeight = rows * cellHeight;
	
	if(gameCamera.xOffset <0){
		gameCamera.xOffset = 0;
	}else if(gameCamera.xOffset > worldWidth -width){
		gameCamera.xOffset =worldWidth -width ;
	}
	if(gameCamera.yOffset <0){
		gameCamera.yOffset = 0;
	}else if(gameCamera.yOffset > worldHeight -height){
		gameCamera.yOffset =worldHeight -height ;
	}
	
}
function adjustViewSize(){
	viewSizeMultiplier.x = columns / viewPort;
	viewSizeMultiplier.y = rows / viewPort;
}
function addPlayer(p){
	players[p.pid] = p;
}
function setTiles(obj){
	tiles = obj;
	columns = tiles.width;
	rows = tiles.height;
	adjustViewSize();
	loaded = true;
}
function setPlayers(obj){
	players = [];
	obj.players.forEach(element => {
		addPlayer(element);
	});
	currPlayerId = obj.playerId;
	//setCookie("username",players[currPlayerId].name,7);	
}

function movePlayer(id,x,y){
	players.forEach(element => {
		if(element.pid == id){
			element.x = x;
			element.y = y;
			return;
		}
	});
}

var chatInput = document.getElementsByClassName("msgIn")[0];
chatInput.addEventListener("keyup", function(event) {
    if (event.key === "Enter") {
		let obj = {};
		obj.key = "msg";
		obj.msg = chatInput.value;
		send(obj);
		chatInput.value ="";
    }
});

let clickInterval = 200;
var mousedownID = -1;
let mouseX = 0,mouseY = 0;
canvas.addEventListener("mousedown", function mousedown(event) {
  if(mousedownID==-1)  //Prevent multimple loops!
     mousedownID = setInterval(whilemousedown, clickInterval);

	whilemousedown();
});
canvas.addEventListener("mouseup",function mouseup(event) {
   if(mousedownID!=-1) {  //Only stop if exists
     clearInterval(mousedownID);
     mousedownID=-1;
   }

});

canvas.addEventListener("mousemove",function mouseup(event) {
	mouseX = event.clientX;
	mouseY = event.clientY;
});
function whilemousedown() {
	let mouse = {};
	mouse.clientX = mouseX;
	mouse.clientY = mouseY;

	let coords = getMousePos(canvas,mouse);
	
	let inWorld = getPosInWorld(coords);
	if(buildingMode){
		let obj = {};
		obj.key = "placeTile";
		obj.layer = selectedLayerId;
		obj.x = inWorld.x;
		obj.y = inWorld.y;
		obj.id = selectedButtonId;

		send(obj);
	}else{
		let obj = {};
		obj.key = "interact";
		obj.x  = inWorld.x;
		obj.y = inWorld.y;
		
		send(obj);
	}
}

function getPosInWorld(mouseOnCanvas){
	let width = canvas.width;
	let height = canvas.height;
	let cellHeight = height/rows * viewSizeMultiplier.y,cellWidth = width/columns * viewSizeMultiplier.x;
	
	let coords = {};
	coords.x = (mouseOnCanvas.x + gameCamera.xOffset)/cellWidth;
	coords.y = (mouseOnCanvas.y + gameCamera.yOffset)/cellHeight;

	return coords;
}

function  getMousePos(canvas, evt) {
	var rect = canvas.getBoundingClientRect(), // abs. size of element
		scaleX = canvas.width / rect.width,    // relationship bitmap vs. element for X
		scaleY = canvas.height / rect.height;  // relationship bitmap vs. element for Y
  
	return {
	  x: (evt.clientX - rect.left) * scaleX,   // scale mouse coordinates after they have
	  y: (evt.clientY - rect.top) * scaleY     // been adjusted to be relative to element
	}
  }

function render(){
	focusCamera();
	if(canvas.getBoundingClientRect().width != canvas.width){
		canvas.width = canvas.getBoundingClientRect().width;
	}
	if(canvas.getBoundingClientRect().height != canvas.height){
		canvas.height = canvas.getBoundingClientRect().height;
	}

	if(tiles == null) return;
	
	
	

	let ctx = canvas.getContext('2d');
	let width = canvas.width;
	let height = canvas.height;
	let cellHeight = height/rows * viewSizeMultiplier.y,cellWidth = width/columns * viewSizeMultiplier.x;
	
	
	let arr = tiles.ground;
	//clear canvas
	ctx.clearRect(0,0,width,height);

	

	for ( let x = 0; x < columns; x++ ) {
	        for ( let y = 0; y < rows; y++ ) {
				
	        	if(arr[x][y] !=0)	ctx.drawImage(imgs[arr[x][y]],x*cellWidth-gameCamera.xOffset,y*cellHeight-gameCamera.yOffset,cellWidth,cellHeight);
	        }
	 }
	 
	 players.forEach(p => {
		ctx.drawImage(playerImg,p.x*cellWidth+((1-p.width)/2)-gameCamera.xOffset,p.y*cellHeight+((1-p.height)/2)-gameCamera.yOffset,cellWidth*p.width,cellHeight*p.height);
	 });
	 
	 moveableEntitys.forEach(p=>{
		ctx.drawImage(playerImg,p.x*cellWidth+((1-p.width)/2)-gameCamera.xOffset,p.y*cellHeight+((1-p.height)/2)-gameCamera.yOffset,cellWidth*p.width,cellHeight*p.height);
	 });
	 	
}

function setCookie(cname, cvalue, exdays) {
	var d = new Date();
	d.setTime(d.getTime() + (exdays*24*60*60*1000));
	var expires = "expires="+ d.toUTCString();
	document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}



setInterval(() => {
	if(loaded){
		render();
	}
}, 50);

function onSocketLoad(){
	requestWorld();

}
function requestWorld() {
	let worldRequest = {};
	worldRequest.key = "getWorld";
	send(worldRequest);
}


kd.A.down(function () {
	if(chatInput === document.activeElement){
		return;
	}
 	var obj = {"key":"keyPress","code":65};
    send(obj);
});
kd.S.down(function () {
	if(chatInput === document.activeElement){
		return;
	}
 	var obj = {"key":"keyPress","code":83};
    send(obj);
});
kd.D.down(function () {
	if(chatInput === document.activeElement){
		return;
	}
 	var obj = {"key":"keyPress","code":68};
    send(obj);
});
kd.W.down(function () {
	if(chatInput === document.activeElement){
		return;
	}
 	var obj = {"key":"keyPress","code":87};
    send(obj);
});
   

kd.run(function () {
	kd.tick();
});

addMessageListener(onmessage);
addSocketLoadListener(onSocketLoad);

toggleBuildingMode();