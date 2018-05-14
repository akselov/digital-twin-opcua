'''
Source code for an OPC UA server able to extract axis variables 
from a KUKA KR C4 robot controller and send them to an
OPC UA client. The server also extracts information about the 
current velocity of each of the motors controlling the different 
KUKA robot axis and does a real-time plot of all the values.

Based on work by:
- Massimiliano Fago (https://sourceforge.net/projects/openshowvar)
- Mechatronics Lab at AAlesund University College 
  (https://github.com/aauc-mechlab/JOpenShowVar)
- Ahmad Saeed (https://github.com/akselov/kukavarproxy-msg-format)
- Olivier Roulet-Dubonnet (https://github.com/FreeOpcUa)

Author: Aksel Oevern
NTNU 2018
'''

import sys
import socket     
sys.path.insert(0, "..")
import time
import numpy as np
import matplotlib.pyplot as plt
from opcua import ua, Server

class KUKA(object):
    # Open socket
    # KukaVarProxy actively listens on TCP port 7000
    def __init__(self, TCP_IP):
        try: 
            client.connect((TCP_IP, 7000))
        except: 
            self.error_list(1)

    # Sending messages on the KukaVarProxy message format:        
    # Msg ID in HEX                       2 bytes
    # Msg length in HEX                   2 bytes
    # Read (0) or write (1)               1 byte
    # Variable name length in HEX         2 bytes
    # Variable name in ASCII              # bytes
    # Variable value length in HEX        2 bytes
    # Variable value in ASCII             # bytes
    def send (self, var, val, msgID):
        try:
            msg = bytearray()
            temp = bytearray()
            if val != "":
                val = str(val)
                msg.append((len(val) & 0xff00) >> 8)
                msg.append((len(val) & 0x00ff))
                msg.extend(map(ord, val))
            temp.append(bool(val))
            temp.append(((len(var)) & 0xff00) >> 8)
            temp.append((len(var)) & 0x00ff)
            temp.extend(map(ord, var))
            msg = temp + msg
            del temp[:]
            temp.append((msgID & 0xff00) >> 8)
            temp.append(msgID & 0x00ff)
            temp.append((len(msg) & 0xff00) >> 8)
            temp.append((len(msg) & 0x00ff))
            msg = temp + msg
        except :
            self.error_list(2)
        try:
            client.send(msg)
            return  client.recv(1024)
        except :
            self.error_list(1)

    # Get variables from KukaVarProxy response format
    def __get_var(self, msg):
        try:
            lsb = int( msg[5])
            msb = int( msg[6])
            lenValue = (lsb <<8 | msb)
            return str(msg [7: 7+lenValue],'utf-8')  
        except:
            self.error_list(2)

    # Read variables from KukaVarProxy
    def read (self, var, msgID=0):
        try:
            return self.__get_var(self.send(var,"",msgID))  
        except :
            self.error_list(2)

    # Write variables to KukaVarProxy
    def write (self, var, val, msgID=0):
        try:
            if val != (""): return self.__get_var(self.send(var,val,msgID))
            else: raise self.error_list(3)
        except :
            self.error_list(2)

    # Close socket
    def disconnect (self):
            client.close()                                      # CLose socket

    # In case of error
    def error_list (self, ID):
        if ID == 1:
            print ("Network Error (tcp_error)")
            print ("Check your KRC's IP address on the network, and make sure kukaproxyvar is running.")
            self.disconnect()
            raise SystemExit
        elif ID == 2:
            print ("Python Error.")
            print ("Check the code and uncomment the lines related to your python version.")
            self.disconnect()
            raise SystemExit
        elif ID == 3:
            print ("Error in write() statement.")
            print ("Variable value is not defined.")

if __name__ == "__main__":
    # Initialize OPC-UA Client connection
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 

    # Setup robot object
    robot = KUKA('192.168.250.16')

    # Setup server
    server = Server()
    server.set_endpoint("opc.tcp://192.168.250.200:50895")

    # Setup namespace
    uri = "http://examples.freeopcua.github.io"
    idx = server.register_namespace(uri)

    # Get objects node
    objects = server.get_objects_node()

    # Populating address space
    myObject = objects.add_object(idx, "KUKA_KR16_2")

    # Adding desired variables
    A1 = myObject.add_variable(idx, "A1", 6.7)
    A2 = myObject.add_variable(idx, "A2", 6.7)
    A3 = myObject.add_variable(idx, "A3", 6.7)
    A4 = myObject.add_variable(idx, "A4", 6.7)
    A5 = myObject.add_variable(idx, "A5", 6.7)
    A6 = myObject.add_variable(idx, "A6", 6.7)

    A1.set_writable()
    A2.set_writable()
    A3.set_writable()
    A4.set_writable()
    A5.set_writable()
    A6.set_writable()

    # Starting OPC-UA Server
    server.start()

    # Start plot
    xaxis_t0 = 100
    plt.axis([0, xaxis_t0, -100, 100])
    plt.title('Current motor speed of KUKA KR 16-2, Axis 1-6')
    plt.grid(True)
    plt.xlabel('time [s*10]')
    plt.ylabel('Current motor speed [%]')
    plt.ion()
    count = 0

    plotted_values_A1 = []
    plotted_values_A2 = []
    plotted_values_A3 = []
    plotted_values_A4 = []
    plotted_values_A5 = []
    plotted_values_A6 = []

    plt.plot(plotted_values_A1, 'b', label='Axis 1')
    plt.plot(plotted_values_A2, 'r', label='Axis 2')
    plt.plot(plotted_values_A3, 'g', label='Axis 3')
    plt.plot(plotted_values_A4, 'c', label='Axis 4')
    plt.plot(plotted_values_A5, 'm', label='Axis 5')
    plt.plot(plotted_values_A6, 'y', label='Axis 6')

    plt.legend()
    
    try:
        count = 0
        while True:
            time.sleep(0.1)
            
            output = robot.read("$AXIS_ACT")
            output = output.replace(",", "")
            output_elements = output.split(" ")

            A1.set_value(output_elements[2])
            A2.set_value(output_elements[4])
            A3.set_value(output_elements[6])
            A4.set_value(output_elements[8])
            A5.set_value(output_elements[10])
            A6.set_value(output_elements[12])
        
            plotted_values_A1.append(float(robot.read("$VEL_AXIS_ACT [1]")))
            plotted_values_A2.append(float(robot.read("$VEL_AXIS_ACT [2]")))
            plotted_values_A3.append(float(robot.read("$VEL_AXIS_ACT [3]")))
            plotted_values_A4.append(float(robot.read("$VEL_AXIS_ACT [4]")))
            plotted_values_A5.append(float(robot.read("$VEL_AXIS_ACT [5]")))
            plotted_values_A6.append(float(robot.read("$VEL_AXIS_ACT [6]")))

            plt.plot(plotted_values_A1, 'b', label='Axis 1')
            plt.plot(plotted_values_A2, 'r', label='Axis 2')
            plt.plot(plotted_values_A3, 'g', label='Axis 3')
            plt.plot(plotted_values_A4, 'c', label='Axis 4')
            plt.plot(plotted_values_A5, 'm', label='Axis 5')
            plt.plot(plotted_values_A6, 'y', label='Axis 6')

            plt.pause(0.00001)
            count+=1

            # Resizing plot
            if(count+20 > xaxis_t0):
                xaxis_t0+=50
                plt.axis([0, xaxis_t0, -100, 100])

    finally:
        # Close OPC UA client connection, remove subcsriptions, etc
        server.stop()
