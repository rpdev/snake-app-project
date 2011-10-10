$(document).ready(function(){
    // Try div.hover or div#divMain if not working 
   
    $('div.stars img').live({
        mouseover: function() {
            var element = $(this).attr('id');
            $('div.stars img').each(
                function(){
                    var src = "img/star_yellow.png";
                    $(this).attr("src", src);
                    if($(this).attr('id') == element){
                        return false;
                    }
                });
        },
        mouseout: function() {
            $('div.stars img').each(
                function(){
                    var src = "img/star_gray.png";
                    $(this).attr("src", src);
                });
        }        
    })
    
})