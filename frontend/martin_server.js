#!/usr/bin/env node --harmony

'use strict';
// load express, morgan and path modules for further usage
var
    express = require('express'),
    morgan = require('morgan'),
    path = require('path');
var app = express();

app.use(morgan('dev'));
// set path to static files to deliver to client
app.use(express.static(__dirname + '/homepage'));

// open endpoints
app.get('/', function (req, res) {

    res.sendFile(path.join(__dirname + 'index.html'));

});

app.get('/backendPort', function (req, res) {
    res.contentType('json');
    res.send({ backendPort: (process.argv[2] || 4040) });
});

// start server
app.listen(4141, function () {
    console.log("ready for MArtIn operation.");
    console.log('backend port: ' + (process.argv[2] || 4040));
});
