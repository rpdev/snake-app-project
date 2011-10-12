$(document).ready(function(){
    
    $('#login').css('position', 'absolute');
    $.ajax({
        type: "GET",
        url: "./Login",
        data: "action=getForm",
        dataType: "xml",
        success: function(user) {
            var userName = $(user).find('userName').text();
            if(userName == ''){
                $('#login').load('ihtml/Login.ihtml');
            } else {
                $('#login').append("" +
                    "<form action=\"Login\" method=\"POST\">" +
                    "<p>" +
                    "Logged in as " + userName +
                    "<br/>" +
                    "<input type=\"hidden\" name=\"action\" value=\"logout\"/>" +
                    "<input type=\"submit\" value=\"Logout\" class=\"buttonstyle\" />" +
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
    
    $('#Register').live('click', function(){
        window.location="register.jsf";
    });
    
    $('.buttonstyle').live({
        mouseover: function(){
            $(this).css({
                "background-color" : "#00CC33"
            })
        },
        mouseout: function(){
            $(this).css({
                "background-color" : "#006600"
            })
        }
        
    });
    
})