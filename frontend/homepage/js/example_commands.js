function ExampleCommandsRenderer(commandList) {
    this.commandList = commandList;
}

ExampleCommandsRenderer.prototype.renderCommands = function() {
    this.commandList.forEach(function(element) {
        var exampleCommand = $('<p class="exampleCommand"></p>');
        var callDescription = $('<p class="callDescription"></p>');
        exampleCommand.append(element.call);
        callDescription.append(element.description);
        $("#possibleCommands").append(exampleCommand).append(callDescription);
    }, this);
};

