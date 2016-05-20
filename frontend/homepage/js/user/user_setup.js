// variable to move through history with *UP* and *DOWN* arrows
var historyLocation = 0;

// timing flag to append if timing information is wanted
var timingFlag = ' -t';

// boolean to indicate if timing information is wanted
var wantTimingInformation = false;

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
    $("#commandInput").keyup(function (event) {
        if (event.which == 38) {
            historyLocation = Math.min(historyLocation + 1, 16);
            getPreviousCommand(historyLocation);

        }
    });
    $("#commandInput").keyup(function (event) {
        if (event.which == 40) {
            historyLocation = Math.max(historyLocation - 1, 0);
            getPreviousCommand(historyLocation);
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
    // shows MArtIn thingking Area
    $('.thinking').slideDown("fast");
    $('.history-loading').slideDown("fast");
    // get and clear text input
    var textInput = $('#commandInput').val();
    textInput = textInput.replace(/(<([^>]+)>)/ig,'');
    $('#commandInput').val('');

    // check for timing flag
    textInput=checkTimingFlag(textInput);

    // create object to send to MArtIn
    var command = {
        command: textInput,
        timed: wantTimingInformation
    };

    // create request URL from current URL
    var backendUrl = createRequestURL(frontendUrl, backendPort, "command");

    // send GET request with data and show response on page
    $.get(backendUrl, command, function (response) {
        var martinStatement = {
            request: command,
            response: response
        };

        var historyItem = {
            date: new Date(),
            request: command,
            response: response
        };


        var martinResponseRenderer = new MartinResponseRenderer();
        //martinResponseRenderer.renderResponse(martinStatement, wantTimingInformation);

        // if wantTimingInformation is set, a chart will be drawn
        // drawTimingChart(response);

        var historyRenderer = new HistoryRenderer(null);
        historyRenderer.renderItem(historyItem);
    })
        // always hide the Section.
        .always(function () {
            // hides thinking Area
            $('.thinking').slideUp("fast");
            $('.history-loading').slideUp("fast");
        });

    // reset location to move through history with *UP* and *DOWN* arrows
    historyLocation = 0;

};



var addRequestToHistory = function (requestText) {

};

// ask the backend for example commands and history to show on the homepage after the document has loaded
$(document).ready(function () {

    // ask server for port where backend runs and call callback-Function with the received data.
    getPort(function (port) {

        backendPort = port;

        exampleCommandsUrl = createRequestURL(frontendUrl, backendPort, "exampleCommands");
        $('.possible-commands-loading').show();
        // send GET request with data and show response on page
        $.get(exampleCommandsUrl, function (receivedExampleCommands) {
            var exampleCommandsRenderer = new ExampleCommandsRenderer(receivedExampleCommands);
            exampleCommandsRenderer.renderCommands();


        })
            // always hide the Section.
            .always(function () {
                $('.possible-commands-loading').hide();
            });

        HistoryUrl = createRequestURL(frontendUrl, backendPort, "history");
        var amountOfHistoryItems = { amount: 15 };
        $('.history-loading').show();
        // send GET request with data and show response on page
        $.get(HistoryUrl, amountOfHistoryItems, function (receivedHistory) {
            var historyRenderer = new HistoryRenderer(receivedHistory);
            historyRenderer.renderAll();
        })
            // always hide the Section.
            .always(function () {
                $('.history-loading').hide();
            });
		registerOnServerEvent(createRequestURL(frontendUrl,backendPort,"serverOutput"));
    });
	
	

});

var checkTimingFlag = function (textInput) {
    if (textInput.indexOf(' -t') > -1) {
        wantTimingInformation = true;
        textInput = textInput.replace(' -t', '');
    } else {
        wantTimingInformation = false;
    }
    return textInput;
}

// function to move through history with *UP* and *DOWN* arrows
var getPreviousCommand = function (location) {
    var selector = '#historyItems > tbody > tr:nth-child(' + location + ') > td:nth-child(2)';
    $('#commandInput').val($(selector).html());
}


var registerOnServerEvent = function (url) {
	var source = new EventSource(url);
		source.onmessage = function(event){
			console.log(JSON.parse(event.data)); 
            var martinResponseRenderer = new MartinResponseRenderer();
            martinResponseRenderer.renderEvent(JSON.parse(event.data));
		}
} 










