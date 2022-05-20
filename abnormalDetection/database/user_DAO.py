from database_connection import DatabaseConnection

class UserDAO():
    def __init__(self,):
        self.database_connection = DatabaseConnection()

    def userSelect(self):
        self.conn = self.database_connection.getConnection()
        self.cursor = self.conn.cursor()

        print(self.conn)
        self.cursor.execute("SELECT userName, userAge FROM user WHERE userID = ?", (123,))

        for (userName, userAge) in self.cursor:
            print(f"userName: {userName}, userAge: {userAge}")
        
        self.conn.close()