function create(rows, columns, radius){
    var array = new Array(column);
    var squares = {};
    for (i=0; i < array.length; i++)
        array[i] = new Array(rows);
    for(i=0;i < columns; i++){
        // create TR
        for(j=0; j<rows; j++){
            var square = Square.square(radius, j, i);
            var key = j + '_' + i;
            array[i][j] = square;
            squares[key] = square;
            setNeigbours(array, square, j, i);
            // create & add  & close TD
        }
        // close & add TR
    }
    
    function setNeigbours(array, square, row, column){
        dirArray = new Array(DIR.W, DIR.NW, DIR.N, DIR.NE);
        for(dir in dirArray){
            var r = row + dir.x;var c = column + dir.y;
            if(r >= 0 && c >= 0 && r < array[0].length && c < array.length && array[c][r] != null)
                square.linkNeighbors(array[c][r], dir);
        }
    }
}

