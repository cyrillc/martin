function PluginListRenderer(pluginList) {
    this.pluginList = pluginList;
}

PluginListRenderer.prototype.renderPlugins = function () {
    this.pluginList.forEach(function(element) {
        var pluginName = $('<p class="pluginName"></p>');
        var pluginDescription = $('<p class="pluginDescription"></p>');

        pluginName.append(element.name);
        pluginDescription.append(element.description);
        functionNames = createFunctionNameList(element.functionInformation);


        $("#pluginList").append(pluginName).append(pluginDescription).append(functionNames);
    });
};

var createFunctionNameList = function (functionList) {
    var htmlFunctionList = $('<ul class="functionNames"></ul>');

    functionList.forEach(function (functionElement) {
        var functionListElement = $('<li class="functionName"></li>');
        functionListElement.append(functionElement.name);
        htmlFunctionList.append(functionListElement);
    });

    return htmlFunctionList;
};

