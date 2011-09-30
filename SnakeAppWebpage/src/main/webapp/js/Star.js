$(document).ready(function(){
    // Try div.hover or div#divMain if not working 
    $("#star1").hover(
    function() {
        setStars(1)            
    }, 
    function() {
        clearStars();
    }),
    $("#star2").hover(
    function() {
        setStars(2)            
    }, 
    function() {
        clearStars();
    }),
    $("#star3").hover(
    function() {
        setStars(3)            
    }, 
    function() {
        clearStars();
    }),
    $("#star4").hover(
    function() {
        setStars(4)            
    }, 
    function() {
        clearStars();
    }),
    $("#star5").hover(
    function() {
        setStars(5)            
    }, 
    function() {
        clearStars();
    })
})

function setStars(i){
    for(k = 1; k <= i; k++){
        var src = "img/star_yellow.png";
        var namn = "#star"+k;
        $(namn).attr("src", src);
    }
}

function clearStars(){
    for(k = 1; k <= 5; k++){
        var src = "img/star_gray.png";
        var namn = "#star"+k;
        $(namn).attr("src", src);
    }
}