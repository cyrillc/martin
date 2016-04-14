
function HistoryRenderer(history){
	this.history = history;
}

HistoryRenderer.prototype.render = function(){
	this.history.forEach(function(item){
		var date = $('<td></td>');
		var request = $('<td></td>');
		var response = $('<td></td>');
		dateObj = new Date(item.date);
		date.append(dateObj.getDate()+'.'+ (dateObj.getMonth()+1) +'.'+dateObj.getFullYear())
		request.append(item.request.command);
		response.append(item.response.content);
		var html = $('<tr></tr>').append(date).append(request).append(response);
		$('#historyItems').append(html);
	}, this);
}