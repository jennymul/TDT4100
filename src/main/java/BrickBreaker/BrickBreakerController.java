package BrickBreaker;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BrickBreakerController {

    //FXML-noder opprettet i App.fxml
    @FXML
    private Circle movingBall;
    @FXML
    private AnchorPane background;
    @FXML
    private Rectangle playerRect;
    @FXML
    private Button startknapp;

    //FXML-noder opprettet underveis i programmet
    private TextField nameFieldInput;
    private Text gameOverText;
    private Text nameFieldText;
    private Text scoreText;
    private Text scoreBoardText;
    private static TextArea scoreBoardOversikt;
    private Button lagreKnapp;
    private Button restartKnapp;
    private Button seScoreBoard;
    private Bounds bounds;
    private Rectangle gameoverScreen;
    private Rectangle scoreBoard;

    //Bredder og høyder på fxml-noder
    private double playerRectWidth;
    private double movingBallRadius;
    private double screenWidth;
    private double screenHeight;
    private static double brickWidth = 30;
    private static double brickHeight = 30;
    private static double gameOverScreenWidth = 250;
    private static double gameOverScreenHeight = 250;
    
    //Posisjoner på fxml-noder
    private double backgroundXMinPos;
    private double backgroundXMaxPos;
    private double backgroundYMinPos;
    private double backgroundYMaxPos;
    private double movingBallXStartingPosition;
    private double movingBallYStartingPosition;
    private double movingBallXPosition;
    private double movingBallYPosition;
    private double playerRectXPosition;
    private double playerRectYPosition;
    private double MouseXPos;

    //Objekter av klasser
    private ValidateReadWrite validateReadWrite;
    private PlayerRect playerRectObject;
    private MovingBall movingBallObject;
    
    //Øvrige variabler
    private double dx = 1;  //Fart i x-retning
    private double dy = -3; //Fart i y-retning
    private int score = 0;  //Poengscore
    private String name;    //Navn på bruker
    private ArrayList<Rectangle> brickList = new ArrayList<>(); //Liste over alle bricks
    
    private void initReadAndWrite() {
        ValidateReadWrite validateReadWrite = new ValidateReadWrite();
        this.validateReadWrite = validateReadWrite;
    }
    
    private void initPlayerRect() {
        PlayerRect playerRectObject = new PlayerRect();
        this.playerRectObject = playerRectObject;
    }

    private void initMovingBall() {
        MovingBall movingBall = new MovingBall();
        this.movingBallObject = movingBall;
    }
    
    private Robot robot = new Robot();

    Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent t) {
            MouseXPos = robot.getMouseX() - 340; // Henter ut posisjon til musa kontinuerlig, må kompensere for hvor vinduet er relativt til skjermen
            
            // flytter ballen
            movingBall.setLayoutX(movingBall.getLayoutX() + dx);
            movingBall.setLayoutY(movingBall.getLayoutY() + dy);
            
            //Henter posisjonen til ballen kontinuerlig
            movingBallXPosition = movingBall.getLayoutX();
            movingBallYPosition = movingBall.getLayoutY();

            //Henter posisjonen til playerRect kontinuerlig
            playerRectXPosition = playerRect.getLayoutX();
            playerRectYPosition = playerRect.getLayoutY();


            //flytter rektangelet, sjekker for kollisjoner
            movePlayerRect(backgroundXMinPos, backgroundXMaxPos, playerRectWidth, MouseXPos);
            checkCollisionBackground();
            checkCollisionPlayerRect();


            if (brickList.isEmpty()) {
                gameOver(); //Får gameover-skjerm til å dukke opp
            } else {
                try {
                    brickList.removeIf(brick -> checkCollisionBricks(brick));
                } catch (ConcurrentModificationException e) {
                    // Ingenting skal skje, programmet prøver å fjerne en brick fra listen mens den fortsatt itereres over
                    // Dette er mulig med removeIf, men feilmelding kastes likevel
                }
            }

        }
    }));

    @FXML
    private void handleButtonClick() { // Når startknappen blir trykket på

        try {
            startknapp.setVisible(false);
            initPlayerRect();
            initMovingBall();

            dx = 1;
            dy = -3;

            score = 0;

            //Startposisjon til ball
            movingBallXStartingPosition = movingBall.getLayoutX();
            movingBallYStartingPosition = movingBall.getLayoutY();

            //Bredde og høyde på skjerm
            screenWidth = background.getWidth();
            screenHeight = background.getHeight();
            
            playerRectWidth = playerRect.getWidth(); //bredde på rektangelet
            movingBallRadius = movingBall.getRadius();  //radius på movingBall
            
            bounds = background.getBoundsInLocal(); //grensene på background
            backgroundXMinPos = bounds.getMinX(); //posisjonen lengst til venstre på background
            backgroundXMaxPos = bounds.getMaxX();   //posisjonen lengst til høyre på background
            backgroundYMinPos = bounds.getMinY();   //posisjonen øverst på background
            backgroundYMaxPos = bounds.getMaxY();   //posisjonen nederst på background
            
            generateBricks();   //Genererer brikkene
            startAnimasjon();   //starter timeline
            background.setCursor(Cursor.NONE);
            
        } catch (Exception e) {
            System.out.println("Feil ved start av animasjon");
        }
    }
    
    public void generateBricks() {
  
        for (double i = screenWidth - 1.8 * brickWidth; i > 0; i -= 40) {
            for (double j = screenHeight * 0.3; j > 0; j -= 40) {
                Rectangle rectangle = new Rectangle(i, j, brickWidth, brickHeight);
                rectangle.setFill(Color.HOTPINK);
                background.getChildren().add(rectangle);
                brickList.add(rectangle);
            }
        }
    }

    public void startAnimasjon() throws Exception {

        timeline1.setCycleCount(Timeline.INDEFINITE);
        timeline1.play();
    }

    public void checkCollisionBackground() {

        // Hvis ballen treffer venstre eller høyre kant
        if (movingBallObject.leftHit(movingBallXPosition, backgroundXMinPos, backgroundXMaxPos, movingBallRadius, playerRectWidth, MouseXPos) ||
        movingBallObject.rightHit(movingBallXPosition, backgroundXMinPos, backgroundXMaxPos, movingBallRadius, playerRectWidth, MouseXPos)) {

            dx = -dx;
        }

        // Hvis ballen treffer bunn
        if (movingBallObject.bottomHit(movingBallYPosition, backgroundYMinPos, backgroundYMaxPos, movingBallRadius)) {
            gameOver();
        }

        // Hvis ballen treffer topp
        if (movingBallObject.topHit(movingBallYPosition, backgroundYMinPos, backgroundYMaxPos, movingBallRadius)) {
            dy = -dy;
        }
    }

    public void checkCollisionPlayerRect() {
        // sjekker om ballen kolliderer med playerRect, og endrer retningen deretter
        if (movingBall.getBoundsInParent().intersects(playerRect.getBoundsInParent())) {

            if (movingBallObject.rectHit(movingBallYPosition, playerRectYPosition, movingBallRadius)) {
                dy = -dy;

            }
        }
    }

    public boolean checkCollisionBricks(Rectangle brick) {
        //Sjekker om ball kolliderer med en brick
        if (brick != null && movingBall.getBoundsInParent().intersects(brick.getBoundsInParent())) {

            if (movingBallObject.leftOrRightBrickHit(movingBallXPosition, movingBallRadius, brick, brickWidth)) {
                dx = -dx;
            } else if (movingBallObject.topOrBottomBrickHit(movingBallYPosition, movingBallRadius, brick, brickHeight)) {
                dy = -dy;
            }
            // fjerner brick og legger til et poeng på score
            background.getChildren().remove(brick);
            brickList.remove(brick);
            score++;

            return true;
        }
        return false;
    }

    public void movePlayerRect(double backgroundXMinPos, double backgroundXMaxPos, double playerRectWidth, double MouseXPos) {
        //flytter på rektangel

        //Hvis posisjonen til playerRect er innenfor background, setter posisjon til dette
        if (playerRectObject.insideBounds(backgroundXMinPos, backgroundXMaxPos, playerRectWidth, MouseXPos)) {
            playerRect.setLayoutX(MouseXPos + backgroundXMinPos - (playerRectWidth / 2));
        }
        //Hvis man beveger playerRect utenfor background til venstre, skal den forbli lengst mulig til venstre
        if (playerRectObject.leftHit(MouseXPos, backgroundXMinPos, backgroundXMaxPos, backgroundXMaxPos, playerRectWidth, MouseXPos)) {
            playerRect.setLayoutX(0);
        }
        //Hvis man beveger playerRect utenfor background til høyre, skal den forbli lengst mulig til høyre
        if (playerRectObject.rightHit(MouseXPos, backgroundXMinPos, backgroundXMaxPos, backgroundXMaxPos, playerRectWidth, MouseXPos)) {
            playerRect.setLayoutX(backgroundXMaxPos - playerRectWidth);
        }
    }

    public void gameOver() {

        timeline1.stop();
        
        background.setCursor(null);

        gameoverScreen = new Rectangle(screenWidth / 2 - gameOverScreenWidth / 2,
        screenHeight / 2 - gameOverScreenHeight / 2, gameOverScreenWidth, gameOverScreenHeight);
        gameoverScreen.setFill(Color.GREEN);
        
        gameOverText = new Text();
        gameOverText.setText("Game Over");
        gameOverText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        gameOverText.setX(screenWidth / 2 - gameOverScreenWidth / 2.6);
        gameOverText.setY(screenHeight / 3.2);

        scoreText = new Text();
        scoreText.setText("Score: " + score);
        scoreText.setFont(Font.font("verdana"));
        scoreText.setX(screenWidth / 2 - gameOverScreenWidth / 2.6);
        scoreText.setY(screenHeight / 2.6);

        nameFieldText = new Text();
        nameFieldText.setText("Skriv inn navn:");
        nameFieldText.setFont(Font.font("verdana"));
        nameFieldText.setX(screenWidth / 2 - gameOverScreenWidth / 2.6);
        nameFieldText.setY(screenHeight / 2.3);

        nameFieldInput = new TextField();
        nameFieldInput.setLayoutX(screenWidth / 2 - gameOverScreenWidth / 2.6);
        nameFieldInput.setLayoutY(screenHeight / 2.1);

        lagreKnapp = new Button();
        lagreKnapp.setText("Lagre Score");
        lagreKnapp.setFont(Font.font("verdana"));
        lagreKnapp.setLayoutX(screenWidth / 2 - gameOverScreenWidth / 2.6);
        lagreKnapp.setLayoutY(screenHeight / 1.7);

        restartKnapp = new Button();
        restartKnapp.setText("Restart");
        restartKnapp.setFont(Font.font("verdana"));
        restartKnapp.setLayoutX(screenWidth / 3 + gameOverScreenWidth / 1.8);
        restartKnapp.setLayoutY(screenHeight / 1.4);

        seScoreBoard = new Button();
        seScoreBoard.setText("Score Board");
        seScoreBoard.setFont(Font.font("verdana"));
        seScoreBoard.setLayoutX(screenWidth / 2 - gameOverScreenWidth / 2.6);
        seScoreBoard.setLayoutY(screenHeight / 1.5);

        scoreBoard = new Rectangle(screenWidth / 2 - gameOverScreenWidth / 2,
                screenHeight / 2 - gameOverScreenHeight / 2, gameOverScreenWidth, gameOverScreenHeight);
        scoreBoard.setFill(Color.GREEN);

        scoreBoardText = new Text();
        scoreBoardText.setText("Scoreboard");
        scoreBoardText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        scoreBoardText.setX(screenWidth / 2 - gameOverScreenWidth / 2.6);
        scoreBoardText.setY(screenHeight / 4);

        scoreBoardOversikt = new TextArea();
        scoreBoardOversikt.setFont(Font.font("verdana"));
        scoreBoardOversikt.setMaxHeight(160);
        scoreBoardOversikt.setMaxWidth(200);
        scoreBoardOversikt.setLayoutX(screenWidth/ 2 - gameOverScreenWidth / 2.6);
        scoreBoardOversikt.setLayoutY(screenHeight / 3.4);

        lagreKnapp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                name = nameFieldInput.getText();
                initReadAndWrite(); //Lager et objekt av typen ValidateReadWrite
                validateReadWrite.lagreVerdi(name, score, "./ScoreBoard.txt");  //Skriver score til fil
                lagreKnapp.setDisable(true);

            }
        });

        seScoreBoard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                validateReadWrite.leseScoreboard(); //Leser fra fil
                seScoreBoard.setDisable(true);
                background.getChildren().removeAll(gameoverScreen, gameOverText, scoreText, nameFieldInput,
                        nameFieldInput, lagreKnapp, seScoreBoard, nameFieldText);   //Fjerner gameover-skjerm
                background.getChildren().add(scoreBoardOversikt);   //Viser scoreboard på skjerm

            }
        });
        
        background.getChildren().addAll(scoreBoard, scoreBoardText);

        restartKnapp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                background.getChildren().removeAll(gameoverScreen, gameOverText, scoreText, nameFieldText,
                        nameFieldInput,
                        lagreKnapp, restartKnapp, seScoreBoard, scoreBoard, scoreBoardText, scoreBoardOversikt);
                restartGame();
            }
        });

        background.getChildren().addAll(gameoverScreen, gameOverText, scoreText, nameFieldText, nameFieldInput,
                seScoreBoard,
                lagreKnapp, restartKnapp);

    }

    public void restartGame() {
        //Resetter startposisjon
        movingBall.setLayoutX(movingBallXStartingPosition);
        movingBall.setLayoutY(movingBallYStartingPosition);
        handleButtonClick();
    }
    
    public static void setScoreBoardOversikt(String newscoreboard) {
        scoreBoardOversikt.appendText(newscoreboard + "\n");
    }
}