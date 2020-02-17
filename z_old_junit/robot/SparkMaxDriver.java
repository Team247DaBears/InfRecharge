package frc.robot;

public class SparkMaxDriver  {
    public  double Direction=0;
    public  double Speed=0;

    public  void set(double speed) {
        Speed = speed;
    }

    public double get() {
        return Speed;
    }
}