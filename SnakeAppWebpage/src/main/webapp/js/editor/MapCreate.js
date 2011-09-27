function create(rows, columns, radius){
    var array = new Array(columns);
    var squares = {};
    for (i=0; i < array.length; i++)
        array[i] = new Array(rows);
    
    
    for(i=0;i < columns; i++){
        // create TR
        var tr = $("<tr></tr>");
        for(j=0; j<rows; j++){
            var tdInner = $("<td></td>");
            $(tdInner).css("width",radius*2+"px")
            $(tdInner).css("height",radius*2+"px")
            var square = createSquare(tdInner, radius, j, i);
            var key = j + '_' + i;
            array[i][j] = square;
            squares[key] = square;
            setNeigbours(array, square, j, i);
            // create & add  & close TD
            $(tdInner).click(square.click_function);
            //tdInner.text(i + " " + j);
            tr.append(tdInner);
        }
        $('#myTable').append(tr);
        // close & add TR
    }
    
    
    
    function setNeigbours(array, square, row, column){
        dirArray = new Array(DIR.W, DIR.NW, DIR.N, DIR.NE);
        for(dir in dirArray){
            var r = row + dir.x;var c = column + dir.y;
            if(r >= 0 && c >= 0 && r < array[0].length && c < array.length && array[c][r] != null)
                createSquare.linkNeighbors(array[c][r], dir);
        }
    }
}

