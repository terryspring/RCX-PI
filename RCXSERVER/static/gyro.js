// sets up the events
window.onload = function () {
    window.ondevicemotion = function(event) {
        /*
        var accelerationX = event.accelerationIncludingGravity.x;
        var accelerationY = event.accelerationIncludingGravity.y;
        var accelerationZ = event.accelerationIncludingGravity.z;
        */
        issueCommand('x',event.accelerationIncludingGravity.x);
        issueCommand('y',event.accelerationIncludingGravity.y);
        issueCommand('z',event.accelerationIncludingGravity.z);
    }
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
