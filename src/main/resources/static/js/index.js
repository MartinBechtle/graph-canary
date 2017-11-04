const GRAPH_URL = "graph";

$(document).ready(function () {

    $('#toggle-hierarchical').click(toggleHierarchical);
    loadFakeGraph();
});

// function loadGraph() {
//
//     $.get({
//         url: GRAPH_URL,
//         success: onLoadGraphSuccess
//     });
// }

// function onLoadGraphSuccess() {
//
//     loadFakeGraph();
// }

var network = null;
var hierarchical = true;

function loadFakeGraph() {

    var container = document.getElementById('graph');
    network = buildGraph(fakeGraph, container);
}

function toggleHierarchical() {

    if (hierarchical) {
        graphOptions.layout.hierarchical.enabled = false;
        hierarchical = false;
    }
    else {
        graphOptions.layout.hierarchical.enabled = true;
        hierarchical = true;
    }
    loadFakeGraph();
}