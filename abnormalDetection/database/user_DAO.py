from database.database_connection import DatabaseConnection

class UserDAO():
    def __init__(self,):
        self.database_connection = DatabaseConnection()

    def userSelect(self, ip):
        self.conn = self.database_connection.getConnection()
        self.cursor = self.conn.cursor()

        self.cursor.execute("SELECT * FROM subproducts WHERE ip = ?", (ip,))

        for (id, name, age, phone_number, ip) in self.cursor:
            print(f"id: {id}, name: {name}, age: {age}, phone_number: {phone_number}, ip: {ip}")
        
        self.conn.close()

    def getToken(self, userid):
        self.conn = self.database_connection.getConnection()
        self.cursor = self.conn.cursor()

        self.cursor.execute("SELECT * FROM products WHERE id = ?", (userid,))

        for (id, password, token) in self.cursor:
            userToken = token

        # userToken = self.cursor.fetchone()[3]

        self.conn.close()

        return userToken