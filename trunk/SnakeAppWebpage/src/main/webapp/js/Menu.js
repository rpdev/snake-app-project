$(document).ready(function(){
    // Try div.hover or div#divMain if not working 
    $("#homebutton2").hover(
        function() {
            enter();
        }, 
        function() {
            leave();
        })
})


function enter(){
    alert("enter");
}

function leave(){
    alert("leave");
}