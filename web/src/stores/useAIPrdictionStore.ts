import { ImagePredictReport } from '@/types/report/EyeTestType';
import { create } from 'zustand';
import { EventSourcePolyfill } from 'event-source-polyfill';
import { api } from '@/lib/fetchAPI';

interface AIPredictionState {
  isStreaming: boolean;
  streamData: string[];
  error: string | null;
  progress: number;
  eventSource: EventSourcePolyfill | null;
  imagePredictResult: ImagePredictReport | null;
  
  startStream: (emitterId: string) => void;
  stopStream: () => void;
  addStreamData: (data: string) => void;
  setError: (error: string | null) => void;
  setProgress: (progress: number) => void;
  setImagePredictResult: (result: ImagePredictReport) => void;
  reset: () => void;
}

const useAIPredictionStore = create<AIPredictionState>((set, get) => ({
  isStreaming: false,
  streamData: [],
  error: null,
  progress: 0,
  eventSource: null,
  imagePredictResult: null,
  
  startStream: (emitterId) => {

    const eventSourceList = get().eventSource;
    // 기존 스트림 정리
    if (eventSourceList) {
      eventSourceList.close();
    }
    
    // 새 EventSource 생성
    const eventSource = new EventSourcePolyfill(api(`/image/subscribe?emitterId=${emitterId}`), {
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
      
      // 핑 이벤트 리스너
      eventSource.addEventListener('ping', (event: any) => {
        console.log('핑 이벤트:', event.data);
        // 핑에 대한 특별한 처리가 필요하면 여기에 작성
      });
      
      // 이미지 분석 결과 이벤트 리스너
      eventSource.addEventListener('image-result', (event: any) => {
        try {
          console.log('이미지 분석 결과 수신:', event.data);
          const data: ImagePredictReport = JSON.parse(event.data);
          get().setImagePredictResult(data);
          
          // 분석 완료 시 진행률 100%로 설정
          get().setProgress(100);
        } catch (e) {
          console.error('이미지 분석 결과 파싱 오류:', e);
          set({ error: '결과 처리 중 오류가 발생했습니다.' });
        }
      });

      eventSource.addEventListener("image-error", (event: any) => {

        try{

          console.log("이미지 분석 에러 수신:", event.data);

        }catch(e) {
          console.error(e);
        }

      })
    
    eventSource.onmessage = (event) => {
      try {
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
        error: 'Connection error occurred', 
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

  setImagePredictResult: (result: ImagePredictReport) => {
    set({imagePredictResult: result});
  },
  
  setProgress: (progress) => {
    set({ progress });
  },
  
  reset: () => {
    const { eventSource } = get();
    if (eventSource) {
      eventSource.close();
    }
    set({ 
      isStreaming: false,
      streamData: [],
      error: null,
      progress: 0,
      eventSource: null
    });
  }
}));

export default useAIPredictionStore;