const http = require('http').createServer();
const socketio = require('socket.io')(http);
var fs = require('fs');
const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});

/* Charset */
var charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

/* Text to decode */
var text = "G2pilVJccjJiQZ1poiM3iYZhj3I0IRbvj3wxomnoeOatVHUxZ2ozGKJgjXMzj2LgoOitBOM1dSDzHMatdRpmQZpidNehG29mkTxwmDJbGJxsjnVeQT9mTPSwSAOwnuWhSE50ByMpcuJoqGstJOCxqHCtdvG3HJV0TOGuwOIyoOGhwOHgm2GhlZpyISJik3J/";

/* Results */
var foundResults = {};

/* General */
var current = 0;
var padding = 10000;

/* Performance compare */
var old = current;

readline.on('line', function (input) {
    var commands = input.split(' ');
    switch (commands[0]) {
        case 'exit':
            exitHandler({cleanup: true});
            break;
        case 'restart':
            restart();
            break;
        case 'text':
            if (commands[1]) {
                text = commands[1];
                restart();
                console.log('New text set to: ' + text);
            } else {
                console.log('No text entered');
            }
            break;
        case 'padding':
            if (commands[1]) {
                padding = parseInt(commands[1]);
                console.log('New padding set to: ' + padding);
            } else {
                console.log('No padding entered');
            }
            break;
    }
});

socketio.on('connection', function (socket) {
    console.log('Socket connected');

    socket.on('decode_result', function (data) {
        if (data.result.length > 0) {
            for (var i = 0; i < data.result.length; i++) {
                console.log(data.result[i].key + ': ' + data.result[i].value);
                foundResults[data.result[i].key] = data.result[i].value;
                fs.appendFile('results.txt', data.result[i].key + ": " + data.result[i].value + "\n", function (err) {
                    if (err) return console.log(err);
                });
            }
            socketio.emit('decode_found', data);
        }
    });

    socket.on('request_decode', function () {
        newDecode(socket);
    });

    socket.on('request_results', function () {
        socket.emit('results', foundResults);
    });

    socket.on('request_current', function () {
        socket.emit('current', toRadix(current, charset));
    });

    socket.on('disconnect', function () {
        console.log("Socket disconnected");
    });
});

function newDecode(socket) {
    socket.emit('decode_this', {
        text: text,
        start: current,
        stop: current + padding,
        charset: charset
    });
    current += padding;
}

/* Slow - works on high numbers */
function toRadix(n, charset) {
    var result = [];

    n++;
    while (n !== 0) {
        result.unshift(charset.charAt((n - 1) % charset.length));
        n = Math.floor((n - 1) / charset.length);
    }

    return result.join('');
}

http.listen(5000, function () {
    console.log('listening on *:5000');
    if (process.argv[2]) {
        printStatus();
    }
});

function restart() {
    foundResults = {};
    current = 1;
}

function printStatus() {
    console.log("Performance: " + ((current - old) / 10) + " keys/s");
    console.log("Current key: " + toRadix(current, charset));
    old = current;
    setTimeout(printStatus, 10 * 1000);
}


process.stdin.resume(); //so the program will not close instantly

function exitHandler(options, err) {
    if (options.cleanup) {
        //socketio.emit('exit');
        process.exit()
    }
    if (err) console.log(err.stack);
    if (options.exit) process.exit();
}

//do something when app is closing
process.on('exit', exitHandler.bind(null, {cleanup: true}));

//catches ctrl+c event
process.on('SIGINT', exitHandler.bind(null, {cleanup: true}));

// catches "kill pid" (for example: nodemon restart)
process.on('SIGUSR1', exitHandler.bind(null, {cleanup: true}));
process.on('SIGUSR2', exitHandler.bind(null, {cleanup: true}));

//catches uncaught exceptions
process.on('uncaughtException', exitHandler.bind(null, {cleanup: true}));
