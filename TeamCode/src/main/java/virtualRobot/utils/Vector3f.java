package virtualRobot.utils;

/**
 * Created by ethachu19 on 10/28/2016.
 */

public class Vector3f {
    public double x;
    public double y;
    public double z;

    /**
     * Creates a default 3d vector with all values set to 0.
     */
    public Vector3f() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    /**
     * Creates a vector based off of 3 points.
     *
     * @param x
     * @param y
     * @param z
     */
    public Vector3f(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new vector based off another.
     *
     * @param v Vector to be copied
     */
    public Vector3f(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public static double getDistance(Vector3f v1, Vector3f v2){
        return (float) Math.sqrt(Math.pow((v2.x - v1.x), 2)+Math.pow((v2.y - v1.y), 2)+Math.pow((v2.z - v1.z), 2));
    }

    public synchronized double length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public Vector3f inverse() {
        return new Vector3f(this.x * -1, this.y * -1, this.z * -1);
    }

    public Vector3f reciprocal(){
        return new Vector3f(1/this.x, 1/this.y, 1/this.z);
    }

    private Vector3f addVectors(Vector3f v){
        return new Vector3f(this.x + v.x,this.y+v.y,this.z+v.z);
    }
    public Vector3f add(Vector3f... v){
        Vector3f sum = new Vector3f(this);
        for(Vector3f n :v)
            sum = sum.addVectors(n);
        return sum;
    }

    public Vector3f subtract(Vector3f... v){
        Vector3f sum = new Vector3f(this);
        for(Vector3f n :v)
            sum = sum.addVectors(n.inverse());
        return sum;
    }

    public Vector3f add(float f){
        return new Vector3f(this.x + f, this.y + f, this.z + f);
    }

    public void addEquals(Vector3f ...v) {
        for(Vector3f n :v)
        {
            this.x += n.x;
            this.y += n.y;
            this.z += n.z;
        }
    }

    public Vector3f normalize() {
        return divide(length());
    }

    private double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3f divide(double scalar) {
        if (scalar == 0) throw new ArithmeticException("Divide by 0");
        return multiply(1 / scalar);
    }

    public Vector3f multiply(double scalar) {
        return new Vector3f(x * scalar, y * scalar, z * scalar);
    }

    public Vector3f multiply(Vector3f v) {
        return new Vector3f(x * v.x, y * v.y, z * v.z);
    }

    public void setRadius(float radi) {
        double inclination = getInclination();
        double azimuth = getAzimuth();

        this.x = (radi * Math.sin(Math.toRadians(inclination)) * Math.cos(Math.toRadians(azimuth)));
        this.z = (radi * Math.sin(Math.toRadians(inclination)) * Math.sin(Math.toRadians(azimuth)));
        this.y = (radi * Math.cos(Math.toRadians(inclination)));
    }

    public double getInclination() {
        return (float) Math.toDegrees(Math.acos(y / length()));
    }

    public double getAzimuth() {
        return (float) Math.toDegrees(Math.atan2(z, x));
    }

    public void setInclination(float deg){
        double length = length();
        double azimuth = getAzimuth();

        x = (length * Math.sin(Math.toRadians(deg)) * Math.cos(Math.toRadians(azimuth)));
        y = (length * Math.sin(Math.toRadians(deg)) * Math.sin(Math.toRadians(azimuth)));
        z = (length * Math.cos(Math.toRadians(deg)));
    }

    public void setAzimuth(float deg){
        double length = length();
        double inclination = getInclination();

        x = (length * Math.sin(Math.toRadians(inclination)) * Math.cos(Math.toRadians(deg)));
        z = (length * Math.sin(Math.toRadians(inclination)) * Math.sin(Math.toRadians(deg)));
    }

    public Vector3f closertoZero(float f){
        float signX = x < 0 ? -1 : 1;
        float signY = y < 0 ? -1 : 1;
        float signZ = z < 0 ? -1 : 1;
        this.x = (Math.abs(x) - f)*signX;
        this.y = (Math.abs(y) - f)*signY;
        this.z = (Math.abs(z) - f)*signZ;
        return this;
    }

    public Vector3f closertoZero(Vector3f v){
        float signX = x < 0 ? -1 : 1;
        float signY = y < 0 ? -1 : 1;
        float signZ = z < 0 ? -1 : 1;
        this.x = (Math.abs(x) - v.x)*signX;
        this.y = (Math.abs(y) - v.y)*signY;
        this.z = (Math.abs(z) - v.z)*signZ;
        return this;
    }

    public void zero(){
        this.x = this.y = this.z = 0;
    }

    public double[] getArr() { return new double[]{x,y,z};}

    @Override
    public String toString(){
        return x + ", " + y + ", " + z;
    }

    public double dotProduct(Vector3f x){
        return this.x*x.x + this.y*x.y + this.z*x.z;
    }

    public Matrix toMatix() { return new Matrix(new double[][]{{x},{y},{z}}); }
}
