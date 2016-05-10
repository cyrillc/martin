function MartinResponseRenderer() {
}

MartinResponseRenderer.prototype.renderResponse = function (martinStatement) {
    // for some fun
    if (martinStatement.request.command == "EASTEREGG") {
        $('#main').toggleClass("EASTEREGG");
        return;
    }

    var martinCommand = $('<p class="martin-command"></p>');
    var martinResponse = $('<p class="martin-response"></p>');
    martinCommand.append(martinStatement.request.command);
    martinResponse.append(martinStatement.response.responseText);
    $("#martin-responses").prepend(martinResponse).prepend(martinCommand);



};

