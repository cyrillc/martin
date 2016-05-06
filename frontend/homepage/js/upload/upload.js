
$(document).ready(function () {
    

// upload
$('#upload_btn').on('click', uploadFile);

// Catch the form submit and upload the files
function uploadFile(event) {
	event.stopPropagation(); // Stop stuff happening
    event.preventDefault(); // Totally stop stuff happening
    
    var formData = new FormData($("#upload_form").get(0));
    
    // create request URL from current URL
    var backendUrl = createRequestURL(frontendUrl, backendPort, "upload");
    
    $.ajax({
        url: backendUrl,  
        type: 'POST',
        data: formData,
        success:function (serverResponse) {
            $("#response").html(serverResponse)
        },
        cache: false,
        contentType: false,
        processData: false
    });
}
});

   