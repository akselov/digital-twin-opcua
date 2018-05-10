# kukavarproxy Message Format

## 1. Description
kukavarproxy is an TCP/IP interface to Kuka robots that allows for reading and writing variables and data structures of the controlled manipulators. 

## 2. How it works
The kukavarproxy is a TCP/IP server that listens for network messages on the TCP port 7000, the reads and writes data to the KRC system variables.
kukavarproxy message format is 
* msg ID in HEX                       2 bytes
* msg length in HEX                   2 bytes
* read (0) or write (1)               1 byte
* variable name length in HEX         2 bytes
* variable name in ASCII              # bytes
* variable value length in HEX        2 bytes
* variable value in ASCII             # bytes
		
## 3. Usage
The KRC robot controller runs the Microsoft Windows operating system. The teach pendant shows an “HMI” which is a program that KUKA developed to run on Windows and it is the interface that the robot user has to manipulate the robot through.
In order to establish an Ethernet (TCP/IP) connection, you first need to run kukavarproxy on the controllers operating system, then configure the network connection from KUKA "HMI".
###   3.1. Copying kukavarproxy to the operating system on the KRC:
* Get the kukavarproxy from https://sourceforge.net/projects/openshowvar/files/openshowvar/REV%200.13.0/kukavarproxy-6_1.rar/download or from https://github.com/aauc-mechlab/JOpenShowVar/tree/master/KUKAVARPROXY%20rev%206.1.0.101
* Unpack and copy the folder to a USB flash drive
* Plug it to the KRC (not the teach pendant)
* Log in as an Expert or Administrator. For KR C4: `KUKA Menu -> Configuration -> User Group`. Defaulf password is `kuka`
* Minimize the "HMI". For KR C4:`KUKA Menu -> Startup -> Service -> Minimize HMI`
* Copy kukavarproxy folder to the Desktop (or anywhere else)
* Start the KUKAVARPROXY.exe
* If you have a problem with the file `cswsk32.ocx`, Use this command in the Administrator Command Prompt `regsvr32.exe c:\asdf\cswsk32.ocx` while changing the `asdf` with the true path to the file.
* You can make this program start automatically on reboot by creating a shortcut of KUKAVARPROXY.exe in `Windows Start -> All Programs -> Right click Startup -> Open`
###   3.2. HMI Network Configuration:
* Connect the robot to a network. (private one is recommended)
* Configure the KRC IP. For KR C4: `KUKA Menu -> Startup -> Network Configuration`
* Unlock port 7000. For KR C4: `KUKA Menu -> Startup -> Network Configuration -> Advanced`
* `NAT -> Add Port -> Port number 7000`
* Set permitted protocols: `tcp/udp`

## 4. Resources
* http://sourceforge.net/projects/openshowvar/
* https://github.com/aauc-mechlab/jopenshowvar/
* http://filipposanfilippo.inspitivity.com/publications/jopenshowvar-an-open-source-cross-platform-communication-interface-to-kuka-robots.pdf
* https://www.robodk.com/forum/attachment.php?aid=4