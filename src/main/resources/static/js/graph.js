const filterOptions = {
    service: {
        name: null
    }
};

const graphOptions = {
    nodes: {
        borderWidth: 4,
        size: 30,
        color: {
            border: '#222222',
            background: '#666666'
        },
        font: {
            color: 'black',
            size: 20
        }
    },
    edges: {
        color: 'lightgray',
        width: 2
    },
    layout: {
        randomSeed: 0,
        hierarchical: {
            enabled: true
        }
    }
};

function buildGraph(graph, container) {

    const data = canaryGraphToVisGraph(graph);

    const network = new vis.Network(container, data, graphOptions);

    return {
        network: network,
        data: data
    }
}

const IMG_DIR = 'img/dependencies/';

function typeToImage(type) {

    switch (type) {

        case 'API':
            return 'canary_api.png';
        case 'DATABASE':
            return 'canary_db.png';
        case 'CACHE':
            return 'canary_cache.png';
        case 'STORAGE':
            return 'canary_storage.png';
        case 'CONFIGURATION':
            return 'canary_config.png';
        case 'WORKER':
            return 'canary_worker.png';
        case 'FTP':
            return 'canary_ftp.png';
        case 'MESSAGE_QUEUE':
            return 'canary_queue.png';
        case 'MESSAGE_CHANNEL':
            return 'canary_channel.png';
        case 'STREAM':
            return 'canary_stream.png';
        case 'HTTP_RESOURCE':
            return 'canary_http.png';
    }
    return 'canary_resource.png';
}

/**
 * Converts a canary graph into a representation suitable for the visjs library
 *
 * @param canaryGraph
 */
function canaryGraphToVisGraph(canaryGraph) {

    const nodes = canaryGraph.nodes.filter(function (node) {

        if (filterOptions && filterOptions.service && filterOptions.service.name) {
            return isRelatedTo(node, filterOptions.service.name, canaryGraph);
        }
        return true;

    }).map(function (node) {

        const label = node.name;

        return {
            id: node.name,
            label: label,
            shape: 'circularImage',
            image: IMG_DIR + typeToImage(node.type)
        }
    });

    const edges = canaryGraph.edges.map(function (edge) {

        const status = edge.dependencyStatus;

        const color = status === 'HEALTHY' || status === 'UNKNOWN' ?
            '#3fc435' :
            '#ff000b';

        const dashes = status === 'CRITICAL' || status === 'UNKNOWN';

        return {
            color: color,
            dashes: dashes,
            from: edge.from,
            to: edge.to
        }
    });

    return {
        nodes: nodes,
        edges: edges
    };
}

/**
 * @true if node's name corresponds to nodeName or is related to a node with such name
 */
function isRelatedTo(node, nodeName, canaryGraph) {

    if (nodeName === node.name) {
        return true;
    }
    for (var i = 0; i < canaryGraph.edges.length; i++) {

        const edge = canaryGraph.edges[i];
        if ((edge.from === node.name && edge.to === nodeName) ||
            (edge.from === nodeName && edge.to === node.name)) {
            return true;
        }
    }
}