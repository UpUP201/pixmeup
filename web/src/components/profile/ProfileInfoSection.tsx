import { ProfileInfo } from '@/components/profile/ProfileInfo';
import { useUserProfile } from '@/hooks/auth/useUserProfile';

interface ProfileInfoSectionProps {
  onEdit: () => void;
}

const ProfileInfoSection = ({ onEdit }: ProfileInfoSectionProps) => {
  const { profile } = useUserProfile();

  return (
    <div>
      <ProfileInfo data={profile!} onEdit={onEdit} />
    </div>
  );
};

export default ProfileInfoSection;
