from database.database_connection import DatabaseConnection

class UserDAO():
    def __init__(self,):
        self.database_connection = DatabaseConnection()

    def userSelect(self):
        self.conn = self.database_connection.getConnection()
        self.cursor = self.conn.cursor()

        self.cursor.execute("SELECT * FROM user WHERE userID = ?", (123,))

        for (userName, userAge) in self.cursor:
            print(f"userName: {userName}, userAge: {userAge}")
        
        self.conn.close()

    def getToken(self, id):
        self.conn = self.database_connection.getConnection()
        self.cursor = self.conn.cursor()

        self.cursor.execute("SELECT * FROM user WHERE userID = ?", (id,))

        # for (userID, userName, userAge, token) in self.cursor:
        #     userToken = token

        userToken = self.cursor.fetchone()[3]

        self.conn.close()

        return userToken