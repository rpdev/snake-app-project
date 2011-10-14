$(document).ready(function(){
    
    var MAX_STARS = 5;
    var count = 0;
    var snakeMapID;
    
    $('#mapView').css({
        "visibility": "hidden"
    })
    
    $('div.commentButtons').live({
        click: function(){
            commentView.showCommentViewDialog($(this));
            snakeMapID = $(this).attr('id');
        }
    });
    
    
    $('#commentButton').live({
        click: function(){
            $.ajax({
                type: "POST",
                url: "./editmap",
                data: 'action=comment&id=' + snakeMapID + 'commentString=' + document.getElementById('commentText').value,
                dataType: "xml",
                success: function() {}
            });
        }
    });
    
    $('#cancelCommentButton').live({
        click: function(){
            commentView.hide();
        }
    });
    
    $('td.mapName').live({
        mouseover: function(){
            mapView.showMapDialog($(this).find("div.mapNameDiv"));
        },
        mouseout: function(){
            mapView.hide();
        }
    });
    
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

var mapView = function(){
    return{
        showMapDialog: function(currentRow){
            var currentSnakeMapId = currentRow.attr('id');
            $('#mapView').insertAfter(currentRow)
            $('#mapView').css({ 
                "visibility": "visible",
                "position": "absolute",
                "z-index": "2",
                "background-image": "url(img/mapDetailsBg.PNG)",
                "background-color": "white",
                "background-repeat": "repeat-x",
                "border": "2px solid black",
                "margin-left": "100px"
            });
            mapView.setSnakeMapDetails(currentSnakeMapId);
        },
        
        hide: function(){
            $('#mapView').css({
                "visibility": "hidden"
            })
        },
        setSnakeMapDetails: function(snakeMapId){
            $.ajax({
                type: "GET",
                url: "./editmap",
                data: 'action=preview&id=' + snakeMapId,
                dataType: "xml",
                success: function(snakeMap) {
                    var mapName = $(snakeMap).find('mapName').text();
                    var difficulty = $(snakeMap).find('difficulty').text();
                    var speed = $(snakeMap).find('speed').text();
                    var growth = $(snakeMap).find('growth').text();
                    var description = $(snakeMap).find('description').text();
                    mapView.fillSnakeMapDetails(mapName, difficulty, speed, growth, description);
                    MapEditor('innerreviewmap','./editmap?action=get&id='+snakeMapId,false,true);
                }
            });
        },
        fillSnakeMapDetails: function(mapName, difficulty, speed, growth, description){
            $('#mapDetails').empty().append(mapName + "<br />" + difficulty 
                + "<br />" + speed + "<br />" + growth);
            $('#mapDescription').empty().append(description);
        }
        
    }
}();

var commentView = function(){
    return{
        showCommentViewDialog: function(commentButton){
            $('#commentView').insertAfter(commentButton);
            $('#commentView').css({
                "border": "2px solid black",
                "position": "absolute",
                "z-index": "2",
                "margin-left": "100px",
                "visibility" : "visible"
            })
        },
        hide: function(){
            $('#commentView').css({
                "visibility" : "hidden"
            })
        }
    }
}();

