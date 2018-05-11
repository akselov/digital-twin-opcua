'''
Source code for an OPC UA server able to extract axis variables 
from a KUKA KR C4 robot controller and broadcast them to an
OPC UA client.

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
            if val != (""): 
                return self.__get_var(self.send(var,val,msgID))
            else: 
                raise self.error_list(3)
        except :
            self.error_list(2)

    # Close socket
    def disconnect (self):
            client.close()

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

    # Setup OPC-UA Server
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
    
    try:
        while(True):
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

    finally:
        # Close OPC UA client connection, remove subcsriptions, etc
        server.stop()