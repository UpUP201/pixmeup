import { useUserProfile } from '@/hooks/auth/useUserProfile';

const UserProfile = () => {
  const { profile } = useUserProfile();

  return (
    <div>
      <p className="pb-1 text-xl font-bold">{profile?.name}님</p>
      <p>
        {profile?.daysSinceCheck === -1 ? (
          '검사 내역이 없습니다.'
        ) : (
          <>
            검사를 한 지{' '}
            <span className="text-primary-700 font-semibold">{profile?.daysSinceCheck}일</span>{' '}
            지났습니다.
          </>
        )}
      </p>
    </div>
  );
};

export default UserProfile;
