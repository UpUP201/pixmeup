export interface SendVerificationResponse {
  status: number;
  message: string;
  data?: {
    success: boolean;
    expiresInSeconds: number;
  };
  timestamp: string;
}

export interface ErrorResponse {
  timestamp: string;
  name: string;
  status: number;
  message: string;
}

export type ApiError = {
  name:
    | 'PHONE_NUMBER_ALREADY_EXISTS'
    | 'INVALID_PHONE_NUMBER_FORMAT'
    | 'TOO_MANY_REQUESTS'
    | 'INTERNAL_SERVER_ERROR';
  message: string;
  status: number;
};

export interface VerificationCodeResponse {
  success: boolean;
  temporaryAuthToken: string;
}

export type VerificationCodeError = {
  name:
    | 'INVALID_INPUT_VALUE'
    | 'VERIFICATION_CODE_MISMATCH'
    | 'VERIFICATION_CODE_EXPIRED'
    | 'MAX_VERIFICATION_ATTEMPTS_EXCEEDED'
    | 'INTERNAL_SERVER_ERROR';
  message: string;
  status: number;
  timestamp: string;
};

export interface RegisterRequest {
  name: string;
  phoneNumber: string;
  password: string;
  passwordConfirm: string;
  agreements: {
    service: boolean;
    privacy: boolean;
    sensitiveInfo: boolean;
    thirdParty: boolean;
    consignment: boolean;
    marketing: boolean;
  };
}

export interface RegisterResponse {
  userId: number;
  name: string;
}

export type RegisterError = {
  name:
    | 'INVALID_INPUT_VALUE'
    | 'PASSWORD_CONFIRM_MISMATCH'
    | 'TERMS_NOT_AGREED'
    | 'PHONE_NUMBER_ALREADY_EXISTS'
    | 'INTERNAL_SERVER_ERROR';
  message: string;
  status: number;
  timestamp: string;
};
