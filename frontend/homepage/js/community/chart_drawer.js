// documentation: http://www.chartjs.org/docs/#doughnut-pie-chart

var canvasDrawer = $("#myChart");

var myPieChart = new Chart(canvasDrawer, {
    type: 'pie',
    data: {
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
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                }
            }]
        }
    }
});