// ask the backend for information of all plugins to show on the homepage after the document has loaded
$(document).ready(function () {
    // ask server for port where backend runs and call callback-Function with the received data.
    getPort(function (port) {

        backendPort = port;

        pluginListUrl = createRequestURL(frontendUrl, backendPort, "pluginList");
        // send GET request with data and show response on page
        $.get(pluginListUrl, function (receivedPluginList) {
            var pluginListRenderer = new PluginListRenderer(receivedPluginList);
            pluginListRenderer.renderPlugins();
        });

    });

});

$(document)
    .ajaxStart(function () {
        $('.loading').show();
    })
    .ajaxStop(function () {
        $('.loading').hide();
    });