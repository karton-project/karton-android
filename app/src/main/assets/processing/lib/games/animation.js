var ghost, asterisk;

function preload() {
    ghost = loadAnimation('../assets/ghost_standing0001.png', '../assets/ghost_standing0007.png');
    asterisk = loadAnimation('../assets/asterisk.png', '../assets/triangle.png', '../assets/square.png', '../assets/cloud.png', '../assets/star.png', '../assets/mess.png', '../assets/monster.png');
}

function setup() {
    createCanvas(windowWidth, windowHeight);
}

function draw() {
    background(255, 255, 255);
    animation(ghost, windowWidth * 0.3, windowHeight /2);
    animation(asterisk, windowWidth * 0.6, windowHeight / 2);
}
