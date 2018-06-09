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
   * Velocity of motor controlling axis X
   * Torque on motor controlling axis X
   * Current to motor controlling axis X
   * Temperature of motor controlling axis X
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


![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/Physical_%26_digital_model.png)
Make smaller
## Communication

![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/InformationFlow.png)

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
