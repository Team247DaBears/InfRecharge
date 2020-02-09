package frc.robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;
import java.lang.reflect.*;
import java.util.regex.Pattern;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class RobotTestModes
{
    public static boolean testJoysticks(UserInput userinput)
    {
        boolean joystickBoolean = true;
        String pattern = "[Jj]oystick[.\\.]*";

        // Now create matcher object.
        try{
            Field[] fields = userinput.getClass().getDeclaredFields();
            for(Field field : fields){
                if (Pattern.matches(pattern,field.getName())) {
                    SmartDashboard.putString("Testing:","Move Joystick " + field.getName());
                    Method[] methods = userinput.getClass().getDeclaredMethods();
                    for(Method method : methods){
                        if (Pattern.matches(".*"+field.getName(),method.getName())) {
                            Object resp = 0;
                            while((int)resp!=0){
                                try {
                                    resp = method.invoke(resp);
                                }
                                catch (Exception e) {
                                    System.err.println(e.getMessage());    
                                    System.err.println(e.getStackTrace());    
                                }
                            }
                        if ((int)resp == 0) {
                            joystickBoolean = false;
                            SmartDashboard.putString("Testing:","Joystick " + field.getName()+" not found!");
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        return joystickBoolean;
    }

    public static boolean testDevices(Devices devices){
        boolean devices_boolean = true;
        devices_boolean = devices_boolean && Devices.frontLeft.testEncoder();
        devices_boolean = devices_boolean && Devices.frontRight.testEncoder();
        devices_boolean = devices_boolean && Devices.backLeft.testEncoder();
        devices_boolean = devices_boolean && Devices.backRight.testEncoder();
        
        return devices_boolean;
    }
}

  
