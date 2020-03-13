package frc.robot;



import java.lang.Math.*;

public class Victor extends edu.wpi.first.wpilibj.Victor {
    public Victor(int channel) {
        super(channel);
        // TODO Auto-generated constructor stub
    }

    double currentPosition = 0;
    double Position = 0;
    double driveSpeed = 0;
    boolean Inverted = false;

    @Override
    public void set(double speed) {
        driveSpeed = speed;
    }
    @Override
    public double get(){
        return driveSpeed;
    }
    @Override
    public void setInverted(boolean inverted){
        Inverted = inverted;
    }
    @Override
    public boolean getInverted(){
        return Inverted;
    }
    @Override
    public void close() {
        super.close();
        System.out.println("closed a Victor");
        return;
    }
}