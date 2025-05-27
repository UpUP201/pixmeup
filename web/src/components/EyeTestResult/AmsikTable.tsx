import { AmsikResult } from '@/types/report/EyeTestType';

interface Props {
  test: AmsikResult;
}

const AmsikTable = ({ test }: Props) => {
  return (
    <table className="w-fit">
      <thead className="">
        <tr className="bg-secondory-lemon-100 flex w-fit justify-between rounded-t-sm">
          <th className="border-line-50 border-t-line-100 border-l-line-100 w-26 rounded-tl-sm border py-2.5"></th>
          <th className="border-line-50 text-text-md border-t-line-100 w-26 border py-2.5 font-medium">
            왼쪽 눈
          </th>
          <th className="border-line-50 text-text-md border-t-line-100 border-r-line-100 w-26 rounded-tr-sm border py-2.5 font-medium">
            오른쪽 눈
          </th>
        </tr>
      </thead>
      <tbody>
        <tr className="flex w-fit justify-between">
          <td className="border-line-50 text-text-md border-l-line-100 w-26 border py-2.5 text-center font-medium">
            수직
          </td>
          <td className="border-line-50 text-text-lg w-26 border py-2.5 text-center font-semibold">
            {test.vertical.left / 10}
          </td>
          <td className="border-line-50 text-text-lg border-r-line-100 w-26 border py-2.5 text-center font-semibold">
            {test.vertical.right / 10}
          </td>
        </tr>
        <tr className="flex w-fit justify-between rounded-b-md">
          <td className="border-line-50 text-text-md border-l-line-100 border-b-line-100 w-26 rounded-bl-sm border py-2.5 text-center font-medium">
            수평
          </td>
          <td className="border-line-50 text-text-lg border-b-line-100 w-26 border py-2.5 text-center font-semibold">
            {test.horizontaol.left / 10}
          </td>
          <td className="border-line-50 text-text-lg border-b-line-100 border-r-line-100 w-26 rounded-br-sm border py-2.5 text-center font-semibold">
            {test.horizontaol.right / 10}
          </td>
        </tr>
      </tbody>
    </table>
  );
};

export default AmsikTable;
