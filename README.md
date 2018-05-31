# Digital Twin with OPC UA
This repository contains files used in the development of a digital twin for a robot cell at NTNU with the use of Visual Components 4.0 and OPC UA.

## Result
The system currently cointains the following functionality:
* Functioning..
* This works..
* List from project report

    \item Programming, planning and control in VC 4.0
    \item Mirror movements of physical robot cell in real-time using KVP, \newline average read time \textbf{8.75ms}
    \item Mirror movements of physical robot cell in real-time using RSI, \newline average read time \textbf{4.00ms}
    
    \item Plot sensor data for every KUKA robot:
    \begin{itemize}
        \item Velocity of motor controlling axis X
        \item Torque on motor controlling axis X
        \item Current to motor controlling axis X
        \item Temperature of motor controlling axis X
    \end{itemize}
    
    \item Write sensor data for every KUKA robot to .csv files:
    \begin{itemize}
        \item Velocity to file
        \item Torque to file
        \item Current to file
        \item Temperature to file
    \end{itemize}
    
    \item GUI for easier access to functionalities
    \item Extraction of physical robot properties through XML 
    \item Controlling physical robots from VC 4.0 using KVP

## Needs improvement
* This needs improvement
* RSI configuration
* Object setup in VC 4.0
* RSI joint rotations
* Further work in project report

## About Project
This project was initited by the Norwegian Univeristy of Science and Technology (NTNU), department of Production Technology. The aim of the project was the following:


![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/Physical_%26_digital_model.png)

## Python GUI
Simple GUI for launching Visual Components 4.0 OPC UA connection, displaying robot sensor data and writing data to .csv files.

![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/gui_full.png)

## Visual Components 4.0 connection
Example of a combined KUKAVARPROXY (KVP) and RSI connection in Visual Components 4.0.

![alt text](https://github.com/akselov/digital-twin-opcua/blob/master/pictures/Connected_Variables_VC4.0.png)

## Communication modules

### KUKAVARPROXY

### RSI
Confirguration with use of kinematicks file

## Monitoring robot from VC 4.0


## Controlling robot from VC 4.0
Through KVP

## Acknowledgments
- OpenShowVar
- Python-KVP
- FreeOpcUa
