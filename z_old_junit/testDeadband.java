package frc.robot;
import frc.robot.UserInput;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testDeadband{
  @Test
  public void test(){
     

    Assert.assertEquals(0.015,UserInput.getDeadband(.3),.025);
    Assert.assertEquals(0.06,UserInput.getDeadband(.4),.026);
    Assert.assertEquals(0.14,UserInput.getDeadband(.5),.025);
    Assert.assertEquals(-0.015,UserInput.getDeadband(-.3),.025);
    Assert.assertEquals(-0.06,UserInput.getDeadband(-.4),.026);
    Assert.assertEquals(-0.14,UserInput.getDeadband(-.5),.025);
        Assert.assertEquals(0,UserInput.getDeadband(-.16),.0);
        Assert.assertEquals(-.01,UserInput.getDeadband(-.21),.04);
        Assert.assertEquals(-0.76,UserInput.getDeadband(-.9),.025);
        Assert.assertEquals(0.87,UserInput.getDeadband(.95),.0225);
       
      }
      
      

}