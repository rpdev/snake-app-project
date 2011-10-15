$(document).ready(function(){
    
    var MAX_STARS = 5;
    var count = 0;
    var snakeMapID;
    
    $('#mapView').css({
        "visibility": "hidden"
    })
    
    $('td.commentButtons').live({
        click: function(){
            commentView.showCommentViewDialog($(this));
            snakeMapID = $(this).find('div.buttonstyle').attr('id');
        }
    });
    
    
    $('#commentButton').live({
        click: function(){
            $.ajax({
                type: "POST",
                url: "./editmap",
                data: 'action=comment&id=' + snakeMapID + '&commentString=' + document.getElementById('commentText').value,
                dataType: "xml",
                success: function() {}
            });
            alert('Comment added!');
            document.getElementById('commentText').value = "";
            commentView.hide();
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
    
    $('td.stars img').each(
        function(){
            $(this).attr('id', '' + count++);
        }
        )
        
    $('td.stars img').each(
        function(){
            snakeMapID = $(this).attr('class');
            var star = $(this);
            $.ajax({
                type: "GET",
                url: "./editmap",
                data: 'action=getRating&id=' + snakeMapID,
                dataType: "xml",
                success: function(snakeMap) {
                    var rating = +$(snakeMap).find('mapRating').text();
                    var element = star.attr('id');
                    var id = +element;
                    id += 1;
                    var holeStar = "img/star_yellow.png";
                    var halfStar = "img/halfstar.png";
                    var emptyStar = "img/star_gray.png";
                    if(rating > id){
                        star.attr("src", holeStar);
                    } else if(rating < id && rating > (id - 1)){
                        star.attr("src", halfStar);
                    } else {
                        star.attr("src", emptyStar);
                    }
                }
            });
        }
        )
        
    $('td.stars img').live({
        /*mouseover: function() {
            var element = $(this).attr('id');
            var id = +element;
            var startFrom = id - (id % MAX_STARS);
            var count = 0;
            $('td.stars img').each(
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
            $('td.stars img').each(
                function(){
                    var src = "img/star_gray.png";
                    $(this).attr("src", src);
                });
        },*/
        click: function(){
            var starId = +$(this).attr('id');
            var starValue = (starId % MAX_STARS) + 1;
            snakeMapID = $(this).attr('class');
            mapRating.rateMap(starValue, snakeMapID);
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
                    var comments = snakeMap.getElementsByTagName('comment');
                    mapView.fillSnakeMapDetails(mapName, difficulty, speed, growth, description, comments);
                    MapEditor('innerreviewmap','./editmap?action=get&id='+snakeMapId,false,true);
                }
            });
        },
        fillSnakeMapDetails: function(mapName, difficulty, speed, growth, description, comments){
            $('#mapDetails').empty().append(mapName + "<br />" + difficulty 
                + "<br />" + speed + "<br />" + growth);
            $('#mapDescription').empty().append(description);
            $('#mapComments').empty();
            for(var i = 0; i < comments.length; i++){
                var comment = comments[i].lastChild.nodeValue + '<br/>';
                $('#mapComments').append(comment);
            }
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
                "visibility" : "visible",
                "background-color": "#ACD373"
            })
            $('#commentView').css({ 
                "visibility": "visible",
                "position": "absolute",
                "z-index": "2",
                
                "border": "2px solid black",
                "margin-left": "100px"
            });
        },
        hide: function(){
            $('#commentView').css({
                "visibility" : "hidden"
            })
        }
    }
}();

var mapRating = function(){
    return{
        rateMap: function(mapRating, snakeMapID){
            $.ajax({
                type: "POST",
                url: "./editmap",
                data: 'action=rate&id=' + snakeMapID + '&mapRating=' + mapRating,
                dataType: "xml",
                success: function() {}
            });
        },
        setStarImg: function(starImg, snakeMapID){
        }
    }
}();

