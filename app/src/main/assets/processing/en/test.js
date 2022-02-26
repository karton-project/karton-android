// Draw a Car with Function
clearCode();

addCodeInput("define function # araba");
addCodeInput("fill # 232");
addCodeInput("stroke # 165");
addCodeInput("dimension # 150 # 100");
addCodeInput("location # 100 # 50");
addCodeInput("rectangle");
addCodeInput("dimension # 50 # 50");
addCodeInput("location # 125 # 175");
addCodeInput("ellipse");
addCodeInput("dimension # 50 # 50");
addCodeInput("location # 225 # 175");
addCodeInput("ellipse");
addCodeInput("end");

// Call the Car Function with a Loop
addCodeInput("repeat # 12");
addCodeInput("call # araba");
addCodeInput("translate # 50 # 20");
addCodeInput("end");

runP5Code();

// Draw a Cat
clearCode();
addCodeInput("location # 100 # 150");
addCodeInput("dimension # 100 # 200");
addCodeInput("rectangle");
addCodeInput("fill # 32");
addCodeInput("location # 100 # 350");
addCodeInput("dimension # 50 # 50");
addCodeInput("ellipse");
addCodeInput("location # 200 # 350");
addCodeInput("dimension # 50 # 50");
addCodeInput("ellipse");
addCodeInput("location # 60 # 60");
addCodeInput("dimension # 50 # 40");
addCodeInput("triangle");
addCodeInput("location # 190 # 60");
addCodeInput("dimension # 50 # 40");
addCodeInput("triangle");
addCodeInput("fill # 44");
addCodeInput("location # 150 # 150");
addCodeInput("dimension # 200 # 200");
addCodeInput("ellipse");
addCodeInput("fill # 192");
addCodeInput("location # 120 # 100");
addCodeInput("dimension # 50 # 50");
addCodeInput("ellipse");
addCodeInput("location # 180 # 100");
addCodeInput("dimension # 50 # 50");
addCodeInput("ellipse");
addCodeInput("fill # 200");
addCodeInput("location # 120 # 100");
addCodeInput("dimension # 20 # 20");
addCodeInput("ellipse");
addCodeInput("location # 180 # 100");
addCodeInput("dimension # 20 # 20");
addCodeInput("ellipse");
addCodeInput("stroke 332");
addCodeInput("begin shape");
addCodeInput("vertex # 150 # 170");
addCodeInput("vertex # 120 # 180");
addCodeInput("end shape");
addCodeInput("begin shape");
addCodeInput("vertex # 150 # 170");
addCodeInput("vertex # 130 # 190");
addCodeInput("end shape");
addCodeInput("begin shape");
addCodeInput("vertex # 150 # 170");
addCodeInput("vertex # 180 # 180");
addCodeInput("end shape");
addCodeInput("begin shape");
addCodeInput("vertex # 150 # 170");
addCodeInput("vertex # 170 # 190");
addCodeInput("end shape");

runP5Code();

// Draw a Grid of Ellipses
clearCode();

addCodeInput("new variable # x # 40");
addCodeInput("new variable # y # 40");
addCodeInput("repeat # 5");
addCodeInput("repeat # 5");
addCodeInput("location # x # y");
addCodeInput("dimension # 30 # 30");
addCodeInput("ellipse");
addCodeInput("increase value # x # 50");
addCodeInput("end");
addCodeInput("set value # x # 40");
addCodeInput("increase value # y # 50");
addCodeInput("end");

runP5Code();

// Conditionals
clearCode();

addCodeInput("if # touchx > 100");
addCodeInput("fill # 212");
addCodeInput("else");
addCodeInput("fill # 12");
addCodeInput("end");
addCodeInput("location # 100 # 100");
addCodeInput("dimension # 100 # 100");
addCodeInput("ellipse");

runP5Code();

// Moving Line
addCodeInput("fill # 200");
addCodeInput("begin shape");
addCodeInput("vertex # touchx # 0");
addCodeInput("vertex # touchx # height");
addCodeInput("end shape");
runP5Code();