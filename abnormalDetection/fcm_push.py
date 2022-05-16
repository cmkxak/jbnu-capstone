from pyfcm import FCMNotification

class FcmNotification:
    # 파이어베이스 콘솔에서 얻어 온 API키를 넣어 줌
    def __init__(self,):
        self.push_service = FCMNotification(api_key="AAAAPe8PBM4:APA91bHh9s_Dur9z4LOveWHhZwSo3I7BWAfqRrbew5reQn_IbipupXiiy-Nx1Jmdc72CODxXNzlNZchkY2S8uEpSg-CwlmxX7fIcGZ9yAOuCtdsaeqPaSztjC0DwXI4qzk8LLtKsw0fX")
        self.token = ""
        self.nth = 0
    
    def updateToken(self):
        tmpToken = "fPAvbm3cTSyKe1P2sfj-Yg:APA91bEvjEhHXkFoRaZMq2LSpO4484ntln4kHJkHwP2ncyVCij_MBlcGAoG_C2OiqWCcTFHoLy8Evr56I36pUwodMagA_WULeMWPqwwUnh8koaycglTbt-86CLjbvnOPVGCH9ZLt_Hmd"
        self.token = tmpToken

    def sendMessage(self, state):

        registration_id = self.token

        data_message = {
            "body" : state + " 알림입니다."
        }
        
        #data payload만 보내야 안드로이드 앱에서 백그라운드/포그라운드 두가지 상황에서 onMessageReceived()가 실행됨
        result = self.push_service.single_device_data_message(registration_id=registration_id, data_message=data_message)

        print(result)
