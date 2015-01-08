var express = require('express');
var request = require('request');
var app = express();

app.use(express.static(__dirname + '/static'));

var axisStats = {
    "x" : 0,
    "y" : 0,
    "z" : 0
}

function setGyro (axis, val){
    axisStats[axis] = val;
    console.log("Gyro set to:\n" + JSON.stringify(axisStats,null,4));
}

function send2RCX (command, length){
    var options = {
        url : "http://127.0.0.1:3001?command=" + command + "&length=" + length
    }
    request.get(options, function (err, response, body) {
        if(!err)
            console.log('Request routed to RCX@' + options.url);
        else
            console.log('Erroneous sending of request to RCX@' + options.url + "\nErr: " + err);
    });
}

/*
function getFromRCX (){
    var options = {
        url : "localhost:3001"
    }
    request(options, function (err, response, body) {
        if(!err)
            res.send(body);
        else
            console.log('Cannot send request to ' + req.path);
    });
}
*/

app.get('/', function (req, res) {
    res.send('Turn back.');
});

app.get('/remote', function (req, res) {
    res.sendfile('./static/remote.html');
});


app.get('/move/*', function (req, res) {
    send2RCX(req.path.split('/')[2], req.path.split('/')[3]);
    res.send('{"status" : true}');
});

app.get('/gyro/*', function (req, res) {
    setGyro(req.path.split('/')[2], req.path.split('/')[3]);
    res.send('{"status" : true}');
});

/*
app.get('/get*', function (req, res) {
    getFromRCX(res);
});
*/

app.listen(3000);
