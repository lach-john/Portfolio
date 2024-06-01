import threading
import torch
import cv2
from deepface import DeepFace


cap = cv2.VideoCapture(0, cv2.CAP_DSHOW)

cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

counter = 0

match = False
refImage = cv2.imread("referenceImg.jpg") 


def checkFace(frame):
    global match
    try:
        if DeepFace.verify(frame, refImage.copy())['verified']:
            match = True
        else:
            match = False

    except ValueError:
        match = False

while True:
    ret, frame = cap.read()

    if ret:
        if counter % 30 == 0:
            try:
                threading.Thread(target = checkFace, args = (frame.copy(),)).start()
            except ValueError: 
                pass
        counter += 1

        if match:
            cv2.putText(frame, "Match!", (20, 450), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 255, 0), 3)
        else:
            cv2.putText(frame, "No Match!", (20, 450), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 0, 255), 3)

        cv2.imshow("video", frame)

    key = cv2.waitKey(1)
    if key == ord("q"):
        break

cv2.destroyAllWindows()



