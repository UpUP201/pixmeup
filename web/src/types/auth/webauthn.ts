export interface WebAuthnRegisterStartResponse {
  data: {
    challenge: string;
    rp: {
      id: string;
      name: string;
    };
    user: {
      id: string;
      name: string;
      displayName: string;
    };
    pubKeyCredParams: Array<{
      type: 'public-key';
      alg: number;
    }>;
    timeout: number;
    excludeCredentials: Array<{
      type: 'public-key';
      id: string;
      transports?: Array<'ble' | 'internal' | 'nfc' | 'usb'>;
    }>;
    authenticatorSelection: {
      authenticatorAttachment: 'platform' | 'cross-platform';
      userVerification: 'required' | 'preferred' | 'discouraged';
      residentKey: 'required' | 'preferred' | 'discouraged';
    };
    attestation: 'none' | 'indirect' | 'direct';
  };
  sessionId: string;
}

export interface WebAuthnRegisterFinishRequest {
  userId: number;
  sessionId: string;
  credential: {
    id: string;
    rawId: string;
    type: string;
    response: {
      attestationObject: string;
      clientDataJSON: string;
    };
    clientExtensionResults: AuthenticationExtensionsClientOutputs;
  };
  deviceName: string;
}

export interface WebAuthnRegisterFinishResponse {
  data: {
    credentialId: string;
    deviceType: string;
    createdAt: string;
  };
  sessionId: string;
}

// WebAuthn 인증 데이터의 내부 구조를 명확하게 정의
export interface WebAuthnAuthData {
  challenge: string;
  timeout: number;
  allowCredentials: Array<{
    type: 'public-key';
    id: string;
    transports?: Array<'ble' | 'internal' | 'nfc' | 'usb'>;
  }>;
  userVerification?: 'required' | 'preferred' | 'discouraged';
}

export interface WebAuthnAuthenticateStartData {
  data: WebAuthnAuthData;
  sessionId: string;
}

export type WebAuthnAuthenticateStartResponse = WebAuthnAuthenticateStartData;

export interface WebAuthnAuthenticateFinishRequest {
  sessionId: string;
  credential: {
    id: string;
    rawId: string;
    type: string;
    response: {
      authenticatorData: string;
      clientDataJSON: string;
      signature: string;
      userHandle: string;
    };
  };
  deviceName: string;
}

export interface WebAuthnAuthenticateFinishResponse {
  userId: number;
  userName: string;
  accessToken: string;
  credentialId: string;
}
