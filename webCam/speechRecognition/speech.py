import speech_recognition as sr 
# from pyfcm import FCMNotification

<<<<<<< HEAD
# APIKEY = "AAAAPe8PBM4:APA91bHh9s_Dur9z4LOveWHhZwSo3I7BWAfqRrbew5reQn_IbipupXiiy-Nx1Jmdc72CODxXNzlNZchkY2S8uEpSg-CwlmxX7fIcGZ9yAOuCtdsaeqPaSztjC0DwXI4qzk8LLtKsw0fX"
# TOKEN = "cQ1aDc0STRC5rKlWp66F3T:APA91bEVPoyT0G1OXevLAmgxS38wFZoI8LISoHq3yK42rXIMVJjOssjHKqxWBwn7lHx6nLTxJLXr-As7f7lUIo4UlGU3xI7MhzLrNua8XuhBYT59uOqDSO8t6IGWR9Phl_-qJz2tVb6M"
# push_service = FCMNotification(APIKEY)

# def sendMessage(message: str):
#     data_message = {
#         "body" : message
#     }

#     result = push_service.single_device_data_message(registration_id = TOKEN, data_message = data_message)

#     print(result)
=======
#APIKEY = "AAAAPe8PBM4:APA91bHh9s_Dur9z4LOveWHhZwSo3I7BWAfqRrbew5reQn_IbipupXiiy-Nx1Jmdc72CODxXNzlNZchkY2S8uEpSg-CwlmxX7fIcGZ9yAOuCtdsaeqPaSztjC0DwXI4qzk8LLtKsw0fX"
#TOKEN = "cQ1aDc0STRC5rKlWp66F3T:APA91bEVPoyT0G1OXevLAmgxS38wFZoI8LISoHq3yK42rXIMVJjOssjHKqxWBwn7lHx6nLTxJLXr-As7f7lUIo4UlGU3xI7MhzLrNua8XuhBYT59uOqDSO8t6IGWR9Phl_-qJz2tVb6M"
#push_service = FCMNotification(APIKEY)

#def sendMessage(message: str):
#    data_message = {
#        "body" : message
#    }
#
#    result = push_service.single_device_data_message(registration_id = TOKEN, data_message = data_message)
#
#    print(result)
>>>>>>> 4ee97e9c2227a8998c9509e2c03f5e2f34562b84

r = sr.Recognizer() #Recognizer 사용
mic = sr.Microphone() #Mic 선언

print("hello")

while True:
    with mic as source:
        audio = r.record(source, duration = 4)
        try:
            words = r.recognize_google(audio, language = 'ko-KR') #입력받은 음성을 전환 
            print(words)

            if words.find("살려 줘") != -1:
                sendMessage("살려 줘")
                print("살려줘 감지")

            if words.find("살려줘") != -1:
                print("살려줘 감지")
        
            if words.find("나 죽네") != -1:
                print("나 죽네 감지")

            if words.find("나 죽어") != -1:
                print("나 죽어 감지")

        except sr.WaitTimeoutError:
            print("Google Speech Recognition TimeOut")
            
        except sr.UnknownValueError:
            print("Google Speech Recognition could not understand audio")

        except sr.RequestError:
            print("Google Speech Recognition could not request")

        
