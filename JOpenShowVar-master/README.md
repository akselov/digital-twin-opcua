JOpenShowVar
============

JOpenShowVar is a Java open-source cross-platform communication interface to Kuka robots that allows for 
reading and writing variables and data structures of the controlled manipulators. This interface, which is
compatible with all Kuka robots that uses the KR C4 controller, runs as a client on a remote computer 
connected with the Kuka controller via TCP/IP. JOpenShowVar opens up to a variety of possible applications 
making it possible to use different input devices and to develop alternative control methods.

JOpenShowVar may be used to connect to a real KRC controller or a simulated one using the KUKA.OfficeLite package.

Demo video showing a KUKA KR6 R900 sixx being controlled using JOpenShowVar as the communication interface:
https://www.youtube.com/watch?v=6aZZAK4oyGg

References
===========
* Filippo Sanfilippo, Lars Ivar Hatledal, Houxiang Zhang, Massimiliano Fago and Kristin Ytterstad Pettersen. Controlling Kuka Industrial Robots: Flexible Communication Interface JOpenShowVar. IEEE Robotics & Automation Magazine 22(4):96-109, December 2015.

* Filippo Sanfilippo, Lars Ivar Hatledal, Houxiang Zhang, Massimiliano Fago and Kristin Ytterstad Pettersen. JOpenShowVar: an Open-Source Cross-Platform Communication Interface to Kuka Robots. In Proceeding of the IEEE International Conference on Information and Automation (ICIA), Hailar, China. 2014, 1154–1159.

Changelog
===========
0.1 -> 0.2:
* The sendRequest method, and the Request and Callback classes are marked as deprecated, because you should use the read and writeVaribale methods instead. However, feel free to use the method if you don't know the dataype that will be returned. 
* Introduced the read and writeVariable methods that in most cases will replace sendRequest.
* Added a convenience method for reading the joint angles and the joint torques (readJointAngles() and readJointTorques())

0.2 -> 0.2.1 
* Added Enum datatype support 
* Some internal code clean-up 
 
 0.2.1 -> 0.2.2
 * Added simpleRead and simpleWrite
 * Some consistency changes for the KRLStruct classes


Usage
=========
KUKAVARPROXY must firstly be started on the KUKA SmartPad (copy paste the folder to somewhere in the WinXP environment -> run KUKAVARPROXY.exe)
Port 7000 has to set open from the SmartPad:
Start-up -> Network configuration -> NAT -> Add port -> Port number 7000 and Permitted protocols: tcp/udp 

In order to successfully establish an connection to the server. Your IP must be assigned a static IP in the same subrange as the one defined in the the SmartPads network configuration.

Console application
============
JOpenShowVar-core features a simple console application which can be invoked by writing -java -jar JopenShowVar-core ipAddress port 


Example code v0.1
===========

```java
public class Example {

	private static String robotIP = "192.168.2.2";  //The static IP of the robot 
	private static int port = 7000;

	public static void main(String[] args) throws IOException {
		CrosscomClient client = new CrossComClient(robotIP, port);  //establish connection
		
		//Reads the current value of $OV_JOG and print the callback containing the value
		Callback readRequest = client.sendRequest(new Request(0, "$OV_JOG")); //read request
		System.out.println(readRequest);
		
		//Set $OV_JOG to 100% and print the callback
		Callback writeRequest = client.sendRequest(new Request(1, "$OV_JOG", "100")); //write request
		System.out.println(writeRequest);
		
		//note that the first int argument can be any number between 0 and 99. It is simply an id given to the request so that it can be tracked. 
	}

}
```


Example code v0.2.2
===========
```java
public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
        try (CrossComClient client = new CrossComClient("158.38.140.193", 7000)) {

            KRLPos pos = new KRLPos("MYPOS").setX(2).setY(1);  //MYPOS is defined manually in $config.dat
            client.writeVariable(pos);
            System.out.println(pos);

            client.readVariable(pos);
            System.out.println(pos);

            KRLE6Axis axisAct = KRLVariable.AXIS_ACT(); // the same as new KRLE6Axis($AXIS_ACT)
            client.readVariable(axisAct);
            System.out.println(axisAct);

            KRLReal jog = KRLVariable.OV_JOG(); // the same as new KRLReal($OV_JOG)
            client.readVariable(jog);
            System.out.println(jog);

            KRLReal pro = KRLVariable.OV_PRO();
            client.readVariable(pro);
            System.out.println(pro);

            for (int i = 0; i < 100; i++) {
                pro.setValue(i);
               client.writeVariable(pro);
                System.out.println(pro);
                Thread.sleep(10);
            }

            KRLBool out1 = new KRLBool("$OUT[1]");
            client.readVariable(out1);
            System.out.println(out1);
            
            KRLEnum mode = new KRLEnum("$MODE_OP");
            client.readVariable(mode);
            System.out.println(mode);
            
            System.out.println(Arrays.toString(client.readJointAngles()));
			
			System.out.println(client.simpleRead("$OV_JOG"));
			System.out.println(client.simpleWrite("$OV_JOG", "90"));
			
            
        }
    }
}
```

Comparison between v0.1 and v0.2
=================
```java
public class Test2 {

    /**
     * Comparison between v0.1 and v0.2
     *
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        try (CrossComClient client = new CrossComClient("158.38.141.32", 7000)) {

            //v0.1 read
            Callback readRequest = client.sendRequest(new Request(0, "$OV_JOG")); 
            System.out.println(readRequest);

            //v0.1 write
            Callback writeRequest = client.sendRequest(new Request(1, "$OV_JOG", "100")); 
            System.out.println(writeRequest);

            //v0.2 read
            KRLReal jog = KRLVariable.OV_JOG();
            client.readVariable(jog);
            System.out.println(jog);

            //v0.2 write
            jog.setValue(10);
            client.writeVariable(jog);
            System.out.println(jog);

        }
    }
}
```

Repository contents
==================
The repository contains 3 projects.
JOpenShowVar-core is the core project and contains the important stuff.
JOpenShowVar-swing provides a GUI interface and references the core project
JOpenShowVar-android is a simple android project that also references the core project

libs contains 3rd party libraries used by the swing project

KUKAVARPROXY contains the files that must be copied over to the WinXP environment on the KUKA SmartPad.

Acknowledgements
==============
The JOpenShowVar-master repository was originally posted by the Mechatronics Lab at Ålesund University College (https://github.com/aauc-mechlab/JOpenShowVar). The version posted in this project was aquired from this site 09.05.2018.

A big thanks to Massimiliano Fago, which is the original author of KUKAVARPROXY and the C++ project OpenShowVar (http://sourceforge.net/projects/openshowvar/)
