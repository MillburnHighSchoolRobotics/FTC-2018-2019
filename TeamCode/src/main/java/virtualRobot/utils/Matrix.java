package virtualRobot.utils;

import java.util.IllegalFormatException;

/**
 * Created by ethachu19 on 10/28/2016.
 *
 * Beware only use in internal calculations
 */

public class Matrix {
    public double arr[][];

    public Matrix() {
        arr = new double[3][3];
    }

    public Matrix(double[][] arr) {
        this.arr = new double[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i],0,this.arr[i],0,arr[i].length);
        }
    }

    public Matrix(int length, int width) {
        arr = new double[length][width];
    }

    public Matrix(Matrix x) {
        this(x.arr);
    }

    public synchronized int length() { return arr.length; }

    public synchronized int width() { return arr[0].length; }

    public synchronized Matrix multiply(Vector3f v) {
        if (arr[0].length != 3)
            throw new IllegalArgumentException("Matrix is not width of 3");
        Matrix result = new Matrix(length(),1);
        for(int i = 0; i < length(); i++) {
            for(int j = 0; j < 3; j++) {
                result.arr[i][0] += v.getArr()[j] * arr[i][j];
            }
        }
        return result;
    }

    public synchronized Matrix add(Matrix m) {
        if (arr.length != m.arr.length || arr[0].length != m.arr[0].length) {
            throw new IllegalArgumentException("Matrix is not same size");
        }
        Matrix result = new Matrix(length(),width());
        for(int i = 0; i < length(); i++) {
            for(int j = 0; j < width(); j++) {
                result.arr[i][j] = arr[i][j] + m.arr[i][j];
            }
        }
        return result;
    }

    public synchronized Matrix multiply(Matrix m) {
        if (width() != m.length()) {
            throw new IllegalArgumentException("Matrix rows not equal to columns");
        }
        Matrix result = new Matrix(length(),m.width());

        return result;
    }

    public synchronized Matrix scale(double scalar) {
        Matrix result = new Matrix(this);
        for (int i = 0; i < arr.length; i++) {
             for (int j = 0; j < arr[0].length; j++) {
                 result.arr[i][j] *= scalar;
             }
        }
        return result;
    }

    public synchronized double determinant() {
        double result[][] = getArr();
        double determinant1 = 0, determinant2 = 0;
        for (int i = 0; i < 3; i++) {
            double temp = 1, temp2 = 1;
            for (int j = 0; j < 3; j++) {
                temp *= result[(i + j) % 3][j];
                temp2 *= result[(i + j) % 3][3 - 1 - j];
            }
            determinant1 += temp;
            determinant2 += temp2;
        }
        return determinant1 - determinant2;
    }

    public synchronized double[][] getArr() {
        return arr;
    }

    public synchronized double access(int x, int y) {
        return arr[x][y];
    }

    public synchronized Vector3f toVector3f(int offset) {
        if (arr[0].length != 1) {
            throw new IllegalArgumentException("Matrix is not right size: " + arr.length + "x" + arr[0].length);
        }
        return new Vector3f(arr[0+offset][0],arr[1+offset][0],arr[2+offset][0]);
    }

    public synchronized Vector2f toVector2f(int offset) {
        if (arr[0].length != 1) {
            throw new IllegalArgumentException("Matrix is not right size: " + arr.length + "x" + arr[0].length);
        }
        return new Vector2f(arr[0+offset][0],arr[1+offset][0]);
    }

    public static Matrix diagonalFill(int size, double fill) {
        Matrix res = new Matrix(size,size);
        for (int i = 0; i < size; i++) {
            res.arr[i][i] = fill;
        }
        return res;
    }

    public static Matrix identity(int size) { return diagonalFill(size, 1); }
}
