let inLoop = false;
let sampleSelected = false;
let pauseState = false;
let beatAdded = false;
let soundOn = false;

let code = "";
let beatCode = "";
let octave = 4;
let selectedSound = "piano";
let selectedWave = "sine";
let duration = "4n";
let notes = [];

let synth_options = "{ 'oscillator' : { 'type' : '{0}' }, " +
    "'envelope' : { 'attack' : 0.1, 'decay': 0.1, 'sustain': 0.9, 'release': 1 } }";
let synth_code = "var synth = new Tone.Synth('{0}').toMaster();\n" +
    "function playInterval(notes) { \n" +
    "   var interval = new Tone.Sequence(function(time, note){ \n" +
    "       synth.triggerAttackRelease(note.note, note.duration, time); \n" +
    "   }, notes, '4n'); \n" +
    "   interval.loop = {1};\n" +
    "   interval.start(0);\n " +
    "   Tone.Transport.start('+0.2');\n" +
    "}\n" +
    "function triggerSynth(time, note){ \n" +
    "   synth.triggerAttackRelease(note.note, note.duration, time); \n" +
    "} \n" +
    "playInterval([{2}]);\n";
let sample_code = "var instruments = SampleLibrary.load({instruments: ['{0}']});\n" +
    "Tone.Buffer.on('load', function () { \n" +
    "   instruments['{1}'].toMaster(); \n" +
    "   var interval = new Tone.Sequence(function (time, note) { \n" +
    "       instruments['{2}'].triggerAttackRelease(note.note, note.duration, time); \n" +
    "   }, [{3}], '4n'); \n" +
    "   interval.loop = {4}; \n" +
    "   interval.start(0); \n" +
    "   Tone.Transport.start('+0.2'); \n" +
    "});";
let add_beat = "var kick = new Tone.MembraneSynth();\n" +
    "var kickdistortion = new Tone.Distortion(8);\n" +
    "var kickdelay = new Tone.PingPongDelay({\n" +
    "  'delayTime' : '8n',\n" +
    "  'feedback' : 0.3,\n" +
    "  'wet' : 0.5\n" +
    "});\n" +
    "var kickphaser = new Tone.Phaser();\n" +
    "kick.chain(kickdistortion, kickdelay, kickphaser, Tone.Master);\n" +
    "\n" +
    "var kickloop = new Tone.Loop(function(time) {\n" +
    "  kick.triggerAttackRelease('C1', '8n', time);\n" +
    "}, '4n').start();";

let pause_play = "Tone.Transport.pause();\n";
let note_text = "{ time : {0}, note : '{1}{2}', dur : '{3}'}";

function addMusic() {
    soundOn = true;
    clearCode();
}

function getCode() {
    code = "";
    if (sampleSelected) {
        code += sample_code.format(selectedSound, selectedSound, selectedSound, notes, inLoop);
    }else {
        code += synth_code.format(selectedWave, inLoop, notes);
    }
    if (beatAdded){
        code += beatCode;
    }
    return code;
}

function resume(){
    pauseState = false;
    return getCode();
}

function pause(){
    pauseState = true;
    return pause_play;
}

function isPaused(){
    return pauseState;
}

function changeOctave(oct) {
    octave = oct;
}

function isSampleSelected() {
    return sampleSelected;
}

function clearCode() {
    code = "";
    beatCode = "";
    notes = [];
}

function deleteLastPart() {
    if (code.length() > 0) {
        let lastLineIndex = code.lastIndexOf('\n');
        code = code.substring(0, lastLineIndex);
    }
}

function startLoop() {
    inLoop = true;
}

function addBeat(){
    beatCode = "";
    beatAdded = true;
    beatCode += add_beat;
}

function deleteBeat(){
    beatAdded = false;
    beatCode = "";
}

function shortNote(){
    duration = "8n";
}


function longNote(){
    duration = "2n";
}

function selectSample(sample) {
    selectedSound = sample;
    sampleSelected = true;
}

function selectSynthWave(wave){
    selectedWave = wave;
    sampleSelected = false;
}

function addNote(note) {
    switch (note) {
        case 'N':
            notes.push("null");
            break;
        default:
            notes.push(note_text.format(notes.length * 0.5, note, octave, duration));
            break;
    }
}