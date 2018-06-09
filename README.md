# Digital Twin with OPC UA
This repository contains files used in the development of a digital twin (DTw) for a robot cell at NTNU with the use of Visual Components 4.0 (VC 4.0) and OPC UA.

## Result
The system currently cointains the following functionality:
1. Programming, planning and control in VC 4.0
2. Mirror movements of physical robot cell in real-time using KUKAVARPROXY (KVP), <br />
average read time **8.75ms**
3. Mirror movements of physical robot cell in real-time using RSI, <br />
average read time **4.00ms**
4. Plot sensor data for every KUKA robot:
   * Velocity of the motor controlling axis X
   * Torque on the motor controlling axis X
   * Current to the motor controlling axis X
   * Temperature of the motor controlling axis X
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
- [ ] This needs improvement
- [ ] RSI configuration
- [ ] Object setup in VC 4.0
- [ ] RSI joint rotations
- [ ] Further work in project report
- [ ] Improving control server for no lag

---

## About Project
This project was initited by the Norwegian Univeristy of Science and Technology (NTNU), department of Production Technology. The aim of the project was the following:

In order to respond quickly to unexpected events and new demands without extensive system changes, future production systems must be able to work more independently. There is a need for intelligent machines that perform complex tasks without detailed programming and without human interaction. Autonomous systems know their own abilities (which are modeled as "skills") and their state. They are able to choose between a set of possible actions, orchestrating and perform their skills. To succeed, the autonomous systems need to have realistic models of the current state of the production process and the system's own behaviour in interaction with its external environment - usually called a digital twin. 

OPC Unified Architecture (OPC UA) is a platform-independent protocol for machine-to-machine communication for industrial automation developed by the OPC Foundation. OPC focuses on accessing large amounts of real-time data at the same time as system performance is affected to a minimum. OPC UA has the potential to become an important foundation in the future industrial environment where machines deliver "production as a service", and all machines and sensors in production are online (Internet of Things).

In this task, implementation of a digital twin will be studied. A solution must be developed that provides seamless communication between robots, PLSs, and other relevant control systems that can be part of an industrial production system, linked to one digital representation of the system. The system must be tested at the institute Robot Laboratory.

**a)** Describe how a digital twin can be implemented using OPC UA.

**b)** Examine the advantages and disadvantages of using Visual Components 4.0 or similar simulation software <br />
   for the digital twin.

**c)** Use Visual Components 4.0 or similar simulation software to model and simulate the robot cell at the institute.

**d)** Examine the advantages and disadvantages of using KUKAVARPROXY and KUKA RSI Ethernet as middleware <br />
   between the KUKA KR C4 robot controllers and the digital twin.

**e)** Present a solution for a digital twin of the Institute's Robot Laboratory with use of OPC UA for communication.

**f)** Try out the system in a fixed case. Evaluate the results.

## Abstract
This project explores the term Industry 4.0 (I 4.0) and the use of Digital Twins (DTws) as an asset in this modern industrial revolution. A DTw can be described as a digital replica of a physical asset including data about this assets interaction with its environment. The goal of this project has been to develop a DTw for a robot cell at MTP Valgrinda, NTNU, and investigate what benefits could be gained from introducing this technology in this system and in the domain of automated robotic systems in general. 

The DTw was developed using the OPC UA communication architecture. OPC UA is called the pioneer of I 4.0 as it is a communication architecture aiming at the standardization of communication in industry. Three different visualization software solutions were compared. It was concluded that Visual Components 4.0 (VC 4.0) was the strongest candidate for developing a visual representation of the robot cell. Using VC 4.0 and OPC UA a DTw of the robot cell was created. 

Most of the work done in this project revolved around creating communication modules able to connect the physical robot cell to the virtual representation in VC 4.0, through the use of OPC UA. The result of this work is a communication library containing the virtual representation of the robot cell and the different communication modules able to give the DTw various functionalities. This library is made open-source and can be found in the project's GitHub repository at 

https://github.com/akselov/digital-twin-opcua

The software included in this library enables the DTw to do real-time mirroring of the physical robot's movements, plotting of robotic sensor data and controlling the robots from the DTw. A graphical user interface was developed to organize the functionalities. Figure 2 illustrates the current communication architecture for the DTw.

The DTw was tested in a fixed case. This was a multi-robot case including mirroring of movements, plotting of sensor data and control from VC 4.0. It was made a video from the case which is published online at

https://youtu.be/xlQhQPmJwlA

It was concluded that OPC UA was a good solution for use in this DTw as it is possible to implement on any platform, and is enabling a more flexible and structured way of communicating than traditional communication software used in client/server based systems. The benefits found with the use of the DTw in this automated robotic system included visibility to operations and a better foundation for statistical analysis to predict future states and for optimizing characteristic parameters associated with the robot cell. Finally, it was concluded that the DTw act as a good foundation for managing a complex system, something that could be beneficial as this specific system is used in training and professional development at the institute.

![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/Physical_%26_digital_model.png)
<br />**Figure 1**

## Communication

![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/InformationFlow.png)
**Figure 2**

## Python GUI
Simple GUI for launching Visual Components 4.0 OPC UA connection, displaying robot sensor data and writing data to .csv files.

![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/gui_full.png)

## Visual Components 4.0 connection
Example of a combined KUKAVARPROXY (KVP) and RSI connection in Visual Components 4.0.

![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/Connected_Variables_VC4.0.png)

## Communication modules

### KUKAVARPROXY
Configuration, pic?

### RSI
Confirguration with use of kinematicks file

## Monitoring robot from VC 4.0
Through KVP and RSI

## Controlling robot from VC 4.0
Through KVP
Could also be done thorugh RSI

## Acknowledgments
- OpenShowVar
- Python-KVP
- FreeOpcUa
