var express = require('express');
var request = require('request');
var app = express();

var axisStats = {
    "x" : 0,
    "y" : 0,
    "z" : 0
}

function setGyro (axis, val){
    axisStats[axis] = val;
}

function send2RCX (command, length){
    var options = {
        url : "localhost:3001?command=" + command + "&length=" + length
    }
    request(options, function (err, response, body) {
        if(!err)
            console.log('Request routed to ' + req.path);
        else
            console.log('Cannot send request to ' + req.path);
    });
}


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

app.get('/', function (req, res) {
    res.send('Turn back.');
});

app.get('/move/*', function (req, res) {
    send2RCX(req.path.split('/')[2], req.path.split('/')[3]);
});

app.get('/gyro/*', function (req, res) {
    setData(req.path.split('/')[2], req.path.split('/')[3]);
    res.send('OK');
});

app.get('/get*', function (req, res) {
    getFromRCX(res);
});

app.listen(3000);
