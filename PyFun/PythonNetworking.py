#!/usr/bin/env python3

import socket
import codecs
HOST = '127.0.0.1'
PORT = 12345
class PythonNetworking:
    def __init__(self, host=HOST, port=PORT):
        self.host = host
        self.port = port
        self.socket = None
        