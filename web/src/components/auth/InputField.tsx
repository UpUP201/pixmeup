import { InputChangeEvent } from "@/types/auth/common";

interface InputFieldProps {
  type: 'text' | 'tel' | 'password';
  value: string;
  onChange: (e: InputChangeEvent) => void;
  placeholder: string;
  className?: string;
}

export const InputField = ({
  type,
  value,
  onChange,
  placeholder,
  className = ''
}: InputFieldProps) => {
  return (
    <input
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      className={`w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent ${className}`}
    />
  );
}; 