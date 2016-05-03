
$(document).ready(function () {
   
    fs.readFile(req.files.file.path, function (err, data) {
  // ...
  var newPath = __dirname + "/uploads/uploadedFileName";
  fs.writeFile(newPath, data, function (err) {
    res.redirect("back");
  });
});

    
    
});