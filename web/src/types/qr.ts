export interface Presbyopia {
  firstDistance: number;
  secondDistance: number;
  thirdDistance: number;
  avgDistance: number;
  age: number;
}

export interface ShortVisualAcuity {
  leftEye: number;
  rightEye: number;
}

export interface AmslerGrid {
  leftEyeDisorderType: (string | null)[];
  rightEyeDisorderType: (string | null)[];
}

export interface MChart {
  leftEyeVertical: number;
  rightEyeVertical: number;
  leftEyeHorizontal: number;
  rightEyeHorizontal: number;
}

export interface TestResults {
  presbyopia: Presbyopia | null;
  shortVisualAcuity: ShortVisualAcuity | null;
  amslerGrid: AmslerGrid | null;
  mChart: MChart | null;
}

export type QRDataRequest = {
  age: number;
  gender: string;
  glasses: boolean;
  surgery: string;
  diabetes: boolean;
  pastSmoking: boolean;
  currentSmoking: boolean;
  testResults: TestResults;
};
