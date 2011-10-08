$(document).ready(function(){ 
    $.ajax({
        type: "GET",
        url: "./Login",
        data: "action=getForm",
        dataType: "xml",
        success: function(xml) {
            var userName = $(xml).find('userName').text();
            if(userName == ''){
                $('#login').load('login/login.ihtml');
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
    
    
    // Try div.hover or div#divMain if not working 
    $("#homebutton").hover(
    function() {
        var src = "img/menu/homebutton_hover.png";
        $(this).attr("src", src);
    }, 
    function() {
        var src = "img/menu/homebutton_normal.png";
        $(this).attr("src", src);
    }),
    $("#editorbutton").hover(
    function() {
        var src = "img/menu/editorbutton_hover.png";
        $(this).attr("src", src);
    }, 
    function() {
        var src = "img/menu/editorbutton_normal.png";
        $(this).attr("src", src);
    }),
    $("#downloadbutton").hover(
    function() {
        var src = "img/menu/downloadbutton_hover.png";
        $(this).attr("src", src);
    }, 
    function() {
        var src = "img/menu/downloadbutton_normal.png";
        $(this).attr("src", src);
    }),
    $("#faqbutton").hover(
    function() {
        var src = "img/menu/faqbutton_hover.png";
        $(this).attr("src", src);
    }, 
    function() {
        var src = "img/menu/faqbutton_normal.png";
        $(this).attr("src", src);
    }),
    $("#browsemapsbutton").hover(
    function() {
        var src = "img/menu/browsemapsbutton_hover.png";
        $(this).attr("src", src);
    }, 
    function() {
        var src = "img/menu/browsemapsbutton_normal.png";
        $(this).attr("src", src);
    }),
    $('#bitethis').click(function() {
        alert('TROLOLOLOLOLOLOLOLOLOLO');
    });
})