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

var visuallyPressButton = function() {
    $('#sendCommand').addClass('active');
};

var visuallyUnpressButton = function() {
    $('#sendCommand').removeClass('active');
};

var sendCommand = function() {
    // get and clear text input
    var textInput = $('#commandInput').val();
    $('#commandInput').val('');

    // create object to send to MArtIn
    var command = { command: textInput };

    // create request URL from current URL
    var url = window.location.href;
    url = url.split(':')[0] + ":" + url.split(':')[1] + ":4040/command";

    // send GET request with data and show response on page
    $.get(url, command, function(data) {
        $("#response").append(JSON.stringify(data) + '<br>');
    });
};