$(document).ready(function(){ 
    $('#login').css('position', 'absolute');
    $.ajax({
        type: "GET",
        url: "./Login",
        data: "action=getForm",
        dataType: "xml",
        success: function(xml) {
            var userName = $(xml).find('userName').text();
            if(userName == ''){
                $('#login').load('login/login.ihtml');
                $('#login').css('position', 'absolute');
            } else {
                $('#login').append("" +
                    "<p>" +
                    "<form action=\"Login\" method=\"POST\">" +
                    "Välkommen: " + userName +
                    "<br/>" +
                    "<input type=\"hidden\" name=\"action\" value=\"logout\"/>" +
                    "<input type=\"submit\" value=\"Logout\" />" +
                    "</form>" +
                    "</p>");
            }
        }
    });
    
    $('ul.links img').live({
        mouseover: function() {
            var src = "img/menu/" + $(this).attr('id') + "_hover.png";
            $(this).attr("src", src);
        },
        mouseout: function() {
            var src = "img/menu/" + $(this).attr('id') + "_normal.png";
            $(this).attr("src", src);
        }
    });
    $('#bitethis').click(function() {
        alert('TROLOLOLOLOLOLOLOLOLOLO');
    });
})