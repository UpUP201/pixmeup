from app.enums.error_code import ErrorCode

class CustomAppException(Exception):
    def __init__(self, error_code: ErrorCode):
        self.error_code = error_code