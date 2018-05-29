'''
Source code for an OPC UA server able to create a custom OPC UA
object from specifications in a XML-file, estract axis variables 
from a KUKA KR C4 robot controller and send them to an OPC UA client.

Based on work by:
- Massimiliano Fago (https://sourceforge.net/projects/openshowvar)
- Mechatronics Lab at AAlesund University College 
  (https://github.com/aauc-mechlab/JOpenShowVar)
- Ahmad Saeed (https://github.com/akselov/kukavarproxy-msg-format)
- Olivier Roulet-Dubonnet (https://github.com/FreeOpcUa)

To be done:
- Include RSI control module

Author: Aksel Oevern
NTNU 2018
'''

import sys
sys.path.insert(0, "..")
import time
import KUKAVARPROXY_communication
from opcua import ua, Server

if __name__ == "__main__":
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
    ip_adress = kuka_object.get_child("0:IP")
    variable_kukavarproxy = kuka_object.get_child("0:KUKAVARPROXY")
    variable_rsi = kuka_object.get_child("0:RSI")

    # Extracting axis variables and removing KUKAVARPROXU/RSI configuration variables
    variables_all = kuka_object.get_children()

    for variable in variables_all:
        variable.set_writable()

    # Removing configuration properties
    variables_all.remove(ip_adress)
    variables_all.remove(variable_kukavarproxy)
    variables_all.remove(variable_rsi)
    variables_axis = variables_all
    
    # Starting OPC-UA Server
    server.start()

    try:
        print("Select KUKAVARPROXY or RSI communication configuration in OPC UA Client")
        while True:
            time.sleep(0.1)

            # Check for client preference KUKAVARPROXY/RSI communication
            if(variable_kukavarproxy.get_value() and variable_rsi.get_value()):
                raise ValueError("KUKAVARPROXY and RSI can not both be selected")
                SystemExit
            
            if(variable_kukavarproxy.get_value()):
                print("Launching KUKAVARPROXY communication")
                KUKAVARPROXY_communication.run(variables_axis, ip_adress)
                break

            if(variable_rsi.get_value()):
                print("Launching RSI communication")
                #Run RSI communication module

    finally:
        # Close OPC UA client connection, remove subcsriptions, etc
        server.stop()
