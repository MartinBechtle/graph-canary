const GRAPH_URL = "graph";

$(document).ready(function() {

    $('#loadgraph').click(loadGraph);
});

function loadGraph() {

    $.get({
        url: GRAPH_URL,
        success: onLoadGraphSuccess
    });
}

function onLoadGraphSuccess() {

    loadFakeGraph();
}

const IMG_DIR = 'img/dependencies/';

const IMG_SERVICE = IMG_DIR + 'canary_resource.png';

var nodes = null;
var edges = null;
var network = null;

function typeToImage(type) {

    switch (type) {

        case 'API':             return 'canary_api.png';
        case 'DATABASE':        return 'canary_db.png';
        case 'CACHE':           return 'canary_cache.png';
        case 'STORAGE':         return 'canary_storage.png';
        case 'CONFIGURATION':   return 'canary_config.png';
        case 'WORKER':          return 'canary_worker.png';
        case 'FTP':             return 'canary_ftp.png';
        case 'MESSAGE_QUEUE':   return 'canary_queue.png';
        case 'MESSAGE_CHANNEL': return 'canary_channel.png';
        case 'STREAM':          return 'canary_stream.png';
        case 'HTTP_RESOURCE':   return 'canary_http.png';
    }
    return 'canary_resource.png';
}

function fakegraph() {

}

function loadFakeGraph() {
    nodes = [
        {id: '1',  shape: 'circularImage', image: IMG_SERVICE},
        {id: '2',  shape: 'circularImage', image: IMG_SERVICE},
        {id: '3',  shape: 'circularImage', image: IMG_SERVICE},
        {id: '4',  shape: 'circularImage', image: IMG_SERVICE, label:"pictures by this guy!"},
        {id: '5',  shape: 'circularImage', image: IMG_SERVICE}
        // {id: 6,  shape: 'circularImage', image: IMG_SERVICE},
        // {id: 7,  shape: 'circularImage', image: IMG_SERVICE},
        // {id: 8,  shape: 'circularImage', image: IMG_SERVICE},
        // {id: 9,  shape: 'circularImage', image: IMG_SERVICE},
        // {id: 10, shape: 'circularImage', image: IMG_SERVICE},
        // {id: 11, shape: 'circularImage', image: IMG_SERVICE},
        // {id: 12, shape: 'circularImage', image: IMG_SERVICE},
        // {id: 13, shape: 'circularImage', image: IMG_SERVICE},
        // {id: 14, shape: 'circularImage', image: IMG_SERVICE},
        // {id: 15, shape: 'circularImage', image: IMG_SERVICE, label:"when images\nfail\nto load"},
        // {id: 16, shape: 'circularImage', image: IMG_SERVICE, label:"fallback image in action"}
    ];

    // create connections between people
    // value corresponds with the amount of contact between two people
    edges = [
        {from: '1', to: '2'},
        {from: '2', to: '3'},
        {from: '2', to: '4'},
        {from: '4', to: '5'}
        // {from: 4, to: 10},
        // {from: 4, to: 6},
        // {from: 6, to: 7},
        // {from: 7, to: 8},
        // {from: 8, to: 9},
        // {from: 8, to: 10},
        // {from: 10, to: 11},
        // {from: 11, to: 12},
        // {from: 12, to: 13},
        // {from: 13, to: 14},
        // {from: 9, to: 16}
    ];

    // create a network
    var container = document.getElementById('graph');
    var data = {
        nodes: nodes,
        edges: edges
    };
    var options = {
        nodes: {
            borderWidth:4,
            size:30,
            color: {
                border: '#222222',
                background: '#666666'
            },
            font:{color:'#eeeeee'}
        },
        edges: {
            color: 'lightgray'
        }
    };

    network = new vis.Network(container, data, options);
}