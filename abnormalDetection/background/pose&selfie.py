from typing import Any
import cv2
from cv2 import trace
import mediapipe as mp
import numpy as np
import os
mp_drawing = mp.solutions.drawing_utils
mp_drawing_styles = mp.solutions.drawing_styles
mp_pose = mp.solutions.pose

path = 'preprocessing/abnormal'
filePath = os.path.join(path, "FD_In_H11H21H32_0024_20201231_11.mp4_20220405_144135.mkv")

# For static images:
IMAGE_FILES = []
BG_COLOR = (192, 192, 192) # gray
with mp_pose.Pose(
    static_image_mode=True,
    model_complexity=2,
    enable_segmentation=True,
    min_detection_confidence=0.5) as pose:
  for idx, file in enumerate(IMAGE_FILES):
    image = cv2.imread(file)
    image_height, image_width, _ = image.shape
    # Convert the BGR image to RGB before processing.
    results = pose.process(cv2.cvtColor(image, cv2.COLOR_BGR2RGB))

    if not results.pose_landmarks:
      continue
    print(
        f'Nose coordinates: ('
        f'{results.pose_landmarks.landmark[mp_pose.PoseLandmark.NOSE].x * image_width}, '
        f'{results.pose_landmarks.landmark[mp_pose.PoseLandmark.NOSE].y * image_height})'
    )

    annotated_image = image.copy()
    # Draw segmentation on the image.
    # To improve segmentation around boundaries, consider applying a joint
    # bilateral filter to "results.segmentation_mask" with "image".
    condition = np.stack((results.segmentation_mask,) * 3, axis=-1) > 0.1
    bg_image = np.zeros(image.shape, dtype=np.uint8)
    bg_image[:] = BG_COLOR
    annotated_image = np.where(condition, annotated_image, bg_image)
    # Draw pose landmarks on the image.
    mp_drawing.draw_landmarks(
        annotated_image,
        results.pose_landmarks,
        mp_pose.POSE_CONNECTIONS,
        landmark_drawing_spec=mp_drawing_styles.get_default_pose_landmarks_style())
    cv2.imwrite('/tmp/annotated_image' + str(idx) + '.png', annotated_image)
    # Plot pose world landmarks.
    mp_drawing.plot_landmarks(
        results.pose_world_landmarks, mp_pose.POSE_CONNECTIONS)

# For webcam input:
cap = cv2.VideoCapture(filePath)

kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(10,10))
fgbg = cv2.createBackgroundSubtractorMOG2(history = 200, varThreshold = 100, detectShadows = False)
tracemask = None

with mp_pose.Pose(
    min_detection_confidence=0.3,
    min_tracking_confidence=0.3) as pose:
  while cap.isOpened():
    success, image = cap.read()
    if not success:
      print("Ignoring empty camera frame.")
      # If loading a video, use 'break' instead of 'continue'.
      break
    # using MOG2 module but mask is not updated automatically you should control that.
    # It's effective that not human things are detected instead real human.
    fgmask = fgbg.apply(image)
    fgmask = cv2.morphologyEx(fgmask, cv2.MORPH_OPEN, kernel)
    fgmask = np.stack((fgmask,)*3, axis=-1)
    if tracemask is None:
      tracemask = fgmask
    else:
      tracemask = cv2.bitwise_or(tracemask, fgmask)
    if tracemask.mean() > 100:
      tracemask = fgmask
    bitwise_image = cv2.bitwise_and(image, tracemask)

    bitwise_image.flags.writeable = False
    bitwise_image = cv2.cvtColor(bitwise_image, cv2.COLOR_BGR2RGB)
    results = pose.process(bitwise_image)

    # Draw the pose annotation on the image.
    bitwise_image.flags.writeable = True
    bitwise_image = cv2.cvtColor(bitwise_image, cv2.COLOR_RGB2BGR)
    mp_drawing.draw_landmarks(
        bitwise_image,
        results.pose_landmarks,
        mp_pose.POSE_CONNECTIONS,
        landmark_drawing_spec=mp_drawing_styles.get_default_pose_landmarks_style())
    # Flip the image horizontally for a selfie-view display.

    cv2.imshow('frame',bitwise_image)
    if cv2.waitKey(5) & 0xFF == 27:
      break
cap.release()