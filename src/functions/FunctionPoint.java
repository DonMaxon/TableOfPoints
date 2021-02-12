package functions;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.Externalizable;
import java.io.Serializable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

import java.util.Objects;

public class FunctionPoint  implements Serializable, Externalizable, Cloneable {
    double x_;
    double y_;

    public FunctionPoint(double x, double y){
        x_ = x;
        y_ = y;
    }

    public FunctionPoint(FunctionPoint point){
        x_ = point.x_;
        y_ = point.y_;
    }

    public FunctionPoint(){
        x_ = 0;
        y_ = 0;
    }


    public DoubleProperty getPropertyX(){
        return new SimpleDoubleProperty(x_);
    }

    public DoubleProperty getPropertyY(){
        return new SimpleDoubleProperty(y_);
    }

    @Override
    public FunctionPoint clone() throws CloneNotSupportedException {
        return (FunctionPoint) super.clone();
    }

    @Override
    public String toString() {
        return "(" + x_ + ", "+ y_ + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionPoint that = (FunctionPoint) o;
        return that.x_ == x_  && that.y_== y_;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x_, y_);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException{
        out.writeDouble(x_);
        out.writeDouble(y_);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x_ = (double)in.readDouble();
        y_ = (double)in.readDouble();
    }

}
