export default interface PDFDocument {
  
  // 함
  name: string;
  phoneNumber: string;
  latestTestDate: string;

  // 함
  gender: "남자" | "여자";
  age: string;
  currentSmoking: boolean;
  diabetes: boolean;
  glasses: boolean;
  pastSmoking: boolean;
  surgery: string;

  // 함
  amslerCheck: {
    aiResult: string | null;
    leftMacularLoc: string | null;
    rightMacularLoc: string | null;
  };

  // 함
  mChartCheck: {
    aiResult: string | null;
    leftEyeHor: number | null;
    leftEyeVer: number | null;
    rightEyeHor: number | null;
    rightEyeVer: number | null;
  };

  // 함
  presbyopiaCheck: {
    age: number | null;
    agePrediction: number | null;
    aiResult: number | null;
    status: string | null;
  };

  // 함
  sightCheck: {
    aiResult: string | null;
    leftSight: number | null;
    leftSightPrediction: number | null;
    rightSight: number | null;
    rightSightPrediction: number | null;
    status: string | null;
  }

}

