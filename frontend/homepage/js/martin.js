var backendPort = 4242;

// enabling *RETURN* to submit command
$(function() {
    $("#commandInput").keydown(function(event) {
        if (event.which == 13) {
            visuallyPressButton();
            $("#sendCommand").click();
        }
    });
    $("#commandInput").keyup(function(event) {
        if (event.which == 13) {
            visuallyUnpressButton();
        }
    });

});

// make submit button look pressed for visual feedback
var visuallyPressButton = function() {
    $('#sendCommand').addClass('active');
};
var visuallyUnpressButton = function() {
    $('#sendCommand').removeClass('active');
};

// create URL for Ajax request
var createRequestURL = function(url, port, endpoint) {
    url = url.split(':')[0] + ":" + url.split(':')[1] + ":" + port + "/" + endpoint;
    return url;
};

// sending a command to the backend of MArtIn using an Ajax request
var sendCommand = function() {
    // get and clear text input
    var textInput = $('#commandInput').val();
    $('#commandInput').val('');

    // create object to send to MArtIn
    var command = { command: textInput };

    // create request URL from current URL
    var url = window.location.href;
    url = createRequestURL(url, backendPort, "command");

    // send GET request with data and show response on page
    $.get(url, command, function(data) {
        $("#response").append(JSON.stringify(data) + '<br>');
    });
};

// ask the backend for example commands to show on the homepage
$(document).ready(function() {
    // create request URL from current URL
    var url = window.location.href;
    url = createRequestURL(url, backendPort, "exampleCommands");

    // send GET request with data and show response on page
    $.get(url, function(data) {
        $("#possibleCommands").append(JSON.stringify(data) + '<br>');
    });
});