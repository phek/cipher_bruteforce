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
var text = "KGzicnBhvyQhAANaY3BedRBlSXCzQPdaIYOuN29kRPQvaO4zYbFrSTN0IYYuOCT0tGKuAZNecYalrmNdIWdhwGvgQLB2pWvebbVqSSQjsQBdZPBuXFJhZHBjSXzgRf9yXHJhZHBbtXQoRQIvaPFgJEXMVTEm";

/* Regex */
var numberRegex = /^\d+$/;
var keyRegex = /^[A-Z]+$/;

/* Results */
var foundResults = {};

/* General */
var current = 0;
var padding = 10000;

/* Performance compare */
var performance = 0;
var old = current;

/* Empty results on start */
fs.truncate('results.txt', 0, () => {});

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
                console.log('Current text: ' + text);
            }
            break;
        case 'padding':
            if (commands[1]) {
                padding = parseInt(commands[1]);
                console.log('New padding set to: ' + padding);
            } else {
                console.log('Current padding: ' + padding);
            }
            break;
        case 'current':
            var value = commands[1];
            if (value) {
                if (numberRegex.test(value)) {
                    current = parseInt(value);
                } else if (keyRegex.test(value)) {
                    current = toNumber(value);
                } else {
                    console.log('Incorrect value');
                }
                console.log('New index set to: ' + toRadix(current, charset));
            } else {
                printCurrent();
            }
            break;
        case 'performance':
            printPerformance();
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

function toRadix(n, charset) {
    var result = [];

    n++;
    while (n !== 0) {
        result.unshift(charset.charAt((n - 1) % charset.length));
        n = Math.floor((n - 1) / charset.length);
    }

    return result.join('');
}

function toNumber(str) {
    var out = 0, len = str.length;
    for (var pos = 0; pos < len; pos++) {
        out += (str.charCodeAt(pos) - 64) * Math.pow(26, len - pos - 1);
    }
    return out - 1;
}

function restart() {
    foundResults = {};
    current = 1;
}

function statusCheck(print) {
    if (print) {
        printPerformance();
        printCurrent();
    }
    performance = (current - old) / 10;
    old = current;
    setTimeout(function () {
        statusCheck(print);
    }, 10 * 1000);
}

function printPerformance() {
    console.log("Performance: " + performance + " keys/s");
}

function printCurrent() {
    console.log("Current index: " + toRadix(current, charset));
}

http.listen(5000, function () {
    console.log('listening on *:5000');
    var print = false;
    if (process.argv[2]) {
        print = true;
    }
    statusCheck(print);
});

process.stdin.resume(); //so the program will not close instantly

//do something when app is closing
process.on('exit', exitHandler.bind(null, {cleanup: true}));

//catches ctrl+c event
process.on('SIGINT', exitHandler.bind(null, {cleanup: true}));

// catches "kill pid" (for example: nodemon restart)
process.on('SIGUSR1', exitHandler.bind(null, {cleanup: true}));
process.on('SIGUSR2', exitHandler.bind(null, {cleanup: true}));

//catches uncaught exceptions
process.on('uncaughtException', exitHandler.bind(null, {cleanup: true}));

function exitHandler(options, err) {
    if (options.cleanup) {
        //socketio.emit('exit');
        process.exit()
    }
    if (err) console.log(err.stack);
    if (options.exit) process.exit();
}