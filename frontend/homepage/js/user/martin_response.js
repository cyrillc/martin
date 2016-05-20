function MartinResponseRenderer() {
}

MartinResponseRenderer.prototype.renderResponse = function (martinStatement, timingChart) {
    // for some fun
    if (martinStatement.request.command == "EASTEREGG") {
        $('#main').toggleClass("EASTEREGG");
        return;
    }

    if(timingChart){
        martinStatement.response.responses.push({"type": "heading", "value": "Timing-Information"})
    }

    // Render View
    nunjucks.configure("/views")
    var response_html = $(nunjucks.render("martin-statement.html", {
        request:        martinStatement.request, 
        response:       martinStatement.response, 
        timingChart:    timingChart
    }));

    $("#martin-responses").prepend(response_html);

    if(timingChart){
        // Render the timing-chart  
        this.renderTimingChart(martinStatement.response.timingInfo, response_html.find(".timing-chart"));
    }
   
    $(response_html).slideDown("fast"); 
};


MartinResponseRenderer.prototype.renderTimingChart = function(timingInfo, el){
    var timingChartRenderer = new TimingChartRenderer();
    console.log(el);
    try {
        timingChartRenderer.renderTimingChart(timingInfo, el);

    } catch(error){
        console.log(error);
    }
}