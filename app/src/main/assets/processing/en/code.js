const codeList = [{
        title: "fill",
        code: "fill({0}, 75, 50);\n",
        input: "attr",
        code_type: 3,
        no_in: 1
    },
    {
        title: "background",
        code: "background({0}, 75, 50);\n",
        input: "attr",
        code_type: 3,
        no_in: 1
    },
    {
        title: "stroke",
        code: "stroke({0}, 75, 50);\n",
        input: "attr",
        code_type: 3,
        no_in: 1
    },
    {
        title: "size",
        code: "size_w = {0}; size_h = {1};",
        input: "attr",
        code_type: 3,
        no_in: 2
    },
    {
        title: "location",
        code: "loc_x = {0}; loc_y = {1};",
        input: "attr",
        code_type: 3,
        no_in: 2
    },
    {
        title: "ellipse",
        code: "ellipse(loc_x, loc_y, size_w, size_h);\n",
        input: "shape",
        code_type: 3,
        no_in: 0
    },
    {
        title: "begin shape",
        code: "beginShape();\n",
        input: "shape",
        code_type: 3,
        no_in: 0
    },
    {
        title: "end shape",
        code: "endShape(CLOSE);\n",
        input: "shape",
        code_type: 3,
        no_in: 0
    },
    {
        title: "vertex",
        code: "vertex({0}, {1});\n",
        input: "attr",
        code_type: 3,
        no_in: 2
    },
    {
        title: "line",
        code: "line(loc_x, loc_y, {0}, {1});\n",
        input: "attr",
        code_type: 3,
        no_in: 2
    },
    {
        title: "translate",
        code: "translate({0}, {1});\n",
        input: "attr",
        code_type: 3,
        no_in: 2
    },
    {
        title: "rotate",
        code: "rotate({0});\n",
        input: "attr",
        code_type: 3,
        no_in: 1
    },
    {
        title: "rectangle",
        code: "rect(loc_x, loc_y, size_w, size_h);\n",
        input: "shape",
        code_type: 3,
        no_in: 0
    },
    {
        title: "triangle",
        code: "simpleTriangle(loc_x, loc_y, size_w, size_h);\n",
        input: "shape",
        code_type: 3,
        no_in: 0
    },
    {
        title: "text",
        code: "textSize(size_h); \n text('{0}', loc_x, loc_y);\n",
        input: "shape",
        code_type: 3,
        no_in: 1
    },
    {
        title: "puppet",
        code: "animation(ghost, loc_x, loc_y);\n",
        input: "shape",
        code_type: 3,
        no_in: 0
    },
    {
        title: "define function",
        code: "{0} = function(){\n",
        input: "cond",
        code_type: 2,
        no_in: 1
    },
    {
        title: "end",
        code: "};\n",
        input: "end",
        code_type: 3,
        no_in: 0
    },
    {
        title: "call",
        code: "{0}();\n",
        input: "call",
        code_type: 3,
        no_in: 1
    },
    {
        title: "new variable",
        code: "let {0} = {1};\n",
        input: "variable",
        code_type: 1,
        no_in: 2
    },
    {
        title: "increase value",
        code: "{0} += {1};\n",
        input: "variable",
        code_type: 3,
        no_in: 2
    },
    {
        title: "decrease value",
        code: "{0} -= {1};\n",
        input: "variable",
        code_type: 3,
        no_in: 2
    },
    {
        title: "set value",
        code: "{0} = {1};\n",
        input: "variable",
        code_type: 3,
        no_in: 2
    },
    {
        title: "if",
        code: "if({0}){\n",
        input: "cond",
        code_type: 3,
        no_in: 1
    },
    {
        title: "else",
        code: "}else{\n",
        input: "cond",
        code_type: 3,
        no_in: 0
    },
    {
        title: "repeat",
        code: "for(let i = 0; i < {0}; i++){\n",
        input: "cond",
        code_type: 3,
        no_in: 1
    },
    {
        title: "open/close loop",
        code: "",
        input: "loop",
        code_type: 0,
        no_in: 0
    },
    {
        title: "random",
        code: "let {0} = random({1});\n",
        input: "variable",
        code_type: 1,
        no_in: 2
    },
    {
        title: "when device shake",
        code: "deviceShaken = function() {\n",
        input: "cond",
        code_type: 2,
        no_in: 0
    },
    {
        title: "wait",
        code: "await sleep({0});\n",
        input: "numeric",
        code_type: 3,
        no_in: 1
    },
    {
        title: "",
        code: "",
        input: "numeric",
        code_type: 2,
        no_in: 1
    }
]