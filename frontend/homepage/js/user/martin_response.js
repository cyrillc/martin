function MartinResponseRenderer() {
}

MartinResponseRenderer.prototype.renderResponse = function (martinStatement) {
    var martinCommand = $('<p class="martin-command"></p>');
    var martinResponse = $('<p class="martin-response"></p>');
    martinCommand.append(martinStatement.request.command);
    martinResponse.append(martinStatement.response.content);
    $("#martin-responses").prepend(martinResponse).prepend(martinCommand);

};

