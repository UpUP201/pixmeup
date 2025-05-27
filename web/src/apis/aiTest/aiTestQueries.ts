import { useSuspenseQuery } from "@tanstack/react-query";
import { getAIImagePredictionResult, getDiagnosisResult } from "./aiTestApi";

export const useGetAIImagePredictionResultQuery = ({resultId}:{resultId:string}) => useSuspenseQuery({
  queryKey: ["image-prediction-result", resultId],
  queryFn: () => getAIImagePredictionResult({resultId}),
  staleTime: 60 * 1000 * 5
})

export const useGetDiagnosisResultQuery = ({resultId}: {resultId: string}) => useSuspenseQuery({
  queryKey: ["diagnosis-result", resultId],
  queryFn: () => getDiagnosisResult({resultId}),
  staleTime: 60 * 1000 * 5
})