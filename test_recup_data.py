#!/usr/bin/env python
# -*- coding: utf-8 -*-

import logging
import sys, os
import resource

# recuperer le hostname du pi
# os.uname()[1]
# changer hostname
# os.system('hostname <id>')


logging.basicConfig(level=logging.INFO)
logger = logging.getLogger('openzwave')

import openzwave
from openzwave.node import ZWaveNode
from openzwave.value import ZWaveValue
from openzwave.scene import ZWaveScene
from openzwave.controller import ZWaveController
from openzwave.network import ZWaveNetwork
from openzwave.option import ZWaveOption
import time

device="/dev/ttyACM0"
log="None"
sniff=15.0

#Define some manager options
options = ZWaveOption(device, \
  config_path="/home/pi/Desktop/python-openzwave-master/openzwave/config", \
  user_path=".", cmd_line="")
options.set_log_file("OZW_Log.log")
options.set_append_log_file(False)
options.set_console_output(True)
options.set_save_log_level(log)
#options.set_save_log_level('Info')
options.set_logging(False)
options.lock()

#Create a network object
network = ZWaveNetwork(options, log=None)


time_started = 0
print("------------------------------------------------------------")
print("Waiting for network awaked : ")
print("------------------------------------------------------------")
for i in range(0,300):
    if network.state>=network.STATE_AWAKED:

        print(" done")
        print("Memory use : {} Mo".format( (resource.getrusage(resource.RUSAGE_SELF).ru_maxrss / 1024.0)))
        break
    else:
        sys.stdout.write(".")
        sys.stdout.flush()
        time_started += 1
        time.sleep(1.0)
if network.state<network.STATE_AWAKED:
    print(".")
    print("Network is not awake but continue anyway")

while not network.is_ready:
    print("|")


print("Network home id : {}".format(network.home_id_str))
print("Controller node id : {}".format(network.controller.node.node_id))
print("Controller node version : {}".format(network.controller.node.version))
print("Nodes in network : {}".format(network.nodes_count))
print("\n")

for node in network.nodes:
    if network.nodes[node].refresh_info():
        print(type(network.nodes[node]))
        print("Id : {}".format(network.nodes[node].node_id))
        print("Node : {}".format(network.nodes[node]))
        print("Product Id : {}".format(network.nodes[node].product_id))
        print("Type device : {}".format(network.nodes[node].device_type))
        print("Type product : {}".format(network.nodes[node].product_type))
        print("Name : {}".format(network.nodes[node].name))
        print("Location : {}".format(network.nodes[node].location))
        print("Neighbors : {}".format(network.nodes[node].neighbors))
        print("------------------------------------------------------")
        print("Capabilities : {}".format(network.nodes[node].capabilities))
        print("command class : {}".format(network.nodes[node].command_classes))
        print("get values for cc : {}".format(network.nodes[node].get_values_for_command_class(49)))
        for val in network.nodes[node].get_values_for_command_class(49):
            if network.nodes[node].values[val].label == "Luminance" or network.nodes[node].values[val].label == "Temperature":
                print("{} : {}".format(network.nodes[node].values[val].label, network.nodes[node].values[val].data_as_string))
        print("\n")

network.stop()
