from database.database_connector import DatabaseConnection
import logging

class VideoDAO(object):
    def __init__(self,):
        self.connector = DatabaseConnection()
        self.logger = logging.getLogger()

    def insertValue(self, id, name, url):
        sql = "INSERT INTO video (id, name, url) VALUES(?, ?, ?)"
        try:
            self.conn = self.connector.getConnection()
            cursor = self.conn.cursor()
            cursor.execute(sql, (id, name, url,))
        except Exception as e:
            self.logger.error(e)
        finally:
            self.conn.close()
