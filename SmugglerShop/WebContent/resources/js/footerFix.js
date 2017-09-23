$(document).ready(function(){
	
	fixFooter();
	setTimeout(function() { //sometimes it is acting weird
		fixFooter();
	}, 1000);
	
	$('.showDetail').parent().parent().parent().children('.panel-container').hide();
	
	$('.showDetail').click(function() {
		var a = $(this).children('.fa'),
			elm = $(this).parent().parent().parent();
		
		if(a.hasClass('fa-minus')){
			a.removeClass('fa-minus');
			a.addClass('fa-plus');

			elm.children('.panel-container').slideToggle();
		}
		else {
			a.removeClass('fa-plus');
			a.addClass('fa-minus');

			elm.children('.panel-container').slideToggle();
		}
		
	});
});

function fixFooter(){
	var wH = $(window).height(), bH = $('html').height(), hH = $('#footerPart').height();


	// fixes the positioning for the footer
	$('#footerPart').css('top', ((wH < bH ? bH : wH))+'px');
}

function upload(callback){
    var img = $('#pages').get(0).files[0];
    var reader = new FileReader();

    reader.onload = function(e){
        uploadImage(e, callback);
    }
    reader.readAsDataURL(img);
}

function uploadImage(page, callback){
    var iurl = page.target.result.substr(page.target.result.indexOf(",") + 1, page.target.result.length);
    var clientId = '98fb764612f62b2';
    $.ajax({
        url: "https://api.imgur.com/3/upload",
        type: "POST",
        datatype: "json",
        data: {
            'image': iurl,
            'type': 'base64'
        },
        success: function(data){
            var link = data.data.link;
            callback(link);

        },//calling function which displays url
        error: function(err){
            //error handleing
        	console.log(err);
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Client-ID " + clientId);
        }
    });
}