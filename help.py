#!/usr/bin/env python
# -*- coding: utf-8 -*-

import logging
import sys, os
import json
import resource
import requests
from copy import deepcopy

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
sniff=3.0

#Define some manager options
options = ZWaveOption(device, \
  config_path="/home/pi/open-zwave/config", \
  user_path="/home/pi/p2p", cmd_line="")
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


print(network.controller.request_network_update(2))
print(network.controller.send_queue_count)

#while not network.is_ready:
#    pass
prev_res = {}
while True:
    if network.is_ready:
        res = {}
        for node in network.nodes:
            if node != network.controller.node.node_id:
                res[network.nodes[node].name] = {}
                res[network.nodes[node].name]["location"] = network.nodes[node].location
                for val in network.nodes[node].get_values_for_command_class(49):
                    if network.nodes[node].values[val].label == "Luminance" or network.nodes[node].values[val].label == "Temperature":
                        res[network.nodes[node].name][network.nodes[node].values[val].label] = network.nodes[node].values[val].data_as_string
        # Arrondissement de la température
        for x in res:
            res[x]["Temperature"] = round(res[x]["Temperature"], 1)
        print(res)
        if prev_res == {}:
            prev_res = deepcopy(res)
        else:
            for x in res:
                for y in res[x]:
                    if type(res[x][y]) == int and (0.8 > res[x][y] / prev_res[x][y] or res[x][y] / prev_res[x][y] > 1.2):
                        print("[!] {}: {} -> {}".format(y, prev_res[x][y], res[x][y]))
            prev_res = deepcopy(res)
        print("---")
        time.sleep(6.0)


""" # Requete get et post api
payload = {'id' : str(network.home_id_str), 'datas' : str(res)}
print(str(network.home_id_str))
rpost = requests.post("http://35.206.157.216:8080/capteur", data=payload)
print(rpost)
print(rpost.text)

rget = requests.get("http://35.206.157.216:8080/capteurs")
print(rget)
print(rget.text)
"""


""" # Changer config value
while not network.nodes[2].is_awake:
    pass
#network.nodes[2].set_config(72057594081707014,3) #temp
network.nodes[2].set_config(72057594081706662,3) #lux
#network.nodes[2].set_config(72057594081706017,3) #blind time
while not network.nodes[2].is_awake:
    pass
for conf in network.nodes[2].get_configs().values() :
    print(str(conf.label) + " : " + str(conf.data))
"""

network.stop()

