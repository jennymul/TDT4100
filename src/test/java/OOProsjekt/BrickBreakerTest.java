package OOProsjekt;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.scene.shape.Rectangle;

import BrickBreaker.MovingBall;
import BrickBreaker.PlayerRect;
import BrickBreaker.ValidateReadWrite;

public class BrickBreakerTest {

    @Test
    @DisplayName("Sjekke om input validation for navn fungerer")
    public void testValidaton() {
        ValidateReadWrite readAndWrite = new ValidateReadWrite();
        
        String testNavn1 = "";
        String testNavn2 = "Navn";
        
        Assertions.assertEquals(true, readAndWrite.validateName(testNavn1));
        Assertions.assertEquals(false, readAndWrite.validateName(testNavn2));
    }
    
    @Test
    @DisplayName("Sjekke skriving til og lesing fra fil, lagreVerdi() og leseScoreboard()")
    public void testWriteAndRead() throws IOException {
        ValidateReadWrite readAndWrite = new ValidateReadWrite();

        String testNavn = "navn";
        int testScore = 1;
        readAndWrite.lagreVerdi(testNavn, testScore, "./ScoreBoardTestFile.txt");

        //Leser manuelt fra fil da leseScoreBoard() krever at scoreBoardOversikt er oppretter først. Dette fungerer i selve kontroller-koden
        String lineFromFile = null;
        File scoreBoard = new File("ScoreBoardTestFile.txt");
        Scanner reader = new Scanner(scoreBoard);
        lineFromFile = reader.nextLine();
        reader.close();

        Assertions.assertEquals(testNavn + " " + Integer.toString(testScore), lineFromFile, "Feil streng lagret i fil");

        scoreBoard.delete();
    }

    @Test
    @DisplayName("Sjekke at Playerrect ikke går utenfor bounds")
    public void testPlayerRect(){
        PlayerRect playerRect = new PlayerRect();
        
        double backgroundXMinPos = 0.0;
        double backgroundxMaxPos = 600.0;
        double playerRectWidth = 133.0;
        boolean testBool;

        //Sjekker om riktig verdi fra insideBounds() når musa er på innsiden av skjermen
        double MouseXPos = 300;
        testBool = playerRect.insideBounds(backgroundXMinPos, backgroundxMaxPos, playerRectWidth, MouseXPos);

        Assertions.assertEquals(true, testBool);

        //Sjekker om riktig verdi fra insideBounds() når musa er til venstre for skjermen
        MouseXPos = 65;
        testBool = playerRect.leftHit(MouseXPos, backgroundXMinPos, backgroundxMaxPos, backgroundxMaxPos, playerRectWidth, MouseXPos);

        Assertions.assertEquals(true, testBool);

        //Sjekker om riktig verdi fra insideBounds() når musa er til høyre for skjermen
        MouseXPos = 900;
        testBool = playerRect.rightHit(MouseXPos, backgroundXMinPos, backgroundxMaxPos, backgroundxMaxPos, playerRectWidth, MouseXPos);

        Assertions.assertEquals(true, testBool);

    }

    @Test
    @DisplayName("Sjekke kollisjon mellom ball og vegg, checkCollisionBackground()")
    public void testBackgroundCollision() {

        MovingBall movingBall = new MovingBall();
        
        double movingBallRadius = 11;
        double backgroundXMinPos = 0;
        double backgroundXMaxPos = 600;
        double backgroundYMinPos = 0;
        double backgroundYMaxPos = 400;
        double movingBallXPosition = 0;
        double movingBallYPosition = 100;
        boolean testBool;

        //Sjekker om test trigges når ball er til venstre for skjermen
        movingBallXPosition = 0;
        movingBallYPosition = 100;

        testBool = movingBall.leftHit(movingBallXPosition, backgroundXMinPos, backgroundXMaxPos, movingBallRadius, movingBallXPosition, movingBallYPosition);
        Assertions.assertEquals(true, testBool);

        //Sjekker om test trigges når ball er til høyre for skjermen
        movingBallXPosition = 600;
        movingBallYPosition = 100;

        testBool = movingBall.rightHit(movingBallXPosition, backgroundXMinPos, backgroundXMaxPos, movingBallRadius, movingBallXPosition, movingBallYPosition);
        Assertions.assertEquals(true, testBool);

        //Sjekker om test trigges når ball er over skjermen
        movingBallXPosition = 100;
        movingBallYPosition = 0;

        testBool = movingBall.topHit(movingBallYPosition, backgroundYMinPos, backgroundYMaxPos, movingBallRadius);
        Assertions.assertEquals(true, testBool);

        //Sjekker om test trigges når ball er under skjermen
        movingBallXPosition = 100;
        movingBallYPosition = 400;

        testBool = movingBall.bottomHit(movingBallYPosition, backgroundYMinPos, backgroundYMaxPos, movingBallRadius);
        Assertions.assertEquals(true, testBool);
    }

    @Test
    @DisplayName("Sjekke kollisjon mellom ball og rektangel")
    public void testBallRectCollision() {

        MovingBall movingBall = new MovingBall();

        double playerRectYPosition = 353;
        double movingBallRadius = 11;
        double movingBallYPosition;
        boolean testBool;

        //Sjekker at testkollisjon trigges når kollisjon skjer
        movingBallYPosition = 353;
        testBool = movingBall.rectHit(movingBallYPosition, playerRectYPosition, movingBallRadius);

        Assertions.assertEquals(true, testBool);

        //Sjekker at testkollisjon ikke trigges når kollisjon ikke skjer
        movingBallYPosition = 400;
        testBool = movingBall.rectHit(movingBallYPosition, playerRectYPosition, movingBallRadius);

        Assertions.assertEquals(false, testBool);
    }

    @Test
    @DisplayName("Tester kollisjon mellom ball og en brick")
    public void testBallBrickCollision() {

        MovingBall movingBall = new MovingBall();

        double movingBallRadius = 11;
        double brickWidth = 30;
        double brickHeight = 30;
        Rectangle brick = new Rectangle(brickWidth, brickHeight, brickWidth, brickHeight);
        double movingBallXPosition;
        double movingBallYPosition;
        boolean testBool;

        //Sjekker at kollisjon med topp av brick fungerer
        movingBallXPosition = brickWidth + movingBallRadius;
        movingBallYPosition = brickHeight - movingBallRadius;

        testBool = movingBall.topOrBottomBrickHit(movingBallYPosition, movingBallRadius, brick, brickHeight);
        Assertions.assertEquals(true, testBool);

        //Sjekker at kollisjon med bunn av brick fungerer
        movingBallXPosition = brickWidth*2 + movingBallRadius;
        movingBallYPosition = brickHeight + movingBallRadius;

        testBool = movingBall.topOrBottomBrickHit(movingBallYPosition, movingBallRadius, brick, brickHeight);
        Assertions.assertEquals(true, testBool);

        //Sjekker at kollisjon med venstre av brick fungerer
        movingBallXPosition = brickWidth - movingBallRadius;
        movingBallYPosition = brickHeight;

        testBool = movingBall.leftOrRightBrickHit(movingBallXPosition, movingBallRadius, brick, brickWidth);
        Assertions.assertEquals(true, testBool);

        //Sjekker at kollisjon med høyre av brick fungerer
        movingBallXPosition = brickWidth*2 + movingBallRadius;
        movingBallYPosition = brickHeight;

        testBool = movingBall.leftOrRightBrickHit(movingBallXPosition, movingBallRadius, brick, brickWidth);
        Assertions.assertEquals(true, testBool);
    }

}

