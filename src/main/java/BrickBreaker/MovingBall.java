package BrickBreaker;

import javafx.scene.shape.Rectangle;

public class MovingBall implements CollisionInterface{
    
    //Sjekker om ball treffer høyre kant av skjerm
    public boolean rightHit(double movingBallXPosition, double backgroundXMinPos, double backgroundXMaxPos, double movingBallRadius, double playerRectWidth, double MouseXPos) {

        if (movingBallXPosition >= (backgroundXMaxPos - movingBallRadius)) {
            return true;
        }
        return false;
    }

    //Sjekker om ball treffer venstre kant av skjerm
    public boolean leftHit(double movingBallXPosition, double backgroundXMinPos, double backgroundXMaxPos, double movingBallRadius, double playerRectWidth, double MouseXPos) {

        if (movingBallXPosition <= (backgroundXMinPos + movingBallRadius)) {
            return true;
        }
        return false;
    }
    

    //Sjekker om ball treffer topp av skjerm
    public boolean topHit(double movingBallYPosition, double backgroundYMinPos, double backgroundYMaxPos, double movingBallRadius) {

        if (movingBallYPosition <= (backgroundYMinPos + movingBallRadius)) {
            return true;
        }
        return false;
    }

    //Sjekker om ball treffer bunn av skjerm
    public boolean bottomHit(double movingBallYPosition, double backgroundYMinPos, double backgroundYMaxPos, double movingBallRadius) {

        if ((movingBallYPosition >= (backgroundYMaxPos - movingBallRadius))) {
            return true;
        }
        return false;
    }

    //Sjekker om ball treffer venstre eller høyre kant av brick
    public boolean leftOrRightBrickHit(double movingBallXPosition, double movingBallRadius, Rectangle brick, double brickWidth) {

        boolean rightBrick = movingBallXPosition >= brick.getX() + brickWidth - movingBallRadius;
        boolean leftBrick = movingBallXPosition <= brick.getX() + movingBallRadius;

        if (rightBrick || leftBrick) {
            return true;
        }
        return false;
    }

    //Sjekker om ball treffer topp eller bunn av brick
    public boolean topOrBottomBrickHit(double movingBallYPosition, double movingBallRadius, Rectangle brick, double brickHeight) {

        boolean topBrick = movingBallYPosition <= brick.getY() + movingBallRadius;
        boolean bottomBrick = movingBallYPosition >= brick.getY() + brickHeight - movingBallRadius;

        if (topBrick || bottomBrick) {
            return true;
        }
        return false;
    }

    public boolean rectHit(double movingBallYPosition, double playerRectYPosition, double movingBallRadius) {

        boolean top = movingBallYPosition <= playerRectYPosition + movingBallRadius;

        if (top) {
            return true;
        }
        return false;
    }
}
