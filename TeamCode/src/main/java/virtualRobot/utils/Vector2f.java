package virtualRobot.utils;

/**
 * Created by ethachu19 on 10/28/2016.
 */

public class Vector2f {
    public double x;
    public double y;

    /**
     * Creates a default 2d vector with all values set to 0.
     */
    public Vector2f() {
        this.x = 0f;
        this.y = 0f;
    }

    /**
     * Creates a vector based off of 2 points.
     * @param x
     * @param y
     */
    public Vector2f(double x, double y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Creates a new 2d vector based off another.
     * @param v Vector to be copied
     */
    public Vector2f(Vector2f v){
        this.x = v.x;
        this.y = v.y;
    }
    /**
     * Gets the distance between two vectors.
     * @param v
     * @return
     */

    public synchronized double getDistance(Vector2f v){
        return (float) Math.sqrt((this.x * v.x)+(this.y * v.y));

    }

    @Override
    public String toString(){
        return this.x + "," + this.y;
    }

    /*
    Returns the degree of angle of vector
    */
    public synchronized double getAngle() {
        return Math.toDegrees(Math.atan2(y, x));
    }

    /*
    Returns magnitude of vector
    */
    public synchronized double getMagnitude() {
        return Math.sqrt((x*x) + (y*y));
    }
}
