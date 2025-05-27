from csv import Error
from app.enums.error_code import ErrorCode

class CustomAppException(Exception):
    def __init__(self, error_code: Error):
        self.error_code = error_code