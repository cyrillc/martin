var canvasDrawer;
// documentation: http://www.chartjs.org/docs/#doughnut-pie-chart

var createTimingChart = function (chartData) {
    new Chart(canvasDrawer, {
        type: 'pie',
        data: chartData,
        /*    data: {
                labels: ["AIController", "SignalProcessor", "I/O", "ModuleLibrary", "PerpherialManager"],
                datasets: [{
                    label: '# of Secondes',
                    data: [12, 19, 3, 5, 2],
                    backgroundColor: [
                        "#FF6384",
                        "#4BC0C0",
                        "#FFCE56",
                        "#E7E9ED",
                        "#36A2EB"
                    ],
                }]
            },*/

    });

};


function TimingChartRenderer() { }

TimingChartRenderer.prototype.renderTimingChart = function (timingInfo) {
    canvasDrawer = $('<canvas id="timingChart">');
    var chartData = {
        labels: [],
        datasets: [{
            //label: '# of milliseconds',
            data: [],
            backgroundColor: [
                "#FF6384",
                "#4BC0C0",
                "#FFCE56",
                "#E7E9ED",
                "#36A2EB",
                "#FF6384",
                "#4BC0C0",
                "#FFCE56",
                "#E7E9ED",
                "#36A2EB"
            ],
        }],

    };

    timingInfo.forEach(function (element) {
        // create labels
        if (chartData.labels.indexOf(element.label) == -1) {
            chartData.labels.push(element.label);
            chartData.datasets[0].data.push(0);
        }

        // insert data
        var dataPosition = chartData.labels.indexOf(element.label);
        chartData.datasets[0].data[dataPosition] += (element.endTime - element.startTime);
    }, this);

    $('#timingContainer').append(canvasDrawer);
    createTimingChart(chartData);
};
