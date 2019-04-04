var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
 server.listen(process.env.PORT || 3000);
// app.listen(process.env.PORT || 3000, function(){
//   console.log("Express server listening on port %d in %s mode", this.address().port, app.settings.env);
// });
app.get("/", function(req, res){
	res.sendFile(__dirname + "/index.html");	
});
io.sockets.on('connection',function(socket){
	console.log("Co thiet bi ket noi");

	socket.on("client-send-chat",function(data){
			 io.sockets.emit('server-send-chat', { noidung:data});

	});
});