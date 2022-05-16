import cv2
import mediapipe as mp
import numpy as np
from threading import Thread
from collections import Counter
from tensorflow.python.keras.models import load_model
from fcm_push import FcmNotification
from video_stream import VideoStream

actions = ['error', 'suffer', 'fall', 'sit', 'sit', 'walk', 'stand', 'lie', 'jump']
seq_length = 30

model = load_model('models/modelV5.7_WINDOW=30.h5')

# MediaPipe hands model
mp_pose = mp.solutions.pose
mp_drawing = mp.solutions.drawing_utils
pose = mp_pose.Pose(
    min_detection_confidence=0.6,
    min_tracking_confidence=0.6)

# if os.path.isfile(filePath):
#     cap = cv2.VideoCapture(filePath)
# else:
#     print("file is not exist")
# cap = cv2.VideoCapture("http://211.117.125.107:12485/") #webcam capture
stream_link = "http://211.117.125.107:12485/"

class VideoStreamWidget(object):
    def __init__(self, src=0):
        # Create a VideoCapture object
        self.capture = cv2.VideoCapture(src)
        self.capture.set(cv2.CAP_PROP_BUFFERSIZE, 2)

        # Start the thread to read frames from the video stream
        self.thread = Thread(target=self.update, args=())
        self.thread.daemon = True
        self.thread.start()

    def update(self):
        # Read the next frame from the stream in a different thread
        while True:
            if self.capture.isOpened():
                (self.status, self.frame) = self.capture.read()
                return self.status, self.frame

    def show_frame(self):
        # Display frames in main program
        if self.status:
            self.frame = self.maintain_aspect_ratio_resize(self.frame, width=None)
            cv2.imshow('IP Camera Video Streaming', self.frame)

    # Resizes a image and maintains aspect ratio
    def maintain_aspect_ratio_resize(self, image, width=None, height=None, inter=cv2.INTER_AREA):
        # Grab the image size and initialize dimensions
        dim = None
        (h, w) = image.shape[:2]

        # Return original image if no need to resize
        if width is None and height is None:
            return image

        # We are resizing height if width is none
        if width is None:
            # Calculate the ratio of the height and construct the dimensions
            r = height / float(h)
            dim = (int(w * r), height)
        # We are resizing width if height is none
        else:
            # Calculate the ratio of the 0idth and construct the dimensions
            r = width / float(w)
            dim = (width, int(h * r))

        # Return the resized image
        return cv2.resize(image, dim, interpolation=inter)

video_stream_widget = VideoStreamWidget(stream_link)

# frame size convert to int_type
# frameWidth = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
# frameHeight = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
# frameRate = int(cap.get(cv2.CAP_PROP_FPS))
# delay = round(1000/frameRate)
# fourcc = cv2.VideoWriter_fourcc(*'mp4v')
# out = cv2.VideoWriter('output.mp4', fourcc, frameRate, (frameWidth, frameHeight)) # initialize writer to save video

seq = []
action_queue = []
pre_action = ""
fcmNotification = FcmNotification()
fcmNotification.updateToken()

while video_stream_widget.capture.isOpened():
    ret, img = video_stream_widget.update()
    # img = video_stream_widget.maintain_aspect_ratio_resize(img)
    if not(ret):
        break

    # img = cv2.flip(img, 1)
    # img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    result = pose.process(img)
    # img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)

    if result.pose_landmarks is not None:
        # for res in result.multi_hand_landmarks:
        res = result
        joint = np.zeros((33, 4))
        for j, lm in enumerate(res.pose_landmarks.landmark):
            joint[j] = [lm.x, lm.y, lm.z, lm.visibility]

        # Compute angles between joints
        v1 = joint[[0,0,1,2,0,4,5,3,6,0,0,0,0,11,12,13,14,15,16,15,16,15,16,11,12,23,24,25,26,27,28,27,28,11,12,23,24], :3] # Parent joint
        v2 = joint[[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,12,11,24,23], :3] # Child joint
        v = v2 - v1 # [33, 4] but index[0] is null
        # Normalize v : 단위 벡터로 변환
        v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

        # Get angle using arcos of dot product : 단위 벡터에 대한 arccos 연산을 통해 사잇각을 얻을 수 있다.
        angle = np.arccos(np.einsum('nt,nt->n',
            v[[11,11,12,13,14,15,16,15,16,15,16,11,12,23,24,25,26,27,28,27,28,33,34,33,34,35,36,35,36],:], 
            v[[12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,23,24,13,14,23,24,25,26],:])) # [29,]

        angle = np.degrees(angle) # Convert radian to degree

        d = np.concatenate([joint.flatten(), angle])
        # d = joint.flatten()

        seq.append(d)

        mp_drawing.draw_landmarks(img, res.pose_landmarks, mp_pose.POSE_CONNECTIONS)

        if len(seq) < seq_length:
            continue

        input_data = np.expand_dims(np.array(seq[-seq_length:], dtype=np.float32), axis=0)

        y_pred = model.predict(input_data).squeeze()

        i_pred = int(np.argmax(y_pred))

        action = actions[i_pred]
        action_queue.append(action)

        if len(action_queue) < 16:
            continue
        
        action_queue.pop(0)

        action_counter = Counter(action_queue)
        most_action_tuple = action_counter.most_common().pop(0)
        this_action = most_action_tuple[0] # one action has the most value.
        if most_action_tuple[1] < 10:
            this_action = "unknown"

        if this_action == "fall":
            cv2.putText(img, f'{this_action.upper()}', org=(int(res.pose_landmarks.landmark[0].x * img.shape[1]), int(res.pose_landmarks.landmark[0].y * img.shape[0] + 20)), fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(0, 0, 255), thickness=2)
            if pre_action != "fall":
                fcmNotification.sendMessage(this_action)
        elif this_action == "unknown":
            cv2.putText(img, f'{this_action.upper()}', org=(int(res.pose_landmarks.landmark[0].x * img.shape[1]), int(res.pose_landmarks.landmark[0].y * img.shape[0] + 20)), fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(0, 255, 0), thickness=2)
        elif this_action is not None:
            cv2.putText(img, f'{this_action.upper()}', org=(int(result.pose_landmarks.landmark[0].x * img.shape[1]), int(result.pose_landmarks.landmark[0].y * img.shape[0] + 20)), fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(0, 255, 0), thickness=2)
        else:
            continue

        pre_action = this_action

    # out.write(img) #save video
    cv2.namedWindow('Abnormal Detection', flags=cv2.WINDOW_NORMAL)
    cv2.imshow('Abnormal Detection', img)

    # Press Q on keyboard to stop recording
    key = cv2.waitKey(1)
    if key == ord('q'):
        video_stream_widget.capture.release()
        cv2.destroyAllWindows()
        exit(1)

if video_stream_widget.capture.isClosed():
    video_stream_widget.capture.release()

cv2.destroyAllWindows()
exit(1)