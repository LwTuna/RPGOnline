let onMessageListeners = [];
let onSocketLoadListeners = [];


function addMessageListener(listener){
    onMessageListeners[onMessageListeners.length+1] = listener;
}

function addSocketLoadListener(listener){
    onSocketLoadListeners[onSocketLoadListeners.length+1] = listener;
}

let socket = new WebSocket("ws://"+location.host+"/websocket");
socket.onopen = function(e){
    console.log("Socket Opened");
    //TetsLoginPacket
    const pId = "";//getCookie("username");
    if(pId != ""){
        let loginPacket = {};
        loginPacket.key = "login";
        loginPacket.username = pId;
        send(loginPacket);        
    }else{
        let loginPacket = {};
        loginPacket.key = "login";
        loginPacket.username = "testUsername"+Math.random();
        loginPacket.password = "testPwd";
        send(loginPacket);
    }

    

    for(let i=1;i<onSocketLoadListeners.length;i++){
        onSocketLoadListeners[i]();
    }

};

function getCookie(cname) {
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for(var i = 0; i <ca.length; i++) {
	  var c = ca[i];
	  while (c.charAt(0) == ' ') {
		c = c.substring(1);
	  }
	  if (c.indexOf(name) == 0) {
		return c.substring(name.length, c.length);
	  }
	}
	return "";
  }

socket.onmessage = function(event){
    const data = event.data;
    const obj = JSON.parse(data);
    for(let i=1;i<onMessageListeners.length;i++){
        onMessageListeners[i](obj);
    }
}
socket.onclose = function(e){
    console.log("Socket closed"+e);
    alert("Disconnected:"+e);
};

function send(obj){
    if(socket.OPEN){
        var str = JSON.stringify(obj);
        if(str != null){
            socket.send(str);
        }
    }else{
        alert("Cant send Object due Connection closed");
    }
}

