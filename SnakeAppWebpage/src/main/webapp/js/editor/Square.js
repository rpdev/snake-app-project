var DIR = {
    NW: {x:-1, y: -1, s: "NW"},     N: {x:0, y:-1, s: "N"},     NE: {x: 1, y:-1, s: "NE"},
    W: {x: -1, y: 0, s: "W"},                                   E: {x: 1, y: 0, s: "E"},
    SW: {x: -1, y: 1, s: "SW"},     S: {x: 0, y: 1, s: "S"},    SE: {x: 1, y: 1, s: "SE"}
};

/**
 * Constructor for square
 */
function createSquare(cell, radius, x, y){
    this.x = x;
    this.y = y;
    this.square = this;
    this.cell = cell;
    this.radius = radius;
    var neighbors = {};
    var FILLED = {NOT_FILLED: 0, FILLED: 1, MARKED: 2, SNAKE: 3};
    var filled = FILLED.NOT_FILLED;
    
    return {
        linkNeighbors: function(neighbor, dir){
            var key = dir.s;
            neighbors[key] = neighbor;
            neighbor.setNeighbours(this, getOpposit(dir));
        },
    
        setNeighbours: function(neighbor, dir){
            var key = dir.s;
            neighbors[key] = neighbor;
        },
    
        click_function: function (){
            if(filled == FILLED.FILLED){
                filled = FILLED.NOT_FILLED;
                $(cell).css("backgroundColor", "#00FF00");
            }else if(filled == FILLED.NOT_FILLED){
                filled = FILLED.FILLED;
                clicked_cell = self;
                controlDrawLine();
                $(cell).css("backgroundColor", "#FF0000");
                drawLine(DIR.S);
            }
            
        },
        
        drawLine: function(dir){
            var square = neighbors[dir];
            if(typeof square != "undefined"){
                $(cell).css("backgroundColor", "#00FF00");
                square.drawLine(dir);
            }
        },
    
        /**
         * Get the direction opposit one direction.
         */
        getOpposit: function(dir){
            if(dir == DIR.NW)
                return DIR.SE;
            else if(dir == DIR.N)
                return DIR.S;
            else if(dir == DIR.NE)
                return DIR.SW;
            else if(dir == DIR.W)
                return DIR.E;
            else if(dir == DIR.E)
                return DIR.W;
            else if(dir == DIR.SW)
                return DIR.NE;
            else if(dir == DIR.S)
                return DIR.N;
            else if(dir == SE)
                return DIR.NW;
            return null;
        }
    }
}

