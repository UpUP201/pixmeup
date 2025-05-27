import { useMutation } from "@tanstack/react-query";
import { postAIImagePrediction, postDiagnosisResult } from "./aiTestApi";
import { useAIPredictionStore } from "@/stores";
import { useNavigate } from "react-router";
import useDiagnosisReportStore from "@/stores/useDiagnosisReportStore";
import { useRef } from "react";


export const useImageAIPredictionMutation = () => {

  const startStreamRef = useRef(false); 
  const navigate = useNavigate();
  const startStream = useAIPredictionStore(state => state.startStream);
  const reset = useAIPredictionStore(state => state.reset);
  
  return useMutation({
    mutationFn: postAIImagePrediction,
    onSuccess: (response) => {
      const uuid = response.data.emitterId;

      if (!startStreamRef.current) {
        startStreamRef.current = true;
        reset();
        
        // setTimeout을 사용하여 약간의 지연 추가
        setTimeout(() => {
          startStream(uuid);
          navigate("/image-prediction/loading", {replace: true});
          startStreamRef.current = false;
        }, 100);
        
      }
      navigate("/image-prediction/loading", {replace: true});
    },
    onError: (error) => {
      console.error("에러 :", error)
    }
})
};

export const useDiagnosisResultMutation = () => {

  const startStreamRef = useRef(false);
  const startStream = useDiagnosisReportStore(state => state.startStream);
  const reset = useDiagnosisReportStore(state => state.reset);

  return useMutation({
    mutationFn: postDiagnosisResult,
    onSuccess: (response) => {

      const uuid = response.data.emitterId;

      if (!startStreamRef.current) {
        startStreamRef.current = true;
        reset();
        
        // setTimeout을 사용하여 약간의 지연 추가
        setTimeout(() => {
          startStream(uuid);
          startStreamRef.current = false;
        }, 100);
      }
    },
    onError: (error) => {
      console.error("에러 : ", error);
      startStreamRef.current = false;
    }
  });
}