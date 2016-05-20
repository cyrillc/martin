function MartinResponseRenderer() {
}


MartinResponseRenderer.prototype.renderEvent = function(event){
     // for some fun
    if (event.request.command == "EASTEREGG") {
        $('#main').toggleClass("EASTEREGG");
        return;
    }

    // Render View
    nunjucks.configure("/views")
    var event_html = $(nunjucks.render("event.html", { event: event }));
    $("#martin-responses").prepend(event_html);

    // Render the timing-chart 
    if(event.request.timed){
        var chart_element = event_html.find(".timing-chart")[0];
        this.renderTimingChart(chart_element);
    }

    // Show the parts
    $(event_html).slideDown("fast"); 

}


MartinResponseRenderer.prototype.renderTimingChart = function(el){
    var timingChartRenderer = new TimingChartRenderer();
    var data = $(el).data("value");
    timingChartRenderer.renderTimingChart(data, el);
}