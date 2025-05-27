export interface ResponseDTO<T> {
  status: number;
  message: string;
  data: T;
  timestamp: string;
}

export interface ErrorResponseDTO {
  status: number;
  name: string;
  message: string;
  timestamp: string;
}
