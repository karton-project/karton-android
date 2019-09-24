let p5code =
    "{0}\n" +
    "let ghost, asterisk;\n" +
    "preload = function() {\n" +
    "  ghost = loadAnimation('assets/ghost_standing0001.png', 'assets/ghost_standing0007.png');\n" +
    "  asterisk = loadAnimation('assets/asterisk_circle0000.png', 'assets/asterisk_circle0002.png');\n" +
    "}\n" +
    "setup = function() {\n" +
    " var myCanvas = createCanvas(windowWidth,windowHeight);\n" +
    " myCanvas.parent('myContainer');\n" +
    " fill(255);\n" +
    " {1}\n" +
    "};\n" +
    "draw = function() {\n" +
    " background(200);\n" +
    " ellipse(mouseX,mouseY,50,50);\n" +
    " {2}\n" +
    "};";

let condOnProgress = false;
let variableNames = [];
let variableBlocks = [];
let setupBlocks = [];
let drawBlocks = [];
let condCodeType = 1;

function initInterpreter() {
    window.$.getJSON('https://raw.githubusercontent.com/asabuncuoglu13/kart-on-programming/master/karton-android/app/src/main/assets/processing/code.json', function (response) {
    //window.$.getJSON('./code.json', function (response) {
        fuse = new Fuse(response, {
            keys: ['title'],
            shouldSort: true
        });
    });
}

function addCodeInput(codeInput, codeType) {
    let parsedText = parse(codeInput);
    let ct = (typeof codeType !== 'undefined') ? codeType : parsedText[1];
    if (ct === 1)
        variableBlocks.push(parsedText[0]);
    else if (ct === 2)
        setupBlocks.push(parsedText[0]);
    else if (ct === 3)
        drawBlocks.push(parsedText[0]);
}

function parse(code_text) {
    let code_sub = "";
    if (code_text.indexOf("\n") > 0) {
        code_sub = code_text.substring(0, code_text.indexOf("\n"));
    } else {
        code_sub = code_text;
    }
    code_sub = code_sub.toLowerCase();
    code_sub = code_sub.replace(/\s+/g, " ").trim();
    let result = fuse.search(code_sub.replace(/\s+/g, " ").trim().substring(0, (code_sub.indexOf(':') > 0) ? code_sub.indexOf(':') : code_sub.length));
    let resultCode = result[0].code;
    let inputs = [];
    let codeType = condOnProgress ? condCodeType : result[0].code_type;
    if (result[0].input === "numeric") {
        inputs.push(code_sub.match(/\d+/g).map(Number));
        resultCode = resultCode.format(inputs);
    } else if (result[0].input === "string") {
        let index = code_sub.indexOf(":");
        resultCode = resultCode.format(code_sub.substring(index + 1, code_sub.length).trim());
    } else if (result[0].input === "music") {
        if (result[0].no_in === 0) {
            resultCode = result[0].code;
        } else if (result[0].no_in === 1) {
            resultCode = resultCode.format(code_sub.substring(code_sub.lastIndexOf(" ") + 1, code_sub.length));
        }
        eval(resultCode);
    } else if (result[0].input === "cond") {
        condOnProgress = true;
        condCodeType = result[0].code_type;
        if (result[0].no_in === 0) {
            resultCode = result[0].code;
        } else if (result[0].no_in === 1) {
            resultCode = resultCode.format(code_sub.substring(code_sub.indexOf(":") + 1, code_sub.length).trim());
        }
    } else if (result[0].input === "end") {
        resultCode = result[0].code;
        condOnProgress = false;
    } else if (result[0].input === "variable") {
        let n_index = code_sub.indexOf("n:");
        let v_index = code_sub.indexOf("v:");
        let var_name = code_sub.substring(n_index + 2, v_index - 1);
        if (variableNames.includes(var_name) && result[0].code_type === 1) {
            resultCode = "";
        } else {
            resultCode = resultCode.format(var_name, code_sub.substring(v_index + 2, code_sub.length));
            variableNames.push(var_name);
        }
    } else if (result[0].input === "shape") {
        let x_index = code_sub.indexOf("x:");
        let y_index = code_sub.indexOf("y:");
        let w_index = (code_sub.indexOf("w:") > 0) ? code_sub.indexOf("w:") : code_sub.lastIndexOf("x:");
        let h_index = (code_sub.indexOf("h:") > 0) ? code_sub.indexOf("h:") : code_sub.lastIndexOf("y:");
        let x = code_sub.substring(x_index + 2, y_index - 1).trim();
        let y = code_sub.substring(y_index + 2, w_index - 1).trim();
        let w = code_sub.substring(w_index + 2, h_index - 1).trim();
        let h = code_sub.substring(h_index + 2, code_sub.length).trim();
        resultCode = resultCode.format([x,y,w,h]);
    } else if (result[0].input === "text") {
        code_sub = code_sub.replace(/\s+/g, " ").trim();
        let t_index = code_sub.indexOf(":");
        let x_index = code_sub.indexOf("x:");
        let y_index = code_sub.indexOf("y:");
        let s_index = code_sub.indexOf("s:");
        let t = code_sub.substring(t_index + 2, x_index - 1).trim();
        let x = code_sub.substring(x_index + 2, y_index - 1).trim();
        let y = code_sub.substring(y_index + 2, s_index - 1).trim();
        let s = code_sub.substring(s_index + 2, code_sub.length).trim();
        resultCode = resultCode.format(s,t,x,y);
    }
    return [resultCode, codeType];
}

function runP5Code() {
    var codeP5 = new CodeP5();
    if(!condOnProgress){
        codeP5.runCode();
    }
}
