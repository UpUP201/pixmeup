export const base64ToBuffer = (base64url: string): ArrayBuffer => {
  // base64url을 일반 base64로 변환
  const base64 = base64url.replace(/-/g, '+').replace(/_/g, '/').replace(/=/g, ''); // 패딩 제거

  // 패딩 추가
  const pad = base64.length % 4;
  const paddedBase64 = pad ? base64 + '='.repeat(4 - pad) : base64;

  const binaryString = window.atob(paddedBase64);
  const bytes = new Uint8Array(binaryString.length);
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }
  return bytes.buffer;
};

export const bufferToBase64 = (buffer: ArrayBuffer): string => {
  const bytes = new Uint8Array(buffer);
  let binary = '';
  for (let i = 0; i < bytes.byteLength; i++) {
    binary += String.fromCharCode(bytes[i]);
  }
  return window.btoa(binary);
};

export const bufferToBase64Url = (buffer: ArrayBuffer): string => {
  const base64 = bufferToBase64(buffer);
  return base64.replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
};

export const getDeviceName = (): string => {
  const userAgent = navigator.userAgent;
  if (/iPhone|iPad|iPod/.test(userAgent)) {
    return 'iOS Device';
  } else if (/Android/.test(userAgent)) {
    return 'Android Device';
  } else if (/Windows/.test(userAgent)) {
    return 'Windows PC';
  } else if (/Mac/.test(userAgent)) {
    return 'Mac';
  } else {
    return 'Unknown Device';
  }
};

export const isWebAuthnSupported = (): boolean => {
  return window.PublicKeyCredential !== undefined;
};
