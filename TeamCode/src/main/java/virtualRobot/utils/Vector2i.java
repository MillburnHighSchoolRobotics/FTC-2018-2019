package virtualRobot.utils;

/**
 * Created by ethachu19 on 12/8/2016.
 */

public class Vector2i {
    public int x;
    public int y;

    public Vector2i() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Creates a vector based off of 2 points.
     * @param x
     * @param y
     */
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Creates a new 2d vector based off another.
     * @param v Vector to be copied
     */
    public Vector2i(Vector2i v){
        this.x = v.x;
        this.y = v.y;
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
    public synchronized double length() {
        return Math.sqrt((x*x) + (y*y));
    }
}
