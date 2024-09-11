package BrickBreaker;

public interface CollisionInterface {
    
    boolean rightHit(double movingBallXPosition, double backgroundXMinPos, double backgroundXMaxPos, double movingBallRadius, double playerRectWidth, double MouseXPos);

    boolean leftHit(double movingBallXPosition, double backgroundXMinPos, double backgroundXMaxPos, double movingBallRadius, double playerRectWidth, double MouseXPos);
}
