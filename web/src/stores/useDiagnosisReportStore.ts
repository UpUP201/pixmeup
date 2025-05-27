// stores/aiPredictionStore.ts
import { create } from 'zustand';
import { EventSourcePolyfill } from 'event-source-polyfill';
import { api } from '@/lib/fetchAPI';
import { DiagnosisReportResult } from '@/types/report/EyeTestType';

interface DiagnosisReportState {
  isStreaming: boolean;
  streamData: string[];
  error: string | null;
  aredsError: string | null;
  progress: number;
  eventSource: EventSourcePolyfill | null;
  diagnosisResult: DiagnosisReportResult | null;
  
  startStream: (emitterId: string) => void;
  stopStream: () => void;
  addStreamData: (data: string) => void;
  setError: (error: string | null) => void;
  setProgress: (progress: number) => void;
  setDiagnosisResult: (result: DiagnosisReportResult) => void;
  setAredsError: (aredsError: string | null) => void;
  reset: () => void;
}

const useDiagnosisReportStore = create<DiagnosisReportState>((set, get) => ({
  isStreaming: false,
  streamData: [],
  error: null,
  progress: 0,
  eventSource: null,
  diagnosisResult: null,
  aredsError: null,
  
  startStream: (emitterId) => {

    const eventSourceList = get().eventSource;
    // 기존 스트림 정리
    if (eventSourceList) {
      eventSourceList.close();
    }
    
    // 새 EventSource 생성
    const eventSource = new EventSourcePolyfill(api(`/diagnosis/subscribe?emitterId=${emitterId}`), {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem("accessToken")}`
      },
      withCredentials: true
    });
    
    eventSource.onopen = () => {
      set({ isStreaming: true, error: null });
    };

    // 연결 초기화 이벤트 리스너
    eventSource.addEventListener('init', (event: any) => {
      console.log('연결 초기화:', event.data);
      set({ isStreaming: true, error: null });
    });
    
    // 핑 이벤트 리스너 (필요 시)
    eventSource.addEventListener('ping', (event: any) => {
      console.log('핑 이벤트:', event.data);
      // 핑에 대한 특별한 처리가 필요하면 여기에 작성
    });
    
    // 이미지 분석 결과 이벤트 리스너
    eventSource.addEventListener('areds-result', (event: any) => {
      try {
        
        const data = JSON.parse(event.data);
        console.log(data);
        get().setDiagnosisResult(data);
        
        // 필요에 따라 진행률 업데이트
        get().setProgress(100); // 분석 완료 시 100%로 설정
      } catch (e) {
        console.error('AMD 결과 파싱 오류:', e);
        set({ error: '결과 처리 중 오류가 발생했습니다.' });
      }
    });

    eventSource.addEventListener("areds-error", (event: any) => {
      console.error("예측 실패:", event.data);
      get().setAredsError(event.data);
    });
    
    eventSource.onmessage = (event) => {
      try {
        console.log(event);
        const data = JSON.parse(event.data);
        if (data.type === 'final_answer_token') {
          get().addStreamData(data.token);
        } else if (data.type === 'progress') {
          get().setProgress(data.percentage);
        }
      } catch (e) {
        console.error('Error parsing message:', e);
      }
    };
    
    eventSource.onerror = () => {
      set({ 
        error: `Connection error occurred`, 
        isStreaming: false 
      });
      eventSource.close();
    };
    
    set({ eventSource });
  },
  
  stopStream: () => {
    const { eventSource } = get();
    if (eventSource) {
      eventSource.close();
      set({ eventSource: null, isStreaming: false });
    }
  },
  
  addStreamData: (data) => {
    set(state => ({ 
      streamData: [...state.streamData, data] 
    }));
  },
  
  setError: (error) => {
    set({ error });
  },
  
  setProgress: (progress) => {
    set({ progress });
  },

  setDiagnosisResult: (result) => {
    set({ diagnosisResult: result });
  },

  setAredsError: (aredsError) => {
    set({aredsError})
  },
  
  reset: () => {
    const { eventSource } = get();
    console.log("리셋!");
    if (eventSource) {
      eventSource.close();
    }
    set({ 
      isStreaming: false,
      streamData: [],
      error: null,
      progress: 0,
      eventSource: null,
      diagnosisResult: null
    });
  }
}));

export default useDiagnosisReportStore;