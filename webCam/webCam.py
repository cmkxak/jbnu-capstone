from flask import Flask,render_template,Response
import cv2
from pop import Util

Util.enable_imshow()

cam = Util.gstrmer(width = 1280, height = 720)

app = Flask(__name__)
camera=cv2.VideoCapture(cam, cv2.CAP_GSTREAMER)

def generate_frames():
    while True:

        ## read the camera frame
        success,frame=camera.read()
        if not success:
            break
        else:
            ret,buffer=cv2.imencode('.jpg',frame)
            frame=buffer.tobytes()

        yield(b'--frame\r\n'
                   b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')

@app.route('/')
def index():
	return render_template('index.html')

@app.route('/video')
def video():
	return Response(generate_frames(),mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__=="__main__":
	app.run(host = "192.168.0.12", port=8080)
