#!/usr/bin/env python3
import socket
import codecs
import cv2 as cv
import threading

class VideoStream:
    #Initializes VideoStream object
    def __init__(self, host='', port=''):
        self.host = host
        self.port = port
        self.server_socket = None
        self.frame_capture = None
        self.streaming = False
        self.continue_stream = 'n'

    #starst up the camera and opens a socket
    def setup_stream(self):
        self.open_camera()
        self.open_socket()

    #starts up the camera, if unsuccessful, it'll throw an exception
    #Will address it later as the intended behaviour would be to try again
    #instead of simply quitting the program.
    def open_camera(self, usb_port=0):
        self.frame_capture = cv.VideoCapture(usb_port)
        if not self.frame_capture.isOpened():
            return -1
        
    #Closes the camera if opened
    def close_camera(self):
        if self.frame_capture:
            self.frame_capture.release()

    #Opens socket
    def open_socket(self):
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    #Starts streaming over a specified host and port
    def start_stream(self, host, port):
        if host:
            self.host = host
        if port:
            self.port = port
        self.continue_stream = 'y'
        while True:
            #A separte thread would update self.continue_stream if we choose to stop streaming
            while(self.frame_capture.isOpened() and self.continue_stream == 'y'):
                self.streaming = True
            
                #captures images and gets a return value
                return_value, image_frame = self.frame_capture.read()
                if not return_value:
                    self.streaming = False
                    break
            
                encoded_frame, buffered_image = cv.imencode('.jpg', image_frame, [cv.IMWRITE_JPEG_QUALITY, 35])
                
                #Sends packet to specified IP address 
                self.server_socket.sendto(buffered_image.tobytes(), (self.host, self.port))
            
    #Stops the stream. Currently have it listening for keyboard input
    #However, this will be a passed value from the GUI

    def stop_stream(self):
        while True:
            if input() == 'q':
                break
        
        if self.is_streaming():
            self.continue_stream = 'n'
            self.streaming = False
            self.close_camera()
    
    #Returns whether or not the video is being streamed
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




