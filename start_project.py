from concurrent.futures import thread
import logging
import os
import subprocess
import threading

SCRIPT_PATH = os.getcwd()
STORE_AUTH_API = SCRIPT_PATH + "\store-auth-api"
STORE_API_GATEWAY = SCRIPT_PATH + "\store-api-gateway"
STORE_PRODUCT_API = SCRIPT_PATH + "\store-product-api"
STORE_MANAGEMENT_API = SCRIPT_PATH + "\store-management-api"

def start_component(path):
    
    logging.info("Starting spring application in next path: %s", path)
    subprocess.call("bash mvnw spring-boot:run", shell=True, cwd=path)

#1: start first auth API component
threading.Thread(target=start_component, args=(STORE_AUTH_API,)).start()

#2: start API gateway component
threading.Thread(target=start_component, args=(STORE_API_GATEWAY,)).start()

#3: start product API component
threading.Thread(target=start_component, args=(STORE_PRODUCT_API,)).start()

#4: start management API component
threading.Thread(target=start_component, args=(STORE_MANAGEMENT_API,)).start()