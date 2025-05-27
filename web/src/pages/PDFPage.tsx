import { PDFViewer } from '@react-pdf/renderer';
import EyeDocument from './EyeDocument';
import { useGetUserTotalInfoData } from '@/apis/eyeTest/eyeTestQueries';

const PDFPage = () => {
  const { data: PDFData } = useGetUserTotalInfoData();

  if (!PDFData) return null;

  return (
    <PDFViewer className="h-dvh w-full">
      <EyeDocument data={PDFData.data} />
    </PDFViewer>
  );
};

export default PDFPage;
