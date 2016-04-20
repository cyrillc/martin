// Defautl Port for the Backend is 4040. This can be changed in $(document).ready()
var backendPort = 4040;

// enabling *RETURN* to submit command
$(function() {
    $("#commandInput").keydown(function(event) {
        if (event.which == 13) {
            visuallyPressButton();
        }
    });
    $("#commandInput").keyup(function(event) {
        if (event.which == 13) {
            visuallyUnpressButton();
            $("#sendCommand").click();
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
    var backendUrl = createRequestURL(url, backendPort, "command");

    // send GET request with data and show response on page
    $.get(backendUrl, command, function(data) {
        $("#response").append(JSON.stringify(data) + '<br>');
    });
};


// ask the backend for example commands and history to show on the homepage
$(document).ready(function() {
    // create request URL from current URL
    var url = window.location.href;

    // ask server for port where backend runs
    url = createRequestURL(url, 4141, "backendPort");
    $.get(url, function(data) {

        backendPort = data.backendPort;

        exampleCommandsUrl = createRequestURL(url, backendPort, "exampleCommands");
        // send GET request with data and show response on page
        $.get(exampleCommandsUrl, function(data) {
            var exampleCommandsRenderer = new ExampleCommandsRenderer(data);
            exampleCommandsRenderer.renderCommands();
        });

        HistoryUrl = createRequestURL(url, backendPort, "history");
        // send GET request with data and show response on page
        $.get(HistoryUrl, function(data) {
            var historyRenderer = new HistoryRenderer(data);
            historyRenderer.render();
        });
    });



});