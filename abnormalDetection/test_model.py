from asyncio import QueueEmpty
import cv2
import mediapipe as mp
import numpy as np
import os
from collections import Counter
from tensorflow.keras.models import load_model

actions = ['normal', 'abnormal', 'falling', 'lying', 'sitting', 'walking', 'standing', 'lain']
seq_length = 20

model = load_model('models/modelV4.0_GRU.h5')
path = 'C:/Users/sang9/OneDrive/바탕 화면/preprocessing/abnormal'
filePath = os.path.join(path, "FD_In_H11H21H31_0002_20201013_10.mp4_20220405_142615.mkv")
print(filePath)

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
cap = cv2.VideoCapture(0) #webcam capture

# frame size convert to int_type
# frameWidth = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
# frameHeight = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
# frameRate = int(cap.get(cv2.CAP_PROP_FPS))
# delay = round(1000/frameRate)
# fourcc = cv2.VideoWriter_fourcc(*'mp4v')
# out = cv2.VideoWriter('output.mp4', fourcc, frameRate, (frameWidth, frameHeight)) # initialize writer to save video

seq = []
action_queue = []

while cap.isOpened():
    ret, img = cap.read()
    if not(ret):
        break

    img = cv2.flip(img, 1)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    result = pose.process(img)
    img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)

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
        this_action = action_counter.most_common().pop(0)[0] # one action has the most value.

        cv2.putText(img, f'{this_action.upper()}', org=(int(res.pose_landmarks.landmark[0].x * img.shape[1]), int(res.pose_landmarks.landmark[0].y * img.shape[0] + 20)), fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(255, 255, 255), thickness=2)

    # out.write(img) #save video
    cv2.namedWindow('img', flags=cv2.WINDOW_NORMAL)
    cv2.imshow('img', img)
    if cv2.waitKey(1) == 27:
        break

if cap.isOpened():
    cap.release()

cv2.destroyAllWindows()