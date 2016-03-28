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

    // send GET request with data and show response on page
    $.get("http://localhost:4040/command", command, function( data ) {
        $("#response").append(JSON.stringify(data) + '<br>');
    });
};