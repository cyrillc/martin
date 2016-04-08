#!/usr/bin/env node --harmony

'use strict';
var
    express = require('express'),
    morgan = require('morgan'),
    path = require('path');
var app = express();

app.use(morgan('dev'));
app.use(express.static(__dirname + '/homepage'));
app.get('/', function(req, res) {

    res.sendFile(path.join(__dirname + 'index.html'));

});
app.listen(4141, function() {
    console.log("ready for MArtIn operation.");
    console.log('backend port: ' + (process.argv[2] || 4040));
});

app.get('/backendPort', function(req, res) {
    res.contentType('json');
    res.send({ backendPort: (process.argv[2] || 4040) });
});
