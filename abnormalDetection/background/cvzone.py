import cv2
from cvzone.SelfiSegmentationModule import SelfiSegmentation
import os

cap = cv2.VideoCapture(0)
segmentor = SelfiSegmentation()

while True:
    ret, img = cap.read()
    imgOut = segmentor.removeBG(img, (255,255,255))

    cv2.imshow("Image", imgOut)
    k = cv2.waitKey(30) & 0xff
    if k == 27:
        break

cap.release()
cv2.destroyAllWindows()