package BrickBreaker;

public class PlayerRect implements CollisionInterface {
    
    public boolean insideBounds(double backgroundXMinPos, double backgroundXMaxPos, double playerRectWidth, double MouseXPos) {
        if (MouseXPos >= backgroundXMinPos + (playerRectWidth / 2)
        && MouseXPos <= (MouseXPos + backgroundXMaxPos - (playerRectWidth / 2))) {
            return true;
        }
        return false;
    }

    public boolean leftHit(double movingBallXPosition, double backgroundXMinPos, double backgroundXMaxPos, double movingBallRadius, double playerRectWidth, double MouseXPos) {
        if (MouseXPos < backgroundXMinPos + playerRectWidth / 2) {
            return true;
        }
        return false;
    }

    public boolean rightHit(double movingBallXPosition, double backgroundXMinPos, double backgroundXMaxPos, double movingBallRadius, double playerRectWidth, double MouseXPos) {
        if (MouseXPos > ((backgroundXMinPos + backgroundXMaxPos)) - (playerRectWidth / 2)) {
            return true;
        }
        return false;
    }
}
