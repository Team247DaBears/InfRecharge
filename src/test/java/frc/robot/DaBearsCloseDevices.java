package frc.robot;

import java.lang.reflect.*;

public class DaBearsCloseDevices {  

 
 
 public static void close (Object devices) {
        Field[] fields = devices.getClass().getFields();
        for(Field field : fields){
          Method[] methods=null;
          try {
            methods = field.get(devices).getClass().getDeclaredMethods();
          }
          catch (Exception e){
            e.printStackTrace();
          }
            for(Method method : methods){
            if (method.getName()=="close") {
                try {
                  if (field.get(devices)!=null) {
                    //System.out.println("close:"+devices.getClass().getName()+":"+field.getName());
                    method.invoke(field.get(devices));
                  }                  
                } 
                catch (Exception e) {
                  // TODO Auto-generated catch block
                  System.err.println("invocation of close failed: ");
                }
              field = null;
            }
          }
        }
      }
    }