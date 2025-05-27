import { fetchAPI } from "@/lib/fetchAPI";
import { DiagnosisReportResult, ImagePredictReport } from "@/types/report/EyeTestType";

export const postAIImagePrediction = ({s3Key}:{s3Key: string}) => fetchAPI<{emitterId: string}>({
  method: "POST",
  url:"/image/request",
  data: {s3Key}
})

export const getAIImagePredictionResult = ({resultId} : {resultId: string}) => fetchAPI<ImagePredictReport>({
  method: "GET",
  url: `/image/result/${resultId}`
})

export const postDiagnosisResult = () => fetchAPI<{emitterId: string}>({
  method: "POST",
  url: "/diagnosis/request"
})

export const getDiagnosisResult = ({resultId} : {resultId: string}) => fetchAPI<DiagnosisReportResult>({
  method: "GET",
  url:`/diagnosis/result/${resultId}`
})