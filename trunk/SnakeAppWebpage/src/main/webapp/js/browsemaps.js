$(document).ready(function(){
    
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
        showMapDialog: function(actualDiv){
            
            $('#mapView').insertAfter(actualDiv)
            
            $('#mapView').css({ 
                "visibility": "visible",
                "position": "absolute",
                "z-index": "2",
                "background-image": "url(img/mapDetailsBg.PNG)",
                "background-color": "white",
                "background-repeat": "repeat-x",
                "border": "2px solid black",
                "margin-left": "150px"
            }).load('mapdetails.xhtml')
        },
        
        hide: function(){
            $('#mapView').css({
                "visibility": "hidden"
            })
        }
        
    }
}();

