$(document).ready(function () {

    $('#toggle-hierarchical').click(toggleHierarchical);

    $('#graph-filters').on('click', 'li', function () {
        const parentId = $(this).parents('.select').attr('id');
        const parent = $('#' + parentId + ' .selected');
        const text = $(this).children().text();
        parent.text(text);
        filter(text);
    });

    heartBeat();
});

$(document).click(function () {
    lastInteraction = Date.now();
});

var lastSuccessfulPull = 0;
var lastErrorOnPull = 0;
var lastInteraction = 0;

/**
 * will not refresh graph if this time hasn't passed since last user interaction, as it might interfere with the user
 * trying to interact with the graph
 */
const postInteractionDelayMs = 5000;

/**
 * delay before one successful data pull and graph reload, and the next one
 */
const graphRefreshIntervalMs = 10000;

/**
 * how long to wait before retrying after an unsuccessful data pull or exception
 */
const retryOnErrorIntervalMs = 10000;

const heartBeatIntervalMs = 1000;

function heartBeat() {

    try {
        if (lastSuccessfulPull < Date.now() - graphRefreshIntervalMs &&
            lastErrorOnPull < Date.now() - retryOnErrorIntervalMs &&
            lastInteraction < Date.now() - postInteractionDelayMs) {

            loadGraph();
            lastSuccessfulPull = Date.now();
        }
    }
    catch (error) {
        lastErrorOnPull = Date.now();
        console.log(error);
    }
    setTimeout(heartBeat, heartBeatIntervalMs);
}

var graph = null;
var hierarchical = true;

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

    $.get({
        url: 'graph',

        success: function (data) {

            const container = document.getElementById('graph');
            graph = buildGraph(data.graph, container);
            updateFilters();
            updateHeader(data);
            updateWarnings(data);
        }
    });
}

const TOP_BANNER_HEALTHY_CLASS = 'alert-success';
const TOP_BANNER_DEGRADED_CLASS = 'alert-warning';
const TOP_BANNER_CRITICAL_CLASS = 'alert-danger';
const TOP_BANNER_UNKNOWN_CLASS = 'alert-warning';
const TOP_BANNER_HEALTHY_TEXT = 'System is healthy.';
const TOP_BANNER_DEGRADED_TEXT = 'System is degraded.';
const TOP_BANNER_CRITICAL_TEXT = 'System state is critical.';
const TOP_BANNER_UNKNOWN_TEXT = 'System state is unknown.';

function updateHeader(canaryData) {

    switch (canaryData.status) {
        case 'HEALTHY':
            setTopBannerClass(TOP_BANNER_HEALTHY_CLASS);
            setTopBannerDesc(TOP_BANNER_HEALTHY_TEXT);
            break;
        case 'DEGRADED':
            setTopBannerClass(TOP_BANNER_DEGRADED_CLASS);
            setTopBannerDesc(TOP_BANNER_DEGRADED_TEXT);
            break;
        case 'CRITICAL':
            setTopBannerClass(TOP_BANNER_CRITICAL_CLASS);
            setTopBannerDesc(TOP_BANNER_CRITICAL_TEXT);
            break;
        default:
            setTopBannerClass(TOP_BANNER_UNKNOWN_CLASS);
            setTopBannerDesc(TOP_BANNER_UNKNOWN_TEXT);
            break;
    }
}

function updateWarnings(canaryData) {

    const warningsList = $('#warnings-list');
    const unhealthyList = $('#unhealthy-list');
    warningsList.html("");
    unhealthyList.html("");

    const failedCanaries = canaryData.failedCanaries;
    if (failedCanaries && failedCanaries.length && failedCanaries.length > 0) {

        const warningDivs = failedCanaries.map(function (failedCanary) {
            const errMsg = "Failed to retrieve from " + failedCanary.serviceName +
                ". Reason: " + failedCanary.canaryResult + ".";
            return '<div class="alert alert-danger">' + errMsg + '</div>';

        });
        warningsList.html(warningDivs.join(""));
    }

    const graph = canaryData.graph;
    if (graph && graph.edges && graph.edges.length && graph.edges.length > 0) {

        const unhealthyDependencyDivs = graph.edges
            .filter(function (edge) {
                return edge.dependencyStatus && edge.dependencyStatus !== 'HEALTHY';
            })
            .map(function (edge) {
                const alertClass = "alert " + (edge.dependencyStatus === 'CRITICAL' ? 'alert-danger' : 'alert-warning');
                const errMsg = edge.from + " from " + edge.to + " is " + edge.dependencyStatus;
                return '<div class="' + alertClass + '">' + errMsg + '</div>';
            });
        unhealthyList.html(unhealthyDependencyDivs.join(""));
    }

}

function setTopBannerClass(classToSet) {

    const topBanner = $('#top-banner');
    topBanner.removeClass(TOP_BANNER_CRITICAL_CLASS);
    topBanner.removeClass(TOP_BANNER_HEALTHY_CLASS);
    topBanner.removeClass(TOP_BANNER_DEGRADED_CLASS);
    topBanner.addClass(classToSet);
}

function setTopBannerDesc(text) {

    $('#top-banner-desc').text(text);
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