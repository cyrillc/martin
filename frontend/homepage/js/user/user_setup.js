// Defautl Port for the Backend is 4040. This can be changed in $(document).ready()
var backendPort = 4040;

// enabling *RETURN* to submit command
$(function () {
    $("#commandInput").keydown(function (event) {
        if (event.which == 13) {
            visuallyPressButton();
        }
    });
    $("#commandInput").keyup(function (event) {
        if (event.which == 13) {
            visuallyUnpressButton();
            $("#sendCommand").click();
        }
    });
});

// make submit button look pressed for visual feedback
var visuallyPressButton = function () {
    $('#sendCommand').addClass('active');
};
var visuallyUnpressButton = function () {
    $('#sendCommand').removeClass('active');
};

// sending a command to the backend of MArtIn using an Ajax request
var sendCommand = function () {
    // get and clear text input
    var textInput = $('#commandInput').val();
    $('#commandInput').val('');

    // create object to send to MArtIn
    var command = { command: textInput };

    // create request URL from current URL
    var backendUrl = createRequestURL(frontendUrl, backendPort, "command");

    // send GET request with data and show response on page
    $.get(backendUrl, command, function (data) {
        $("#response").append(JSON.stringify(data) + '<br>');
    });
};


// ask the backend for example commands and history to show on the homepage after the document has loaded
$(document).ready(function () {

    // ask server for port where backend runs and call callback-Function with the received data.
    getPort(function (port) {

        backendPort = port;

        exampleCommandsUrl = createRequestURL(frontendUrl, backendPort, "exampleCommands");
        // send GET request with data and show response on page
        $.get(exampleCommandsUrl, function (receivedExampleCommands) {
            var exampleCommandsRenderer = new ExampleCommandsRenderer(receivedExampleCommands);
            exampleCommandsRenderer.renderCommands();
        });

        HistoryUrl = createRequestURL(frontendUrl, backendPort, "history");
        // send GET request with data and show response on page
        $.get(HistoryUrl, function (receivedHistory) {
            var historyRenderer = new HistoryRenderer(receivedHistory);
            historyRenderer.render();
        });
    });

});