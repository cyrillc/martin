// MartinResponseRenderer singleton
var MartinResponseRenderer = {
    timingChartRenderer: null,
    init: function(){
        timingChartRenderer = new TimingChartRenderer();
    },

    renderEvent: function(event, direction = "prepend", animation_duration = 400){
        // Date Formatting
        event.request.formattedDate = moment.unix(event.request.createdAt/1000).fromNow();
        event.response.formattedDate = moment.unix(event.response.createdAt/1000).fromNow();


         // for some fun
        if (event.request.command == "EASTEREGG") {
            $('#main').toggleClass("EASTEREGG");
            return;
        }

        // Render View
        nunjucks.configure("/views")
        var event_html = $(nunjucks.render("event.html", { event: event }));
        if(direction == "prepend"){
             $("#martin-responses").prepend(event_html);
         } else {
             $("#martin-responses").append(event_html);
         }
       

        // Render the timing-chart 
        if(event.request.timed){
            var chart_element = event_html.find(".timing-chart")[0];
            this.renderTimingChart(chart_element);
        }

        // Show the parts
        $(event_html).slideDown({
            duration: animation_duration,
            easing: "easeInOutQuart"
        }); 
    },

    renderTimingChart: function(el){
        var data = $(el).data("value");
        timingChartRenderer.renderTimingChart(data, el);
    }
};