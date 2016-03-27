$(function() {
    $("#commandInput").keyup(function(event) {
        if (event.which == 13) {
            $("#sendCommand").click();
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
    visuallyPressButton();
    setTimeout(visuallyUnpressButton, 100);

};