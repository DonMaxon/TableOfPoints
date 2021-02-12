package functions;

import java.io.*;

public class TabulatedFunctions {

    private TabulatedFunctions(){}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        TabulatedFunction tabFunc = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        double interval = (rightX-leftX)/(pointsCount-1);
        for (int i = 0; i < pointsCount; ++i) {
            try {
                tabFunc.setPoint(i, new FunctionPoint(leftX + interval * i,
                        function.getFunctionValue(leftX + interval * i)));
            }
            catch (InappropriateFunctionPointException exception){
                System.out.println("");
            }
        }
        return tabFunc;
    }


    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
       PrintWriter pwout = new PrintWriter(out);
       pwout.println(function.getPointsCount());
       for (int i = 0; i < function.getPointsCount(); ++i) {
           pwout.println(function.getPointX(i));
           pwout.println(function.getPointY(i));
       }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer streamTokenizer = new StreamTokenizer(in);
        streamTokenizer.nextToken();
        double size = streamTokenizer.nval;
        FunctionPoint[] funcPoints = new FunctionPoint[(int)size];
        for (int i = 0; i < size; ++ i){
            streamTokenizer.nextToken();
            double x = streamTokenizer.nval;
            streamTokenizer.nextToken();
            double y = streamTokenizer.nval;
            funcPoints[i] = new FunctionPoint(x, y);
        }
        TabulatedFunction returning = new ArrayTabulatedFunction(funcPoints[0].x_,
                funcPoints[funcPoints.length-1].x_, funcPoints);
        return returning;
    }



}
