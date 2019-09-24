const sketch = p5 => {
    let background = p5.loadImage('https://raw.githubusercontent.com/asabuncuoglu13/karton-web/master/flappy-game/assets/background.png');
    let spriteImage = p5.loadImage('https://raw.githubusercontent.com/asabuncuoglu13/karton-web/master/flappy-game/assets/sprite.png');
    let birdyFont = p5.loadFont('https://raw.githubusercontent.com/asabuncuoglu13/karton-web/master/flappy-game/assets/FlappyBirdy.ttf');
    let gameStart;
    let gameOver;
    let bird;
    let pipe;
    let floor;
    let gameButton;
    let gameText;
    let score;

    const resetGame = () => {
        gameStart = false;
        gameOver = false;
        bird = new Bird(p5, spriteImage);
        pipe = new Pipe(p5, spriteImage);
        floor = new Floor(p5, spriteImage);
        gameText = new GameText(p5, birdyFont);
        gameButton = new GameButton(p5, gameText, spriteImage);
        score = 0;
        if (changeScoreOnRun){
            score = scoreAmount;
        }
        pipe.generateFirst();
        bird.draw();
        floor.draw();
    };

    const canvasClick = () => {
        if (p5.mouseButton === 'left') {
            if (gameOver === false)
                if(addFlap){
                    bird.gravity = 0.5;
                    bird.jump();
                }
            if (changeScoreOnClick){
                score  += scoreAmount;
            }
            if (gameStart === false)
                gameStart = true;
            if (gameOver &&
                p5.mouseX > CANVAS_WIDTH / 2 - 85 &&
                p5.mouseX < CANVAS_WIDTH / 2 + 75 &&
                p5.mouseY > CANVAS_HEIGHT / 2 + 100 &&
                p5.mouseY < CANVAS_HEIGHT / 2 + 160
            )
                resetGame();
        }
    };

    const canvasTouch = () => {
        if (gameOver === false)
            if(addFlap){
                bird.gravity = 0.5;
                bird.jump();
            }
        if (changeScoreOnClick){
            score  += scoreAmount;
        }
        if (gameStart === false)
            gameStart = true;
    };

    p5.setup = () => {
        var canvas = p5.createCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.mousePressed(canvasClick);
        canvas.touchStarted(canvasTouch);
        resetGame();
    };

    p5.draw = () => {
        p5.image(background, 0, 0);

        if (gameStart && gameOver === false) {
            if(addPipe){
                pipe.move();
                pipe.draw();
            }
            bird.update();
            bird.draw();

            if (addSpeed){
                floor.update();
            }
            floor.draw();

            if(addEndGamewithObstacle){
                gameOver = pipe.checkCrash(bird);
            }

            if (addEndGamewithGround){
                gameOver = bird.isDead();
            }

            if (changeScoreOnPass){
                if (pipe.getScore(bird))
                    score  += scoreAmount;
            }

            if (changeScoreOnHitO){
                if (pipe.checkCrash(bird))
                    score  += scoreAmount;
            }

            if (changeScoreOnHitG){
                if (bird.isDead())
                    score  += scoreAmount;
            }
        }
        else {
            pipe.draw();
            bird.draw();
            floor.draw();
            if (gameOver)
                bird.update();
        }


        if (gameStart === false) {
            gameText.startText();
        }

        if (gameOver) {
            gameText.gameOverText(score);

            gameButton.resetButton();
        }
        else {
            gameText.scoreText(score);
        }
    };

    p5.keyPressed = (e) => {
        if (e.key === ' ') {
            if (gameOver === false)
                if(addFlap){
                    bird.gravity = 0.5;
                    bird.jump();
                }
            if (gameStart === false)
                gameStart = true;
        }
        if (e.key === 'r') {
            if (gameOver) {
                resetGame();
            }
        }
    }
};
