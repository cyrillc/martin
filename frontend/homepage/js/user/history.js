
function HistoryRenderer(history) {
	this.history = history;
}

HistoryRenderer.prototype.renderDate = function (itemDate) {
	var date = $('<td></td>');
	dateObj = new Date(itemDate);
	date.append(dateObj.getDate() + '.' + (dateObj.getMonth() + 1) + '.' + dateObj.getFullYear());
	return date;
};

HistoryRenderer.prototype.renderRequest = function (itemRequest) {
	var request = $('<td></td>');
	request.append(itemRequest.command);
	return request;
};

HistoryRenderer.prototype.renderResponse = function (itemResponse) {
	var response = $('<td></td>');
	response.append(itemResponse.responseText);
	return response;
};

HistoryRenderer.prototype.renderItem = function (item) {
	// Appends every part of the items
	var html = $('<tr></tr>');
	html.append(this.renderDate(item.date)).append(this.renderRequest(item.request)).append(this.renderResponse(item.response));
	$('#historyItems').prepend(html);
};

// Renders all part of a Historyitem.
HistoryRenderer.prototype.renderAll = function () {
	var renderer = this;
	this.history.forEach(function (item) {
		renderer.renderItem(item);
	});
};

