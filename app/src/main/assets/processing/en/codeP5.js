class CodeP5 {
    constructor() {
        this.p5_obj = {};
    }

    runCode() {
        try {
            const p5code =
                "var ghost, asterisk;\n" +
                "var size_w = 50; size_h = 50;\n" +
                "var loc_x = 100; loc_y = 100;\n" +
                "preload = function() {\n" +
                "  //ghost = loadAnimation('assets/ghost_standing0001.png', 'assets/ghost_standing0007.png');\n" +
                "  //asterisk = loadAnimation('assets/asterisk_circle0000.png', 'assets/asterisk_circle0002.png');\n" +
                "};\n" +
                "simpleTriangle = function(x,y,w,h){\n" +
                "    triangle(x,y, x+w/2, y-h, x+w, y);\n" +
                "};\n" +
                "gridLines = function(){\n" +
                "  fill(0,0,66);\n" +
                "  stroke(0,0,77);\n" +
                "  for (var i = 0; i < width; i += 50) {\n" +
                "    line(i, 0, i, height);\n" +
                "    text(i, i + 1, 10);\n" +
                "  }\n" +
                "  for (var i = 0; i < height; i += 50) {\n" +
                "    line(0, i, width, i);\n" +
                "    text(i, 0, i - 1);\n" +
                "  }\n" +
                "};\n" +
                "{0}\n" +
                "setup = function() {\n" +
                " colorMode(HSL, 360, 100, 100);" +
                " var myCanvas = createCanvas(windowWidth,windowHeight);\n" +
                " myCanvas.parent('myContainer');\n" +
                " gridLines();\n" +
                "};\n" +
                "drawThings = function(){\n" +
                " {1}\n" +
                " {2}\n" +
                "};\n" +
                "draw = function() {\n" +
                " drawThings();\n" +
                " {3}\n" +
                "};";
            let code = p5code.format(functionBlocks.join(' '), variableBlocks.join(' '), drawBlocks.join(' '), loopBlocks.join());
            let s = new Function("p", code);
            if (debug === true) console.log(code);
            this.p5_obj = new p5(s);
        } catch (e) {
            alert(e);
        }
    }

    runActivity() {
        try {
            const p5code =
                "var ghost, asterisk;\n" +
                "var size_w = 100; size_h = 100;\n" +
                "var loc_x = 100; loc_y = 100;\n" +
                "preload = function() {\n" +
                "  //ghost = loadAnimation('assets/ghost_standing0001.png', 'assets/ghost_standing0007.png');\n" +
                "  //asterisk = loadAnimation('assets/asterisk_circle0000.png', 'assets/asterisk_circle0002.png');\n" +
                "};\n" +
                "simpleTriangle = function(x,y,w,h){\n" +
                "    triangle(x,y, x+w/2, y-h, x+w, y);\n" +
                "};\n" +
                "gridLines = function(){\n" +
                "  fill(0,0,66);\n" +
                "  stroke(0,0,77);\n" +
                "  for (var i = 0; i < width; i += 50) {\n" +
                "    line(i, 0, i, height);\n" +
                "    text(i, i + 1, 10);\n" +
                "  }\n" +
                "  for (var i = 0; i < height; i += 50) {\n" +
                "    line(0, i, width, i);\n" +
                "    text(i, 0, i - 1);\n" +
                "  }\n" +
                "};\n" +
                "{0}\n" +
                "setup = function() {\n" +
                " colorMode(HSL, 360, 100, 100);" +
                " var myCanvas = createCanvas(320, 480);\n" +
                " myCanvas.parent('myContainer');\n" +
                " gridLines();\n" +
                "};\n" +
                "drawThings = function(){\n" +
                " {1}\n" +
                " {2}\n" +
                "};\n" +
                "draw = function() {\n" +
                " drawThings();\n" +
                " {3}\n" +
                "};";
            let code = p5code.format(functionBlocks.join(' '), variableBlocks.join(' '), drawBlocks.join(' '), loopBlocks.join());
            let s = new Function("p", code);
            if (debug === true) console.log(code);
            this.p5_obj = new p5(s);
        } catch (e) {
            alert(e);
        }
    }

}