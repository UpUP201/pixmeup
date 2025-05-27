import { useUserProfile } from '@/hooks/auth/useUserProfile';

const ProfileHeader = () => {
  const { profile } = useUserProfile();

  // 팝업 창 열기 함수
  const openPDFPopup = () => {
    // 팝업 창 크기 및 위치 설정
    const width = 800;
    const height = 900;
    const left = window.innerWidth / 2 - width / 2;
    const top = window.innerHeight / 2 - height / 2;

    // 팝업 창 열기 (window.open 사용)
    const popup = window.open(
      '/pdf-page', // PDF 컴포넌트가 있는 경로
      'PDFViewer',
      `width=${width},height=${height},left=${left},top=${top},resizable=yes,scrollbars=yes,status=yes`,
    );

    // 팝업 창이 차단되었는지 확인
    if (!popup || popup.closed || typeof popup.closed === 'undefined') {
      alert('팝업 창이 차단되었습니다. 팝업 차단을 해제해 주세요.');
    }
  };

  return (
    <div className="flex items-end justify-between">
      <div>
        <p className="pb-1 text-xl font-bold">{profile?.name}님</p>
        <div>
          {profile?.daysSinceCheck === -1 ? (
            '검사 내역이 없습니다.'
          ) : (
            <>
              검사를 한 지 <span className="text-primary-700">{profile?.daysSinceCheck}일</span>{' '}
              지났습니다.
            </>
          )}
        </div>
      </div>
      <button
        className="bg-primary-200 hover:bg-primary-400 cursor-pointer rounded-md px-3 py-2"
        type="button"
        onClick={openPDFPopup}
      >
        PDF 열기
      </button>
    </div>
  );
};

export default ProfileHeader;
