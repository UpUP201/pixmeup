import { useMutation, useQuery } from "@tanstack/react-query";
import { getS3UploadURL, putS3ImageUpload } from "./s3Api";

export const useGetS3UploadURLData = ({
  fileName,
  contentType,
  enabled,
}:{
  fileName: string;
  contentType: string;
  enabled: boolean;
}) => useQuery({
  queryKey: ["s3-upload-url", fileName, contentType],
  queryFn: () => getS3UploadURL({fileName, contentType}),
  staleTime: 60 * 1000 * 60 * 10,
  enabled: enabled
});

export const useUploadS3ImageMutation = () => useMutation({
  mutationFn: putS3ImageUpload,
  onSuccess: () => {
    console.log("어 성공~");
  },
  onError: () => {
    console.log("응 실패~");
  }
})