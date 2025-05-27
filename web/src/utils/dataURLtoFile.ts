// base64 문자열 또는 data URI를 File 객체로 변환하는 함수
const dataURLtoFile = (dataurl: string, filename: string): File => {
    // data URI에서 MIME 타입과 데이터 부분 추출
    const arr = dataurl.split(',');
    const mime = arr[0].match(/:(.*?);/)?.[1] || 'image/png';
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);

    // 문자열 데이터를 Uint8Array로 변환
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }

    // File 객체 생성 및 반환
    return new File([u8arr], filename, { type: mime });
};

export default dataURLtoFile;