#BackgroundSubtractor: http://www.gisdeveloper.co.kr/?p=6862
#Masking: https://house-of-e.tistory.com/entry/8-OpenCV-python-%EC%B0%A8%EC%98%81%EC%83%81-%EA%B8%B0%EB%B2%95-background-subtraction%EB%B0%B0%EA%B2%BD-%EC%B6%94%EC%B6%9C
#Dilation: https://webnautes.tistory.com/1257

file_path = "preprocessing/anormal/FD_In_H11H21H32_0023_20201231_10.mp4_20220405_144112.mkv"
################################################# MOG2 방식: Adaptive Mixture of Gaussian - 픽셀에 대해 적절한 수의 Gaussian distribution을 선택
import numpy as np
import cv2

cap = cv2.VideoCapture(file_path)

kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(15,15))
fgbg = cv2.createBackgroundSubtractorMOG2(history = 200, varThreshold = 100, detectShadows = False)

while(1):
    ret, frame = cap.read()

    fgmask = fgbg.apply(frame)
    fgmask = cv2.morphologyEx(fgmask, cv2.MORPH_CLOSE, kernel, iterations=5)
    fgmask = np.stack((fgmask,)*3, axis=-1)
    bitwise_image = cv2.bitwise_and(frame, fgmask)

    cv2.imshow('frame',bitwise_image)
    k = cv2.waitKey(30) & 0xff
    if k == 27:
        break

cap.release()
cv2.destroyAllWindows()

################################################# GMG 방식: Bayesian과 Kalmanfilter의 조합 
# import numpy as np
# import cv2
# cap = cv2.VideoCapture(0)

# kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(3,3))
# fgbg = cv2.bgsegm.createBackgroundSubtractorGMG(initializationFrames=20)

# while(1):
#     ret, frame = cap.read()

#     fgmask = fgbg.apply(frame)
#     fgmask = cv2.morphologyEx(fgmask, cv2.MORPH_OPEN, kernel)

#     cv2.imshow('frame',fgmask)
#     k = cv2.waitKey(30) & 0xff
#     if k == 27:
#         break

# cap.release()
# cv2.destroyAllWindows()

################################################# KNN 방식: K-Nearest Neighbor 최근접이웃 알고리즘 
# import numpy as np
# import cv2
# cap = cv2.VideoCapture(file_path)

# kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(5,5))
# fgbg = cv2.createBackgroundSubtractorKNN(detectShadows=False)

# while(1):
#     ret, frame = cap.read()

#     fgmask = fgbg.apply(frame)
#     fgmask = cv2.dilate(fgmask,kernel,iterations=1)
#     fgmask = np.stack((fgmask,)*3, axis=-1)

#     concat_image = np.concatenate((frame, fgmask), axis=1)

#     cv2.imshow('frame',fgmask)
#     k = cv2.waitKey(30) & 0xff
#     if k == 27:
#         break

# cap.release()
# cv2.destroyAllWindows()

################################################# Optical Flow
# import cv2
# import numpy as np
# cap = cv2.VideoCapture(0)
# ret, frame1 = cap.read()
# prvs = cv2.cvtColor(frame1,cv2.COLOR_BGR2GRAY)
# hsv = np.zeros_like(frame1)
# hsv[...,1] = 255
# while(1):
#     ret, frame2 = cap.read()
#     next = cv2.cvtColor(frame2,cv2.COLOR_BGR2GRAY)
#     flow = cv2.calcOpticalFlowFarneback(prvs,next, None, 0.5, 3, 15, 3, 5, 1.2, 0)
#     mag, ang = cv2.cartToPolar(flow[...,0], flow[...,1])
#     hsv[...,0] = ang*180/np.pi/2
#     hsv[...,2] = cv2.normalize(mag,None,0,255,cv2.NORM_MINMAX)
#     rgb = cv2.cvtColor(hsv,cv2.COLOR_HSV2BGR)
#     cv2.imshow('frame2',rgb)
#     k = cv2.waitKey(30) & 0xff
#     if k == 27:
#         break
#     elif k == ord('s'):
#         cv2.imwrite('opticalfb.png',frame2)
#         cv2.imwrite('opticalhsv.png',rgb)
#     prvs = next
# cap.release()
# cv2.destroyAllWindows()