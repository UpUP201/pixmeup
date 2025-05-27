import { UserProfile } from '@/apis/auth/authApi';

interface ProfileInfoProps {
  data?: UserProfile;
  onEdit: () => void;
}

export const ProfileInfo = ({ data, onEdit }: ProfileInfoProps) => {
  const formatPhoneNumber = (phone: string) => {
    return phone.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
  };

  const formatBoolean = (value: boolean | null) => {
    if (value === null) return '-';
    return value ? 'O' : 'X';
  };

  if (!data) return null;

  return (
    <div>
      <div className="my-2 flex items-center justify-between">
        <h2 className="text-line-900 font-semibold">문진 기록</h2>
      </div>

      <div className="bg-line-10 space-y-3 rounded-sm p-5 text-sm">
        <div className="flex justify-between">
          <span className="text-line-900">성별</span>
          <span>{data.gender ?? '-'}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-line-900">나이</span>
          <span>{data.age ?? '-'}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-line-900">안경 착용 여부</span>
          <span>{formatBoolean(data.glasses)}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-line-900">눈수술 여부</span>
          <span>{data.surgery ?? '-'}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-line-900">흡연 여부</span>
          <span>{formatBoolean(data.smoking)}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-line-900">당뇨 여부</span>
          <span>{formatBoolean(data.diabetes)}</span>
        </div>
      </div>

      <div className="mt-4 mb-2 flex items-center justify-between">
        <h2 className="text-line-900 font-semibold">회원 정보</h2>
      </div>
      <div className="bg-line-10 mt-2 space-y-3 rounded-sm p-5 text-sm">
        <div className="flex justify-between">
          <span className="text-line-900">전화번호</span>
          <span className="text-line-600">{formatPhoneNumber(data.phoneNumber)}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-line-900">비밀번호</span>
          <span>
            <button onClick={onEdit} className="text-primary-700 cursor-pointer text-sm">
              수정하기
            </button>
          </span>
        </div>
      </div>
    </div>
  );
};
