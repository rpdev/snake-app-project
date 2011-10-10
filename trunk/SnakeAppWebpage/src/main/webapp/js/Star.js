$(document).ready(function(){

    function clearStars(){
        for(k = 1; k <= 5; k++){
            var src = "img/star_gray.png";
            var namn = "#star"+k;
            $(namn).attr("src", src);
        }
    }
})