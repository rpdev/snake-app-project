$(document).ready(function(){
    $('#mapView').css({
        "visibility": "hidden"
    })
    
    $('div.mapName').live({
        mouseover: function(){
            mapView.showMapDialog($(this));
        },
        mouseout: function(){
            mapView.hide();
        }
    });

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

