import { fetchAPI } from "@/lib/fetchAPI";
import { UploadURLResponse } from "@/types/s3";

export const getS3UploadURL = ({fileName, contentType}: {fileName: string; contentType: string}) => fetchAPI<UploadURLResponse>({
  method: "GET",
  url: `/s3/upload-url?fileName=${fileName}&contentType=${contentType}`,
})

export const putS3ImageUpload = ({formData, s3Url, contentType}: {formData: FormData; s3Url: string, contentType: string}) => {

  const file = formData.get('file'); 

  // 방법 1: replace와 정규식 사용
  const replaceUrl = s3Url.replace(/&amp;/g, '&');

  return fetch(replaceUrl, {
  method: "PUT",
  body: file,
  headers: {
    "Content-Type": contentType
    }
  }
)}

