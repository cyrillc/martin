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

};