import { fetchAPI } from "@/lib/fetchAPI";
import PDFDocument from "@/types/PDFDocument";
import { EyeTestResults, MacularReport, MyopiaReportList, PresbyopiaReportList, RecentVisionReportRequest, ReportTotalListResponse } from "@/types/report/EyeTestType";

export const getMyopiaReportsAPI = ({ targetDateTime }: { targetDateTime?: string }) =>
  fetchAPI<MyopiaReportList>({
    method: 'GET',
    url: `/reports/myopia${targetDateTime ? `?targetDateTime=${targetDateTime}` : ""}`,
  });

export const getPresbyopiaReportsAPI = ({ targetDateTime }: { targetDateTime?: string }) =>
  fetchAPI<PresbyopiaReportList>({
    method: 'GET',
    url: `/reports/presbyopia${targetDateTime ? `?targetDateTime=${targetDateTime}` : ""}`,
  });

export const getUserResultReportAPI = () =>
  fetchAPI<EyeTestResults>({
    method: 'GET',
    url: '/users/result',
  });

export const getVisionReportsAPI = ({selectedDateTime}:RecentVisionReportRequest) => fetchAPI<EyeTestResults>({
  method: "GET",
  url: `/reports/vision${selectedDateTime ? `?selectedDateTime=${selectedDateTime}` : ""}`,
})

export const getMacularReportsAPI = ({targetDateTime}:{targetDateTime?: string}) => fetchAPI<MacularReport>({
  method: "GET",
  url: `/reports/macular${targetDateTime ? `?targetDateTime=${targetDateTime}` : ""}`
})

export const getReportTotalList = (page: number = 0, size: number = 10) =>
  fetchAPI<ReportTotalListResponse>({
    method: 'GET',
    url: `/reports/total-list?page=${page}&size=${size}`,
  });

export const getUserTotalInfo = () => fetchAPI<PDFDocument>({
  method: "GET",
  url: "/users/total-info"
})