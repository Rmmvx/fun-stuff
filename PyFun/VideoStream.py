#!/usr/bin/env python3
import socket
import codecs
import cv2 as cv
import threading

class VideoStream:
    def __init__(self, host='', port=''):
        self.host = host
        self.port = port
        self.server_socket = None
        self.frame_capture = None
        self.streaming = False
        self.continue_stream = 'n'

    def setup_stream(self):
        self.open_camera()
        self.open_socket()

    def open_camera(self, usb_port=0):
        self.frame_capture = cv.VideoCapture(usb_port)
        if not self.frame_capture.isOpened():
            return -1
        
    def close_camera(self):
        self.frame_capture.release()

    def open_socket(self):
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    def start_stream(self, host, port):
        if host:
            self.host = host
        if port:
            self.port = port
        self.continue_stream = 'y'
        while True:
            while(self.frame_capture.isOpened() and self.continue_stream == 'y'):
                self.streaming = True
            
                #captures images infinitely
                return_value, image_frame = self.frame_capture.read()
                if not return_value:
                    self.streaming = False
                    break
            
                encoded_frame, buffered_image = cv.imencode('.jpg', image_frame, [cv.IMWRITE_JPEG_QUALITY, 35])
                
                #Sends packet to specified IP address 
                self.server_socket.sendto(buffered_image.tobytes(), (socket.gethostname(), self.port))
            
    
    def stop_stream(self):
        while True:
            if input() == 'q':
                break
        
        if self.is_streaming():
            self.continue_stream = 'n'
            self.streaming = False
            self.close_camera()
    
    def is_streaming(self):
        return self.is_streaming
    

'''Testing multithreading component
This is the part that will be in the PythonNetworking class
to stream while actively listen to stop streaming

'''
streamer = VideoStream()
streamer.setup_stream()
thread1 = threading.Thread(target=streamer.start_stream, args=("10.0.0.245", 12345))

thread1.start()
streamer.stop_stream()




