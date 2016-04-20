// create request URL from current URL
var frontendUrl = window.location.href;

// create URL for Ajax request
var createRequestURL = function (url, port, endpoint) {
    url = url.split(':')[0] + ":" + url.split(':')[1] + ":" + port + "/" + endpoint;
    return url;
};

// ask server for port where backend runs and call callback-Function with the received data.
var getPort = function (callback) {


    var backendUrl = createRequestURL(frontendUrl, 4141, "backendPort");

    $.get(backendUrl, function (data) {
        callback(data);
    });
};