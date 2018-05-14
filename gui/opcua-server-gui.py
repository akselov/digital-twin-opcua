'''
Source code for a GUI able to execute different OPC UA servers
with a variety of functionalities for a KUKA robot:
    - Send axis parameters to Visual Components 4.0
    - Display sensor data of motors controlling each axis 
      (real-time plotting):
        # Motor velocity
        # Motor torque
        # Motor current
        # Motor temperature
    - Write sensor data to .csv files

Author: Aksel Oevern
NTNU 2018
'''

import tkinter as tk
from tkinter import ttk
from tkinter import messagebox
from tkinter import *
LARGE_FONT= ("Verdana", 12)
import sys
import subprocess

# Setup KUKA Monitiong Application GUI
class KUKA_APP(tk.Tk):

    def __init__(self, *args, **kwargs):
        tk.Tk.__init__(self, *args, **kwargs)
        tk.Tk.iconbitmap(self, "robot-16.ico")
        tk.Tk.wm_title(self, "KUKA KR 16-2 Monitoring Application")
        tk.Tk.geometry(self,'400x260')
        
        container = tk.Frame(self)
        container.pack(side="top", fill="both", expand = True)
        container.grid_rowconfigure(0, weight=1)
        container.grid_columnconfigure(0, weight=1)

        canvas = Canvas(self, width=2000, height=2000)
        canvas.place(x = -100, y = 220)
        blackLine = canvas.create_line(0,2,2000,2)
        canvas.configure(background='gray')
        
        canvas_id = canvas.create_text(432,25)
        canvas.itemconfig(canvas_id, text = "akselov@stud.ntnu.no")
        canvas.insert(canvas_id, 12, "")

        self.frames = {}

        for F in (StartPage, PageOne, PageTwo, PageThree):
            frame = F(container, self)
            self.frames[F] = frame
            frame.grid(row=0, column=0, sticky="nsew")

        self.show_frame(StartPage)

    def show_frame(self, cont):
        frame = self.frames[cont]
        frame.tkraise()
        
class StartPage(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self,parent)
        label = tk.Label(self, text="KUKA KR 16-2 Monitoring Application", font=LARGE_FONT)
        label.place(x=44,y=10)

        button1 = ttk.Button(self, text="Send axis parameters to Visual Components 4.0",
                            command=lambda: controller.show_frame(PageOne))
        button1.place(x=65,y=50)

        button2 = ttk.Button(self, text="Display sensor data",
                            command=lambda: controller.show_frame(PageThree))
        button2.place(x=65,y=80)

        button3 = ttk.Button(self, text="Write data to file",
                            command=lambda: controller.show_frame(PageTwo))
        button3.place(x=65,y=110)

        close_button = ttk.Button(self, text="Close",command=self.quit)

        close_button.place(x=65,y=140)

class PageOne(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        label = tk.Label(self, text="Axis parameters to Visual Components 4.0", font=LARGE_FONT)
        label.place(x=20,y=10)

        button1 = ttk.Button(self, text="Back to Home",
                            command=lambda: controller.show_frame(StartPage))
        button1.place(x=65,y=50)

        button2 = ttk.Button(self, text="Start connection",
                            command= send_axis_parameters)
        button2.place(x=65,y=80)

class PageTwo(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        label = tk.Label(self, text="Write data to file", font=LARGE_FONT)
        label.place(x=120,y=10)

        button1 = ttk.Button(self, text="Back to home",
                            command=lambda: controller.show_frame(StartPage))
        button1.place(x=65,y=50)

        button2 = ttk.Button(self, text="Write Motor Speed to file",
                            command = write_motorSpeed_to_file)
        button2.place(x=65,y=80)

        button3 = ttk.Button(self, text="Write Motor Torque to file",
                            command = write_motorTorque_to_file)
        button3.place(x=65,y=110)

        button4 = ttk.Button(self, text="Write Motor Current to file",
                            command = write_motorCurrent_to_file)
        button4.place(x=65,y=140)

        button5 = ttk.Button(self, text="Write Motor Temperature to file",
                            command = write_motorTemp_to_file)
        button5.place(x=65,y=170)

class PageThree(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        label = tk.Label(self, text="Display sensor data", font=LARGE_FONT)
        label.place(x=110,y=10)

        button0 = ttk.Button(self, text="Back to Home",
                            command=lambda: controller.show_frame(StartPage))
        button0.place(x=65,y=50)

        button1 = ttk.Button(self, text="Display Motor Speed monitor",
                            command= motor_speed_monitor)
        button1.place(x=65,y=80)

        button2 = ttk.Button(self, text="Display Motor Torque monitor",
                            command= motor_torque_monitor)
        button2.place(x=65,y=110)

        button3 = ttk.Button(self, text="Display Motor Current monitor",
                            command= motor_current_monitor)
        button3.place(x=65,y=140)

        button4 = ttk.Button(self, text="Display Motor Temperature monitor",
                            command= motor_temp_monitor)
        button4.place(x=65,y=170)

def send_axis_parameters():
    messagebox.showinfo("Monitor axis parameters", "Axis parameters will now be available in Visual Components 4.0")
    subprocess.call([sys.executable, 'server_gui_VC4_0.py', 'argument1', 'argument2'])

def motor_speed_monitor():
    messagebox.showinfo("Display sensor data", "Motor Speed data will be displayed in a new window")
    subprocess.call([sys.executable, 'server_gui_motorSpeed.py', 'argument1', 'argument2'])

def motor_torque_monitor():
    messagebox.showinfo("Display sensor data", "Motor Torque data will be displayed in a new window")
    subprocess.call([sys.executable, 'server_gui_motorTorque.py', 'argument1', 'argument2'])

def motor_current_monitor():
    messagebox.showinfo("Display sensor data", "Motor Current data will be displayed in a new window")
    subprocess.call([sys.executable, 'server_gui_motorCurrent.py', 'argument1', 'argument2'])

def motor_temp_monitor():
    messagebox.showinfo("Display sensor data", "Motor Temperature data will be displayed in a new window")
    subprocess.call([sys.executable, 'server_gui_motorTemperature.py', 'argument1', 'argument2'])

def write_motorSpeed_to_file():
    messagebox.showinfo("Data to file", "Motor Speed data will be written to file KUKA_KR16_2_motorSpeed.csv")
    subprocess.call([sys.executable, 'server_gui_write_motorSpeed.py', 'argument1', 'argument2'])
    messagebox.showinfo("Data to file", "Motor Speed data was successfully written to file KUKA_KR16_2_motorSpeed.csv")

def write_motorTorque_to_file():
    messagebox.showinfo("Data to file", "Motor Torque data will be written to file KUKA_KR16_2_motorTorque.csv")
    subprocess.call([sys.executable, 'server_gui_write_motorTorque.py', 'argument1', 'argument2'])
    messagebox.showinfo("Data to file", "Motor Torque data was successfully written to file KUKA_KR16_2_motorTorque.csv")

def write_motorCurrent_to_file():
    messagebox.showinfo("Data to file", "Motor Current data will be written to file KUKA_KR16_2_motorCurrent.csv")
    subprocess.call([sys.executable, 'server_gui_write_motorCurrent.py', 'argument1', 'argument2'])
    messagebox.showinfo("Data to file", "Motor Current data was successfully written to file KUKA_KR16_2_motorCurrent.csv")

def write_motorTemp_to_file():
    messagebox.showinfo("Data to file", "Motor Temperature data will be written to file KUKA_KR16_2_motorTemp.csv")
    subprocess.call([sys.executable, 'server_gui_write_motorTemperature.py', 'argument1', 'argument2'])
    messagebox.showinfo("Data to file", "Motor Temperature data was successfully written to file KUKA_KR16_2_motorTemp.csv")

app = KUKA_APP()
app.mainloop()