let drawBlocks = [];
let commands = [];
let debug = false;
let fuse;

function initInterpreter() {
    const fuseOptions = {
        keys: ['title'],
        shouldSort: true,
        includeScore: true
    };
    fuse = new Fuse(codeList, fuseOptions);
}

function addCodeInput(codeInput) {
    let parsedText = parse(codeInput);
    drawBlocks.push(parsedText);
}

function parse(code_text) {
    let command = "";
    let params = [""];
    if (code_text.indexOf("#") > 0) {
        let code_parts = code_text.split('#');
        command = code_parts[0];
        params = code_parts.slice(1);
    } else {
        command = code_text;
    }
    params = correctParams(params);
    let result = fuse.search(command.toLowerCase().replace(/\s+/g, " ").trim());
    let resultCode = result[0].item.code;
    resultCode = resultCode.format(params[0]);
    return resultCode;
}

function correctParams(params) {
    params[0] = params[0].toLowerCase().replace(/\s+/g, " ").trim();
    params[0] = params[0].replace("o", "0");
    params[0] = params[0].replace("s", "5");
    params[0] = params[0].replace("g", "9");
    params[0] = params[0].replace("b", "6");
    params[0] = params[0].replace("i", "1");
    return params;
}

function getListOfElementIDs() {
    var containerElements = document.getElementById("right-copy-1tomany").children;
    var containerElementIDs = [];
    for (var i = 0; i < containerElements.length; i++) {
        containerElementIDs.push(containerElements[i].getElementsByTagName("label")[0].textContent + "#" +
            (containerElements[i].getElementsByTagName("input").length > 0 ? containerElements[i].getElementsByTagName("input")[0].value : ""));
    }
    return containerElementIDs;
}

function runCommandArray() {
    drawBlocks = [];
    commands = getListOfElementIDs();
    for (var i = 0; i < commands.length; i++) {
        addCodeInput(commands[i]);
    }
    runP5Code();
}

function runP5Code() {
    eval(drawBlocks.join(" "));
}

initInterpreter();