'''
Source code for an OPC UA server able to create a custom OPC UA
object from specifications in a XML-file, estract axis variables 
from an OPC UA client and control a KUKA robot by its axis values.

Based on work by:
- Massimiliano Fago (https://sourceforge.net/projects/openshowvar)
- Mechatronics Lab at AAlesund University College 
  (https://github.com/aauc-mechlab/JOpenShowVar)
- Ahmad Saeed (https://github.com/akselov/kukavarproxy-msg-format)
- Olivier Roulet-Dubonnet (https://github.com/FreeOpcUa)

Author: Aksel Oevern
NTNU 2018

To be done:
- Custom setup of axis variables from OPC UA object
'''

import sys
import socket 
sys.path.insert(0, "..")
import time
from xml.dom import minidom
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

    # Import customobject type
    server.import_xml('KUKA_KR16_2_object.xml')

    # Check if client wants communication over KUKAVARPROXY or RSI
    kuka_object = objects.get_child("0:KUKA_KR16_2")
    variable_kukavarproxy = kuka_object.get_child("0:KUKAVARPROXY")
    variable_rsi = kuka_object.get_child("0:RSI")

    # Manual setup of axis variables for OPC UA object
    A1_var = kuka_object.get_child("0:A1")
    A2_var = kuka_object.get_child("0:A2")
    A3_var = kuka_object.get_child("0:A3")
    A4_var = kuka_object.get_child("0:A4")
    A5_var = kuka_object.get_child("0:A5")
    A6_var = kuka_object.get_child("0:A6")
    
    # Make KVP and RSI configuration writable by OPC UA client
    variable_kukavarproxy.set_writable()
    variable_rsi.set_writable()

    # Extracting axis variables and removing KUKAVARPROXU/RSI configuration variables
    variables_all = kuka_object.get_children()
    variables_all.remove(variable_kukavarproxy)
    variables_all.remove(variable_rsi)
    variables_axis = variables_all

    for variable_axis in variables_axis:
        variable_axis.set_writable()
    
    # Starting OPC-UA Server
    server.start()

    try:
        # Give user information about communication that is being used
        print("Kuka: ", variable_kukavarproxy.get_value())
        print("RSI: ", variable_rsi.get_value())
        print("Select KUKAVARPROXY or RSI control configuration in OPC UA Client")

        while True:
            time.sleep(0.1)

            # Check for client preference KUKAVARPROXY/RSI control
            if(variable_kukavarproxy.get_value() and variable_rsi.get_value()):
                raise ValueError("KUKAVARPROXY and RSI can not both be selected")
                SystemExit
            
            if(variable_kukavarproxy.get_value()):
                print("Launching KUKAVARPROXY control")
                robot.write("TARGET_AXIS","{E6AXIS: A1 " + str(A1_var.get_value()) + ", A2 " + str(A2_var.get_value()) + ", A3 " + str(A3_var.get_value()) + ", A4 " + str(A4_var.get_value()) + ", A5 " + str(A5_var.get_value()) + ", A6 " + str(A6_var.get_value()) + "}")

            if(variable_rsi.get_value()):
                print("Launching RSI control")
               #Run RSI control module

    finally:
        # Close OPC UA client connection, remove subcsriptions, etc
        server.stop()