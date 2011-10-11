$(document).ready(function(){
    
    $('div.mapName').live({
        mouseover: function(){
            mapView.showMapDialog();
        },
        mouseout: function(){
            mapView.hide();
        }
    });

})

var mapView = function(){
    return{
        showMapDialog: function(){
            
            $('#mapView').css({ 
                "visibility": "visible",
                "position": "absolute",
                "z-index": "2",
                "background": "lightgreen",
                "border": "2px solid black"  
            }).load('mapdetails.xhtml')
        },
        
        hide: function(){
            $('#mapView').css({
                "visibility": "hidden"
            })
        }
        
    }
}();

