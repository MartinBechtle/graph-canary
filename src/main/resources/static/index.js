const GRAPH_URL = "graph";

$(document).ready(function() {

    $('#loadgraph').click(loadGraph);
});

function loadGraph() {

    $.ajax({
        url: GRAPH_URL,
        data: data,
        success: success,
        dataType: dataType
    });
}