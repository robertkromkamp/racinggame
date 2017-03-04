var carid;

$(document).ready(function () {

    $("#register-form").submit(function (event) {
    	//stop submit the form, we will post it manually.
        event.preventDefault();
        fire_register_submit();

    });
    
    
    $("#start-form").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();
        fire_start_submit();

    });
    

    $("#navigation-form").submit(function (event) {

        var driveraction = $(document.activeElement).val();
        //stop submit the form, we will post it manually.
        event.preventDefault();
        fire_navigation_submit(driveraction);
        
    });
    autoRefresh_div();

});


function autoRefresh_div() {
	$("#carsBlock").load("/carsandracestatus", function() {
        setTimeout(autoRefresh_div, 1000);
    });
	
	
	var modelAttributeValue = '${cars}';
	alert(customObjectList.typeof);
}


function fire_register_submit() {
    var register = {}
    register["drivername"] = $("#drivername").val();
    
    $("#bth-register").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/register",
        data: JSON.stringify(register),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            carid = data.id;
            $('#feedback').html("");
            console.log("SUCCESS : ", data);
            $("#bth-register").prop("disabled", false);
            $("#carsBlock").load("/carsandracestatus");
            $('#registrationform').hide();
            $('#startform').show();

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#bth-register").prop("disabled", false);
            $("#carsBlock").load("/carsandracestatus");

        }
    });
}


function fire_start_submit() {
    $("#btn-start").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/startrace",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {


            $('#feedback').html("");
            console.log("SUCCESS : ", data);
            $("#bth-register").prop("disabled", false);
            $("#carsBlock").load("/carsandracestatus");
            $('#registrationform').hide();
            $('#startform').hide();
            $('#navigationform').show();

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#bth-search").prop("disabled", false);
            $("#carsBlock").load("/carsandracestatus");

        }
    });

}


function fire_navigation_submit(action) {
	
    $("#btn-start").prop("disabled", true);
    
    var driveraction = {}
    driveraction["carId"] = carid;
    driveraction["action"] = action;
        
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/navigate",
        data: JSON.stringify(driveraction),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
        	$('#feedback').html("");
        	console.log("SUCCESS : ", data);
            $("#bth-register").prop("disabled", false);
            $("#carsBlock").load("/carsandracestatus");
            $('#registrationform').hide();
            $('#startform').hide();
            $('#navigationform').show();

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#bth-search").prop("disabled", false);
            $("#carsBlock").load("/carsandracestatus");

        }
    });

}