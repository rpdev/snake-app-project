$(document).ready(function(){
    // Try div.hover or div#divMain if not working 
    var MAX_STARS = 5;
    var count = 0;
    $('img.star').each(
        function(){
            $(this).attr('id', '' + count++);
        }
        )
    $('img.star').live({
        mouseover: function() {
            var element = $(this).attr('id');
            var id = +element;
            var startFrom = id - (id % MAX_STARS);
            var count = 0;
            $('img.star').each(
                function(){
                    if(count >= startFrom){
                        var src = "img/star_yellow.png";
                        $(this).attr("src", src);
                        if($(this).attr('id') == element){
                            return false;
                        }
                    }
                    count++;
                });
        },
        mouseout: function() {
            $('img.star').each(
                function(){
                    var src = "img/star_gray.png";
                    $(this).attr("src", src);
                });
        }        
    })
    
})