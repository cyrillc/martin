
$(document).ready(function () {
    
// Prepare upload
    
// Variable to store your files
var myfile;
// Add events
$('input[type=file]').on('change', prepareUpload);
// Grab the files and set them to our variable
function prepareUpload(event) {
  myfile = event.target.files[0];
}


// upload
$('form').on('submit', uploadFile);

// Catch the form submit and upload the files
function uploadFile(event) {
  event.stopPropagation(); // Stop stuff happening
    event.preventDefault(); // Totally stop stuff happening

    // START A LOADING SPINNER HERE

    // Create a formdata object and add the files
    var formdata = new FormData();
        formdata.append('file', myfile);
    
    // create request URL from current URL
    var backendUrl = createRequestURL(frontendUrl, backendPort, "upload");
    
    //send data to backend
    $.post(backendUrl,formdata, function (serverResponse) {
            $("#response").append("<p>" + serverResponse + "</p>");
     });

}
});

   