import { ChangePasswordModal } from '@/components/profile/ChangePasswordModal';
import { useState, Suspense } from 'react';
import ProfileHeader from './../../components/profile/ProfileHeader';
import ProfileHeaderSkeleton from '@/components/profile/ProfileHeaderSkeleton';
import ProfileInfoSection from '@/components/profile/ProfileInfoSection';
import ProfileInfoSkeleton from '@/components/profile/ProfileInfoSectionSkeleton';
import ProfileHistorySkeleton from '@/components/profile/ProfileHistorySkeleton';
import ProfileHistory from '@/components/profile/ProfileHistory';
import ProfileActionsSkeleton from '@/components/profile/ProfileActionsSkeleton';
import ProfileActions from '@/components/profile/ProfileActions';

const ProfilePage = () => {
  const [showPasswordModal, setShowPasswordModal] = useState(false);

  return (
    <div className="h-full overflow-y-auto pb-16">
      <main className="mb-6 px-5">
        <div className="flex flex-col gap-4">
          {/* 사용자 정보 */}
          <Suspense fallback={<ProfileHeaderSkeleton />}>
            <ProfileHeader />
          </Suspense>

          {/* 회원정보 */}
          <Suspense fallback={<ProfileInfoSkeleton />}>
            <ProfileInfoSection onEdit={() => setShowPasswordModal(true)} />
          </Suspense>

          {/* 기록 */}
          <Suspense fallback={<ProfileHistorySkeleton />}>
            <ProfileHistory />
          </Suspense>

          {/* 하단 버튼 */}
          <Suspense fallback={<ProfileActionsSkeleton />}>
            <ProfileActions />
          </Suspense>
        </div>
      </main>

      {/* 비밀번호 변경 모달 */}
      <ChangePasswordModal isOpen={showPasswordModal} onClose={() => setShowPasswordModal(false)} />
    </div>
  );
};

export default ProfilePage;
