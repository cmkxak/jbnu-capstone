import cv2
from video_stream import VideoStream

stream_link = "https://ieilms.jbnu.ac.kr/data/10081/Mockito-CI.mp4"

videoStream = VideoStream(stream_link)
while videoStream.capture.isOpened:
    try:
        ret, img = videoStream.get_frame()
        cv2.imshow('Abnormal Detection', img)
    except AttributeError:
        pass

    # Press Q on keyboard to stop recording
    key = cv2.waitKey(1)
    if key == ord('q'):
        videoStream.capture.release()
        cv2.destroyAllWindows()
        exit(1)
