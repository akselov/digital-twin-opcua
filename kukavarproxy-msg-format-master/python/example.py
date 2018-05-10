from kukavarproxy import *
robot = KUKA('172.31.1.147')

robot.write("$OV_PRO",33)
print (robot.read("$OV_PRO"))