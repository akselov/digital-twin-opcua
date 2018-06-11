# Digital Twin with OPC UA
This repository contains files used in the development of a digital twin (DTw) for a robot cell at NTNU with the use of Visual Components 4.0 (VC 4.0) and OPC UA.

**Project report:** < Link is coming >

## Result
The system currently contains the following functionality:
1. Programming, planning and control in VC 4.0
2. Mirror movements of physical robot cell in real-time using KUKAVARPROXY (KVP), <br />
average read time 8.75ms
3. Mirror movements of physical robot cell in real-time using RSI, <br />
average read time 4.00ms
4. Plot sensor data for every KUKA robot:
   * Velocity of the motors controlling all axes
   * Torque on the motors controlling all axes
   * Current to the motors controlling all axes
   * Temperature of the motors controlling all axes
5. Write sensor data for every KUKA robot to .csv files:
   * Velocity to file
   * Torque to file
   * Current to file
   * Temperature to file
6. GUI for access to functionalities
7. Extraction of physical robot properties through XML
8. Controlling physical robots from VC 4.0 using KVP

It was done a fixed case with camera tracking to illustrate some of the functionalities of the DTw: <br /> 
https://youtu.be/xlQhQPmJwlA

## Needs improvement
- [ ] MDA approach to OPC UA information model design
- [ ] Avoid middleware between PC running VC 4.0 and KR C4 robot controllers
- [ ] Remove lag in control of robots through VC 4.0 using KVP
- [ ] Development of an OPC UA server controlling robots through VC 4.0, using RSI
- [ ] Move plotting of data, and camera stream, from middleware to VC 4.0
- [ ] Develop a more accurate calibrated model of the robot cell
- [ ] Gather all functionalities in one server
- [ ] Add sharing of sensor data to online databases

For more information about improvements, see the project report section 5.1.2: <br />
< Link is coming >

---

## About Project
This project was initiated by the Norwegian University of Science and Technology (NTNU), Department of Production Technology. The aim of the project was the following:

In order to respond quickly to unexpected events and new demands without extensive system changes, future production systems must be able to work more independently. There is a need for intelligent machines that perform complex tasks without detailed programming and without human interaction. Autonomous systems know their own abilities (which are modeled as "skills") and their state. They are able to choose between a set of possible actions, orchestrating and perform their skills. To succeed, the autonomous systems need to have realistic models of the current state of the production process and the system's own behaviour in interaction with its external environment - usually called a digital twin. 

OPC Unified Architecture (OPC UA) is a platform-independent protocol for machine-to-machine communication for industrial automation developed by the OPC Foundation. OPC focuses on accessing large amounts of real-time data at the same time as system performance is affected to a minimum. OPC UA has the potential to become an important foundation in the future industrial environment where machines deliver "production as a service", and all machines and sensors in production are online (Internet of Things).

In this task, implementation of a digital twin will be studied. A solution must be developed that provides seamless communication between robots, PLSs, and other relevant control systems that can be part of an industrial production system, linked to one digital representation of the system. The system must be tested at the institute Robot Laboratory.

**a)** Describe how a digital twin can be implemented using OPC UA.

**b)** Examine the advantages and disadvantages of using Visual Components 4.0 or similar simulation software <br />
   for the digital twin.
   
**c)** Use Visual Components 4.0 or similar simulation software to model and simulate the robot cell at the Institute.

**d)** Examine the advantages and disadvantages of using KUKAVARPROXY and KUKA RSI Ethernet as middleware <br />
   between the KUKA KR C4 robot controllers and the digital twin.
   
**e)** Present a solution for a digital twin of the Institute's Robot Laboratory with use of OPC UA for communication.

**f)** Try out the system in a fixed case. Evaluate the results.

## Abstract
This project explores the term Industry 4.0 (I 4.0) and the use of Digital Twins (DTws) as an asset in this modern industrial revolution. A DTw can be described as a digital replica of a physical system including data about this systems interaction with its environment. The goal of this project has been to develop a DTw for a robot cell at MTP Valgrinda, NTNU, and investigate what benefits could be gained from introducing the technology in this system and the domain of automated robotic systems in general. 

The DTw was developed using the OPC UA communication architecture. OPC UA is called the pioneer of I 4.0 as it is a communication architecture aiming at the standardization of communication in industry. Three different visualization software solutions were compared. It was concluded that Visual Components 4.0 (VC 4.0) was the strongest candidate for developing a visual representation of the robot cell. Using VC 4.0 and OPC UA a DTw of the robot cell was created. 

Most of the work done in this project revolved around creating communication modules able to connect the physical robot cell to the virtual representation in VC 4.0, through the use of OPC UA. The result of this work is a communication library containing the virtual representation of the robot cell and the different communication modules able to give the DTw various functionalities. This library is made open-source and can be found in the project's GitHub repository at 

https://github.com/akselov/digital-twin-opcua

The software included in this library enables the DTw to do real-time mirroring of the physical robot's movements, plotting of robotic sensor data and controlling the robots from the DTw. A graphical user interface was developed to organize the functionalities. Figure 2 illustrates the current communication architecture for the DTw.

The DTw was tested in a fixed case. This was a multi-robot case including mirroring of movements, plotting of sensor data and control from VC 4.0. It was made a video from the case which is published online at

https://youtu.be/xlQhQPmJwlA

It was concluded that OPC UA was a good solution for use in this DTw as it is possible to implement on any platform, and is enabling a more flexible and structured way of communicating than traditional communication software used in client/server based systems. The benefits found with the use of the DTw in this automated robotic system included visibility to operations and a better foundation for statistical analysis to predict future states and for optimizing characteristic parameters associated with the robot cell. Finally, it was concluded that the DTw act as a good foundation for managing a complex system, something that could be beneficial as this specific system is used in training and professional development at the institute.

![Figure 1](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/Physical_%26_digital_model.png)
<br />**Figure 1: Robot cell, physical and digital**


![Figure 2](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/InformationFlow.png)
**Figure 2: Current communication architecture**


## Python GUI
GUI for launching Visual Components 4.0 OPC UA connection, displaying robot sensor data and writing data to .csv files.

![Figure 3](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/gui_full.png)
**Figure 3: GUI**

## Visual Components 4.0
This library contains two files of the robot cell from VC 4.0:
1. *Robot_Cell_MTP.vcmx*, Version compatible with all servers developed, <br />
including one example OPC UA client set up for controlling the KUKA KR 16-2 from VC 4.0
2. *Robot_Cell_MTP_case.vcmx*, Version used in the fixed case, including two OPC UA clients set up. <br />
One for controlling the KUKA KR 120 R2500 from VC 4.0, and one for extracting the axis variables <br />
from the KUKA KR 16-2 and mirror its movements in VC 4.0.

![Figure 4](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/VCmodel.png)
**Figure 4: Screenshot from VC 4.0**

## Acknowledgments
The software developed in this project is based on work by: 
- Massimiliano Fago (https://sourceforge.net/projects/openshowvar)
- Mechatronics Lab at AAlesund University College (https://github.com/aauc-mechlab/JOpenShowVar)
- Ahmad Saeed (https://github.com/akselov/kukavarproxy-msg-format)
- Olivier Roulet-Dubonnet (https://github.com/FreeOpcUa)
- Torstein Anderssen Myhre (https://github.com/torstem/examplecode-kukarsi-python)
