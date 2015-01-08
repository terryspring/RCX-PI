// sets up the events
window.onload = function () {
    document.getElementById("goInput").onclick = function () {
        issueCommand(document.getElementById("commandInput").value, document.getElementById("paramInput").value);
    }
}

// requests from the API
function issueCommand(command, param) {
    var client = new XMLHttpRequest();

    client.open("GET", "http://" + window.location.hostname + ":3000/move/" + command + "/" + param, true);
    client.setRequestHeader("Connection", "close");
    client.send();

    client.onreadystatechange = function () {
        if (client.readyState == 4 && client.status == 200) {
            console.log(client.responseText);
        }
    }
}
