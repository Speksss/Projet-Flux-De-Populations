#!/usr/bin/env python
# -*- coding: utf-8 -*-

import logging
import sys, os
import time
import json
import resource
import requests
from copy import deepcopy

#logging.basicConfig(level=logging.INFO)
#logger = logging.getLogger('openzwave')

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
options.set_save_configuration(True)
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

"""
while True:
    node = 2
    val = 72057594081706180
    print("node/name/index/instance : {}/{}/{}/{}".format(node,network.nodes[node].name,network.nodes[node].values[val].index,network.nodes[node].values[val].instance))
    print("  label/help : {}/{}".format(network.nodes[node].values[val].label,network.nodes[node].values[val].help))
    print("  value: {} {}".format(network.nodes[node].get_sensor_value(val), network.nodes[node].values[val].units))
 #"""
     
#Changer config value
"""
while not network.nodes[2].is_awake:
    pass
network.nodes[2].set_config(72057594081707014,10) #temp interval
network.nodes[2].set_config(72057594081706662,10) #lux
network.nodes[2].set_config(72057594081706017,10) #blind time
network.nodes[2].set_config(72057594081706342,5) #cancellation time
network.nodes[2].set_config(72057594081707268,'Long blibnk then 2 short blinks, LED colour depends on the temperature. Set by parameters 86 and 87.') #LED
network.nodes[2].set_field('location', '3.513645a50.321904')
network.nodes[2].set_field('name', 'AB2_106')
while network.nodes[2].is_awake:
    pass
while not network.nodes[2].is_awake:
    pass
#"""

# Capteur de test
# Lat: 50.321904
# Lon:  3.513645
# Location d'un capteur: "<lon>a<lat>"
# Le "a" est un séparateur. Pourquoi "a"? Pourquoi pas. 

 
#while not network.is_ready:
#    pass
prev_res = {}
id_cc = [32,49,48]
#id_cc = [0, 32, 34, 128, 132, 133, 134, 142, 48, 49, 112, 113, 114, 115, 86, 90, 156, 94]
timer = time.time()
#print(timer)
continuer = True
while continuer:
    try:
        res = {}
        for node in network.nodes:
            if node != network.controller.node.node_id:
                res[network.nodes[node].name] = {}
                # res[network.nodes[node].name]["location"] = network.nodes[node].location
                print(network.nodes[node].location)
                lon, lat = network.nodes[node].location.split("a")
                res[network.nodes[node].name]["latitude"] = lat
                res[network.nodes[node].name]["longitude"] = lon
                
                for cc in id_cc:
                    #print(str(cc) + " = "+str(network.nodes[node].get_command_class_as_string(cc)))
                    for val in network.nodes[node].get_values_for_command_class(cc):
                        res[network.nodes[node].name][network.nodes[node].values[val].label] = network.nodes[node].values[val].data #_as_string
        # Arrondissement de la température
        for x in res:
            res[x]["Temperature"] = round(res[x]["Temperature"], 1)
        print(str(json.dumps(res)))
        if prev_res == {}:
            for x in res:
                for y in res[x]:
                    res[x][y] = str(res[x][y])
            prev_res = deepcopy(res)
        else:
            for x in res:
                for y in res[x]:
                    res[x][y] = str(res[x][y])
                    if res[x][y] != prev_res[x][y]: 
                        print("[!] {}: {} -> {}".format(y, prev_res[x][y], res[x][y]))
            prev_res = deepcopy(res)
        print("---")
        # Envoi toutes les 60s
        #if time() - timer > 60:
        payload = {'id' : str(network.home_id_str), 'datas' : str(res)}
        # print(str(network.home_id_str))
        try:
            rpost = requests.post("http://35.206.157.216:8080/capteur", data=payload)
        except:
            e = sys.exc_info()[0]
            print("/!\ Erreur d'envoi a la BDD:")
            print(e)
        finally:
            print(rpost)
        timer = time.time()
        time.sleep(15)
    except KeyboardInterrupt:
        print("Merci d'avoir utilisé les solutions Umbrella corp.")
        network.stop()
        continuer = False

"""
payload = {'id' : str(network.home_id_str), 'datas' : res}
print(str(network.home_id_str))
rpost = requests.post("http://35.206.157.216:8080/capteur", data=payload)
print(rpost)
print(rpost.text)

rget = requests.get("http://35.206.157.216:8080/capteurs")
print(rget)
print(rget.text)
#"""

# network.stop()

