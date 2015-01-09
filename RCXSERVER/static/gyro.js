// sets up the events/*
if ((window.DeviceMotionEvent) {
    window.addEventListener('devicemotion', deviceMotionHandler, false);

}

function deviceMotionHandler(eventData){
    var acceleration = eventData.acceleration;
    issueCommand("x",acceleration.x);
    issueCommand("y",acceleration.y);
    issueCommand("z",acceleration.z);
}

// requests from the API
function issueCommand(axis, value) {
    var client = new XMLHttpRequest();

    client.open("GET", "http://" + window.location.hostname + ":3000/gyro/" + axis + "/" + value, true);
    client.setRequestHeader("Connection", "close");
    client.send();

    client.onreadystatechange = function () {
        if (client.readyState == 4 && client.status == 200) {
            console.log(client.responseText);
        }
    }
}
