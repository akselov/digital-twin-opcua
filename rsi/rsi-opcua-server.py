'''
Source code for an OPC UA server able to extract axis variables 
from a KUKA KR C4 robot controller through RSI communication
and send them to an OPC UA client. 

Based on work by:
- Olivier Roulet-Dubonnet (https://github.com/FreeOpcUa)
- Eren Sezener (https://github.com/akselov/kuka-rsi3-communicator)
- Torstein Anderssen Myhre (https://github.com/torstem/examplecode-kukarsi-python)

Author: Aksel Oevern
NTNU 2018

To be done:
- Connection with rsi breaks down when joint corrections are to big
'''

import socket
import numpy
import command
import time
import time_keeper
from opcua import ua, Server

tiden = time_keeper.time_keeper()

def simple_joint_correction_command(from_kuka):
    	# Axis Correction example (not used)
	#A1 = 10.0 * numpy.sin(tiden.get() / 10.0)
    
	joint_desired = [0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
	#joint_desired = [A1, 0.0, 0.0, 0.0, 0.0, 0.0]
	return command.joint_correction_command(from_kuka, joint_desired)

if __name__ == "__main__":
	# Initialize OPC-UA Client connection
	client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 

	# Setup OPC-UA Server
	server = Server()
	server.set_endpoint("opc.tcp://192.168.250.119:50895")

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

	# Setup RSI connection
	BUFFER_SIZE = 1024
	sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sock.bind(("192.168.1.198", 49001))
	counter = 0

	try:
		while True:
			# Buffer size is 1024 bytes
			received_data, socket_of_krc = sock.recvfrom(BUFFER_SIZE)
			received_data = received_data.decode("utf-8") 
			output_elements = received_data.split(' ')
			raw_axis_variables = output_elements[14:20]
			axis_variables = []

			for raw_axis in raw_axis_variables:
				axis_variables.append(raw_axis.split('"')[1])
 
			A1.set_value(axis_variables[0])
			A2.set_value(axis_variables[1])
			A3.set_value(axis_variables[2])
			A4.set_value(axis_variables[3])
			A5.set_value(axis_variables[4])
			A6.set_value(axis_variables[5])

			kuka_command = simple_joint_correction_command(received_data)
			reply = bytes(kuka_command, 'utf-8')
			sock.sendto(reply, socket_of_krc)
    
	finally:
		# Close OPC UA client connection, remove subcsriptions, etc
		server.stop()
