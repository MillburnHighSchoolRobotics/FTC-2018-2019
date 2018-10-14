package org.firstinspires.ftc.teamcode;

import java.util.*;


public class Spline {
    public static double[][] interpolate(double[] xArr, double[] yArr) {
        LinkedList<double[]> path = new LinkedList<>();
        for (int a = 0; a < xArr.length; a++) {
            path.add(new double[] {xArr[a],yArr[a]});
        }
        //setup
        double[][] matrix = new double[(path.size()-1)*4][(path.size()-1)*4];
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                matrix[x][y] = 0;
            }
        }
        double[] matrixY = new double[matrix.length];
        for (int x = 0; x < matrix.length; x++) {
            matrixY[x] = 0;
        }
        double[][] ranges = new double[path.size()-1][2];
        int row = 0;


        //points
        for (int a = 0; a < path.size()-1; a++) {
            for (int b = 0; b < 2; b++,row++) {
                for (int c = 0; c < 4; c++) {
                    matrix[row][a*4+c] = Math.pow(path.get(a+b)[0],3-c);
                }
                matrixY[row] = path.get(a+b)[1];
                ranges[a][b] = path.get(a+b)[0];
            }
        }


        //first derivative
        for (int a = 0; a < path.size()-2; a++, row++) {
            for (int b = 0; b < 3; b++) {
                matrix[row][a*4+b] = (3-b) * Math.pow(path.get(a+1)[0],2-b);
            }
            for (int b = 4; b < 7; b++) {
                matrix[row][a*4+b] = (b-7) * Math.pow(path.get(a+1)[0],6-b);
            }
        }


        //second derivative
        for (int a = 0; a < path.size()-2; a++, row++) {
            for (int b = 0; b < 2; b++) {
                matrix[row][a*4+b] = (2-b) * (3-b) * Math.pow(path.get(a+1)[0],1-b);
            }
            for (int b = 4; b < 6; b++) {
                matrix[row][a*4+b] = (b-6) * (7-b) * Math.pow(path.get(a+1)[0],5-b);
            }
        }


        //boundary
        matrix[row][0] = 6*path.get(0)[0];
        matrix[row][1] = 2;
        row++;
        matrix[row][matrix.length-4] = 6*path.get(path.size()-1)[0];
        matrix[row][matrix.length-3] = 2;


        //Gaussian Elimination
        double[][] coefficients = GaussianElimination(matrix, matrixY, path);
        return coefficients;
    }
    public static double[][] GaussianElimination(double[][] matrix, double[] matrixY, LinkedList<double[]> path) {
        //Row Echelon Form
        for (int x = 0; x < matrixY.length; x++) {
            int f = x;
            for (int y = x + 1; y < matrixY.length; y++) {
                //check for pivot
                if (Math.abs(matrix[y][x]) > Math.abs(matrix[f][x])) {
                    f = y;
                }
            }

            //switch in matrix and matrixY
            double[] matrixTemp = matrix[x];
            matrix[x] = matrix[f];
            matrix[f] = matrixTemp;
            double matrixYTemp = matrixY[x];
            matrixY[x] = matrixY[f];
            matrixY[f] = matrixYTemp;

            //find the x-th pivot
            for (int y = x + 1; y < matrixY.length; y++) {
                double alpha = matrix[y][x]/matrix[x][x];
                matrixY[y] -= alpha * matrixY[x];
                for (int z = x; z < matrixY.length; z++) {
                    matrix[y][z] -= alpha * matrix[x][z];
                }
            }
        }

        //Find coefficients a,b,c,d for each polynomial piece of the spline
        double[] coeffList = new double[matrixY.length];
        for (int a = matrixY.length-1; a >= 0; a--) {
            double beta = 0.0;
            for (int b = a+1; b < matrixY.length; b++) {
                beta += matrix[a][b]*coeffList[b];
            }
            double difference = matrixY[a] - beta;
            coeffList[a] = difference/matrix[a][a];
        }
        double[][] coeff = new double[path.size()-1][4];
        for (int m = 0; m < path.size()-1; m++) {
            for (int n = 0; n < coeff[m].length; n++) {
                coeff[m][n] = coeffList[(4*m)+n];
            }
        }
        return coeff;
    }
    public static void TwoDimensionalSpline(double[] x, double[] y) {
        ArrayList<Double> time = new ArrayList<>();
        time.add(0.0);
        for (int i = 0; i < x.length-1; i++) {
            double t = time.get(time.size()-1) + Math.sqrt(Math.pow((x[i + 1] - x[i]),2) + Math.pow((y[i + 1] - y[i]),2));
            time.add(t);
        }
        time.add(time.get(time.size()-1) + Math.sqrt(Math.pow((x[x.length-1] - x[0]),2) + Math.pow((y[y.length-1] - y[0]),2)));
        double maxTime = time.get(time.size()-1);
        double[] timeArray = new double[time.size()];
        for (int t = 0; t < time.size(); t++) {
            timeArray[t] = t/maxTime;
        }
        System.out.println(Arrays.toString(timeArray));


        double[] xArray = new double[x.length+1];
        for (int a = 0; a < xArray.length-1; a++) {
            xArray[a] = x[a];
        }
        xArray[xArray.length-1] = x[0];

        double[] yArray = new double[y.length+1];
        for (int a = 0; a < xArray.length-1; a++) {
            yArray[a] = y[a];
        }
        yArray[yArray.length-1] = y[0];

        double[][] x_interpolant = interpolate(timeArray, xArray);
        double[][] y_interpolant = interpolate(timeArray, yArray);

        for (int a = 0; a < x_interpolant.length; a++) {
            for (int b = 0; b < x_interpolant[a].length; b++) {
                System.out.print(x_interpolant[a][b]);
            }
            System.out.println();
        }
        System.out.println("\n");
        System.out.println("\n");

        for (int a = 0; a < y_interpolant.length; a++) {
            for (int b = 0; b < y_interpolant[a].length; b++) {
                System.out.print(y_interpolant[a][b]);
            }
            System.out.println();
        }
    }
    /*public static double getY(double x) {
        //find range
        int range = -1;
        for (int r = 0; r < ranges.length; r++) {
            if (x >= ranges[r][0] && x <= ranges[r][1]) {
                range = r;
            }
        }

        //throw exception if the value is outside the range
        if (range == -1) {
            throw new ArrayIndexOutOfBoundsException("Value is not within the range of the spline");
        }

        //evaluate expression
        double y = evaluate(x, coefficients[range]);
        return y;
    }
    public static double evaluate(double x, double[] coeff) {
        double sum = 0.0;
        for (int s = 0; s < coeff.length; s++) {
            sum += (coeff[s]*Math.pow(x,3-s));
        }
        return sum;
    }
    public static double[] Vector(double[] currentPosition, double movement) {
        //gets displacement vector
        double currentX = currentPosition[0];
        double currentY = currentPosition[1];
        double nextX = currentPosition[0]+movement;
        double nextY = getY(nextX);

        double displacement[] = {(nextX-currentX),(nextY-currentY)};
        return displacement;

    }
    public static double getAngle(double[] currentPosition, double movement) {
        double[] displacement = Vector(currentPosition, movement);
        //finds inverse tangent and gets angle
        double gamma = Math.toDegrees(Math.atan(displacement[1]/displacement[0]));
        return gamma;
    }
    public static double getDistance(double[] currentPosition, double movement) {
        double[] displacement = Vector(currentPosition, movement);
        //finds direct distance
        double delta = Math.sqrt(Math.abs(Math.pow(displacement[1],2) - Math.pow(displacement[0],2)));
        return delta;
    }*/
}