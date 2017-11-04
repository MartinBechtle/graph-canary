$(document).ready(function () {

    $('#toggle-hierarchical').click(toggleHierarchical);

    $('#graph-filters').on('click', 'li', function () {
        const parentId = $(this).parents('.select').attr('id');
        const parent = $('#' + parentId + ' .selected');
        const text = $(this).children().text();
        parent.text(text);
        filter(text);
    });

    loadGraph();
});

var graph = null;
var hierarchical = true;

function loadFakeGraph() {

    var container = document.getElementById('graph');
    graph = buildGraph(fakeGraph, container);
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
    loadGraph();
}

function loadGraph() {

    loadFakeGraph();
    updateFilters();
}

function updateFilters() {

    const services = graph.data.nodes.filter(function (node) {
        return node.image.includes('canary_api.png') // horrible temporary hack
    }).map(function (node) {
        return "<li><a href=\"#\">" + node.id + "</a></li>"
    });

    services.push('<li role="separator" class="divider"></li>');
    services.push('<li><a href="#">Show all</a></li>');

    const filters = services.join("");
    $('#graph-filters').html(filters)
}

function filter(serviceId) {

    if (serviceId === 'Show all') {
        filterOptions.service.name = null;
    } else {
        filterOptions.service.name = serviceId;
    }
    loadGraph();
}