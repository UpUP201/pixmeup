import { useState } from 'react';
import Icon from '../common/icons/Icon';

interface AgreementSectionProps {
  onAgreementChange: (
    isAllRequiredChecked: boolean,
    agreements: {
      service: boolean;
      privacy: boolean;
      sensitiveInfo: boolean;
      thirdParty: boolean;
      consignment: boolean;
      marketing: boolean;
    },
  ) => void;
}

// 약관 목록
const agreements = [
  {
    id: 'all',
    text: '전체 동의',
  },
  {
    id: 'service',
    text: '[필수] 서비스 이용 약관',
    content: '본 서비스 이용을 위한 기본적인 권리와 의무에 대한 내용을 확인하고 동의합니다.',
  },
  {
    id: 'privacy',
    text: '[필수] 개인정보 수집·이용 동의 (일반 정보)',
    content: '회원 가입 및 서비스 제공을 위해 이름, 연락처 등 일반 개인정보를 수집·이용합니다.',
  },
  {
    id: 'sensitiveInfo',
    text: '[필수] 민감정보(건강 정보) 수집·이용 동의',
    content: '맞춤형 서비스 제공을 위해 건강 관련 정보를 수집·이용합니다.',
  },
  {
    id: 'thirdParty',
    text: '[필수] 개인정보 제3자 제공 동의',
    content: '원활한 서비스 제공을 위해 필요한 경우 제휴사 등 제3자에게 개인정보를 제공합니다.',
  },
  {
    id: 'consignment',
    text: '[필수] 개인정보 처리위탁 동의',
    content: '서비스 운영 및 관리를 위해 개인정보 처리를 외부 업체에 위탁할 수 있습니다.',
  },
  {
    id: 'marketing',
    text: '[선택] 마케팅 및 광고 활용 동의',
    content: '이벤트, 혜택 안내 등 마케팅 정보를 수신하는 데 동의합니다.',
  },
];

export const AgreementSection = ({ onAgreementChange }: AgreementSectionProps) => {
  const [checkedItems, setCheckedItems] = useState<Record<string, boolean>>({
    all: false,
    service: false,
    privacy: false,
    sensitiveInfo: false,
    thirdParty: false,
    consignment: false,
    marketing: false,
  });

  const [expandedItems, setExpandedItems] = useState<Record<string, boolean>>({});

  const handleCheck = (id: string) => {
    if (id === 'all') {
      const newValue = !checkedItems.all;
      const newCheckedItems = {
        all: newValue,
        service: newValue,
        privacy: newValue,
        sensitiveInfo: newValue,
        thirdParty: newValue,
        consignment: newValue,
        marketing: newValue,
      };
      setCheckedItems(newCheckedItems);
      onAgreementChange(newValue, {
        service: newValue,
        privacy: newValue,
        sensitiveInfo: newValue,
        thirdParty: newValue,
        consignment: newValue,
        marketing: newValue,
      });
    } else {
      const newCheckedItems = {
        ...checkedItems,
        [id]: !checkedItems[id],
        all: false,
      };
      setCheckedItems(newCheckedItems);
      onAgreementChange(isAllRequiredChecked(newCheckedItems), {
        service: newCheckedItems.service,
        privacy: newCheckedItems.privacy,
        sensitiveInfo: newCheckedItems.sensitiveInfo,
        thirdParty: newCheckedItems.thirdParty,
        consignment: newCheckedItems.consignment,
        marketing: newCheckedItems.marketing,
      });
    }
  };

  const handleExpand = (id: string) => {
    setExpandedItems((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  const isAllRequiredChecked = (items: Record<string, boolean>) => {
    return (
      items.service && items.privacy && items.sensitiveInfo && items.thirdParty && items.consignment
    );
  };

  return (
    <div className="mt-8 space-y-4">
      <p className="mb-1 text-lg font-semibold">약관동의</p>
      <p className="text-sm text-gray-600">필수항목 및 선택항목 약관에 동의해 주세요</p>
      <div className="space-y-2">
        {agreements.map((agreement) => (
          <div
            key={agreement.id}
            className={` ${agreement.id === 'all' ? 'bg-line-50 rounded-sm border p-4 font-semibold' : 'px-4 py-2'}`}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <input
                  type="checkbox"
                  id={agreement.id}
                  checked={checkedItems[agreement.id]}
                  onChange={() => handleCheck(agreement.id)}
                  className="text-primary-500 focus:ring-primary-500 h-4 w-4 rounded border-gray-300"
                />
                <label htmlFor={agreement.id} className="ml-2 text-sm">
                  {agreement.text}
                </label>
              </div>
              {agreement.id !== 'all' && (
                <button
                  onClick={() => handleExpand(agreement.id)}
                  className="text-gray-500 hover:text-gray-700"
                >
                  {expandedItems[agreement.id] ? <Icon name="up" /> : <Icon name="down" />}
                </button>
              )}
            </div>
            {agreement.id !== 'all' && expandedItems[agreement.id] && (
              <div className="rounded p-3 text-sm text-gray-500">{agreement.content}</div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
