class CodeP5 {
    constructor() {
        this.p5_obj = {};
    }

    runCode() {
        try {
            let code = p5code.format(variableBlocks.join(' '), setupBlocks.join(' '), drawBlocks.join(' '));
            let s = new Function("p", code);
            this.p5_obj = new p5(s);
            if (soundOn) {
                eval(getCode());
            }
        } catch (e) {
            alert(e);
        }
    }

}
