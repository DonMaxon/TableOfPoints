package functions;

import static java.lang.System.arraycopy;

import java.io.Serializable;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.Objects;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable ,Externalizable, Cloneable {
    private FunctionPoint [] points_;
    private int capacity;

    public ArrayTabulatedFunction(){
        points_= new FunctionPoint[0];
        capacity = 0;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if (leftX >= rightX){
            throw new IllegalArgumentException ();
        }
        points_= new FunctionPoint[2*pointsCount];
        capacity = pointsCount;
        double interval = (rightX-leftX)/(pointsCount-1);
        for (int i = 0; i < pointsCount; ++i) {
            points_[i] = new FunctionPoint(leftX + interval * i, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, FunctionPoint[] values){
        if (leftX >= rightX){
            throw new IllegalArgumentException ();
        }
        points_= new FunctionPoint[2*values.length];
        capacity = values.length;
        if (values.length < 2){
            throw new IllegalArgumentException();
        }
        points_[0] = new FunctionPoint(values[0]);
        for (int i = 1; i < values.length; ++i) {
            points_[i] = new FunctionPoint(values[i]);
            if (points_[i].x_<=points_[i-1].x_){
                throw new IllegalArgumentException();
            }
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        if (leftX>=rightX){
            throw new IllegalArgumentException ();
        }
        points_= new FunctionPoint[2*values.length];
        capacity = values.length;
        double interval = (rightX-leftX)/(values.length-1);
        for (int i = 0; i < values.length; ++i) {
            points_[i] = new FunctionPoint(leftX + interval * i, values[i]);
        }
    }

    @Override
    public double getLeftDomainBorder(){
        return points_[0].x_;
    }

    @Override
    public double getRightDomainBorder(){
        return points_[capacity-1].x_;
    }

    @Override
    public double getFunctionValue(double x){
        if (!(getRightDomainBorder()>=x && getLeftDomainBorder()<=x)){
            return Double.NaN;
        }
        int L = -1;
        int R = capacity;
        int M = (L+R)/2;
        while (R-L>1){
            if (x>points_[M].x_){
                L=M;
            }
            else{
                R=M;
            }
            M = (L+R)/2;
        }
        if (x==points_[R].x_){
            return points_[R].y_;
        }
        else{
            return points_[L].y_+(x-points_[L].x_)*(points_[R].y_-points_[L].y_)/(points_[R].x_-points_[L].x_);
        }
    }

    @Override
    public int getPointsCount(){
        return capacity;
    }

    @Override
    public FunctionPoint getPoint(int index){
        if (index < 0 | index > capacity - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        return new FunctionPoint(points_[index]);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 | index > capacity - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        if (index == capacity-1 ) {
            if (points_[index-1].x_<point.x_) {
                points_[index] = point;
                return;
            }
            throw new InappropriateFunctionPointException();
        }
        if (index == 0){
            if (points_[index+1].x_>point.x_){
                points_[index] = point;
                return;
            }
            throw new InappropriateFunctionPointException();
        }
        if (points_[index-1].x_<point.x_ && point.x_<points_[index+1].x_){
            points_[index]=point;
            return;
        }
        throw new InappropriateFunctionPointException();
    }

    @Override
    public double getPointX(int index){
        if (index < 0 | index > capacity - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        return points_[index].x_;
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 | index > capacity - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        if (index == capacity-1 ) {
            if (points_[index-1].x_<x) {
                points_[index].x_=x;
                points_[index].y_=points_[index].y_;
                return;
            }
            throw new InappropriateFunctionPointException();
        }
        if (index == 0){
            if (points_[index+1].x_>x){
                points_[index].x_=x;
                points_[index].y_=points_[index].y_;
                return;
            }

            throw new InappropriateFunctionPointException();
        }
        if (points_[index-1].x_<x && x<points_[index+1].x_){
            points_[index].x_=x;
            points_[index].y_=points_[index].y_;
            return;
        }
        throw new InappropriateFunctionPointException();
    }

    @Override
    public double getPointY(int index){
        if (index < 0 | index > capacity - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        return points_[index].y_;
    }

    @Override
    public void setPointY(int index, double y){
        if (index < 0 | index > capacity - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        points_[index].y_=y;
    }

    @Override
    public void deletePoint(int index){
        if (capacity<3){
            throw new IllegalStateException();
        }
        if (index < 0 | index > capacity - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        if ((capacity-1)*4 <= points_.length){
            FunctionPoint [] newPointsArray = new FunctionPoint[points_.length/2];
            arraycopy(points_, 0, newPointsArray, 0, index);
            arraycopy(points_, index+1, newPointsArray, index, capacity-index-1);
            points_ = newPointsArray;
        }
        else{
            arraycopy(points_, index+1, points_, index, capacity-index-1);
        }
        --capacity;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int pos = 0;
        int L = -1;
        int R = capacity;
        int M = (L+R)/2;
        while (R-L>1){
            if (point.x_>points_[M].x_){
                L=M;
            }
            else{
                R=M;
            }
            M = (L+R)/2;
        }
        pos = R;
        if (pos < capacity && point.x_==points_[pos].x_){
            throw new InappropriateFunctionPointException();
        }
        if (capacity+1==points_.length){
            FunctionPoint [] newPointsArray = new FunctionPoint[points_.length*2];
            arraycopy(points_, 0, newPointsArray, 0, pos);
            arraycopy(points_, pos, newPointsArray, pos+1, capacity-pos);
            newPointsArray[pos]=point;
            points_ = newPointsArray;
        }
        else{
            arraycopy(points_, 0, points_, 0, pos);
            arraycopy(points_, pos, points_, pos+1, capacity-pos);
            points_[pos]=point;
        }
        ++capacity;

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(capacity);
        out.writeObject(points_);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        capacity = (int)in.readInt();
        points_ = new FunctionPoint[capacity];
        points_= (FunctionPoint[])in.readObject();
    }

    @Override
    public String toString() {
        String result = "";
        result+=points_[0];
        for (int i= 1; i< capacity; ++i){
            result+=", "+points_[i].toString();
        }
        return "{" + result +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayTabulatedFunction returning = (ArrayTabulatedFunction)super.clone();
        returning.points_ = new FunctionPoint[2*capacity];
        for (int i =0; i < capacity; ++i){
            returning.points_[i]=points_[i].clone();
        }
        return returning;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null
                || !(o instanceof TabulatedFunction)
                || (capacity != ((TabulatedFunction) o).getPointsCount())) return false;
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction that = (ArrayTabulatedFunction) o;
            return capacity == that.capacity &&
                    Arrays.equals(points_, that.points_);
        }
        else{
            LinkedListTabulatedFunction that = (LinkedListTabulatedFunction) o;
            for (int i = 0; i < capacity; ++i){
                if (!points_[i].equals(that.getPoint(i))){
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(capacity);
        for (int i = 0; i < capacity; ++i){
            result = result*(int)Math.pow(31, i) + points_[i].hashCode();
        }
        return result;
    }
}
