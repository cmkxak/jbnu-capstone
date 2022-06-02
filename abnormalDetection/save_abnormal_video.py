from threading import Thread
import cv2
import time
import os

class SaveAbnormalVideo():
    def __init__(self, videoStream):
        # frame size convert to int_type
        self.frameWidth = int(videoStream.capture.get(cv2.CAP_PROP_FRAME_WIDTH))
        self.frameHeight = int(videoStream.capture.get(cv2.CAP_PROP_FRAME_HEIGHT))
        self.frameRate = int(videoStream.capture.get(cv2.CAP_PROP_FPS))
        self.fourcc = cv2.VideoWriter_fourcc(*'avc1')
        
    def set_img_queue(self, img_queue):
        self.img_queue = img_queue

    def save_abnormal_video(self, user_id):
        self.thread = Thread(target=self.save_video, args=())
        os.makedirs('abnormalVideo/' + user_id, exist_ok=True)
        self.out = cv2.VideoWriter(os.path.join('abnormalVideo/' + user_id, f'{int(time.time())}.mp4'), self.fourcc, self.frameRate, (self.frameWidth, self.frameHeight))
        self.thread.start()

    def save_video(self):
        for img in self.img_queue:
            self.out.write(img)
