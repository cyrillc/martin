
function HistoryRenderer(history) {
	this.history = history;
}

HistoryRenderer.prototype.renderDate = function (item) {
	var date = $('<td></td>');
	dateObj = new Date(item.date);
	date.append(dateObj.getDate() + '.' + (dateObj.getMonth() + 1) + '.' + dateObj.getFullYear());
	return date;
};

HistoryRenderer.prototype.renderRequest = function (item) {
	var request = $('<td></td>');
	request.append(item.request.command);
	return request;
};

HistoryRenderer.prototype.renderResponse = function (item) {
	var response = $('<td></td>');
	response.append(item.response.content);
	return response;
};

HistoryRenderer.prototype.renderItem = function (item) {
	// Appends every part of the items
	var html = $('<tr></tr>').append(this.renderDate(item)).append(this.renderRequest(item)).append(this.renderResponse(item));
	$('#historyItems').prepend(html);
};

// Renders all part of a Historyitem.
HistoryRenderer.prototype.renderAll = function () {
	this.history.forEach(function (item) {
		HistoryRenderer.prototype.renderItem(item);
	});
};

