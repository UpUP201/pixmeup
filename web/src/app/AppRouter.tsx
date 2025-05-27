import { createBrowserRouter } from 'react-router';
import { HeaderNavLayout, Layout } from '@/layouts';
import HeaderLayout from '@/layouts/HeaderLayout';
import { AuthGuard } from '@/components/auth/AuthGuard';
import { handleQRData } from '@/lib/qr/QRHandler';
import { lazy, Suspense } from 'react';
import { Exercise, Home, Landing, Login, Profile, Signup } from '@/pages';
import { RedirectWithParams } from './routes';
import ErrorPage from '@/components/common/Error';
import ChartPartSkeleton from '@/components/EyeTestResult/ChartPartSkeleton';
import AMDResultSkeleton from '@/components/EyeTestResult/AMDResultSkeleton';
import ImagePredictionSkeleton from '@/components/EyeTestResult/ImagePredictionSkeleton';
import ToastErrorBoundary from './wrappers/ToastErrorBoundary';

const PDFPage = lazy(() => import('@/pages/PDFPage'));
const ExerciseDetail = lazy(() => import('@/pages/Exercise/ExerciseDetail'));
const ExerciseIntro = lazy(() => import('@/pages/Exercise/ExerciseIntro'));
const ExerciseStep = lazy(() => import('@/pages/Exercise/ExerciseStep'));
const ExerciseComplete = lazy(() => import('@/pages/Exercise/ExerciseComplete'));
const EyeHistory = lazy(() => import('@/pages/Profile/EyeHistory'));
const AIHistory = lazy(() => import('@/pages/Profile/AIHistory'));
const ImagePrediction = lazy(() => import('@/pages/EyeTestReport/ImagePrediction'));
const ImagePredictionLoading = lazy(() => import('@/pages/EyeTestReport/ImagePredictionLoading'));
const ImagePredictionReport = lazy(() => import('@/pages/EyeTestReport/ImagePredictionReport'));
const AMDReport = lazy(() => import('@/pages/EyeTestReport/AMDReport'));
const EyeTestReportMain = lazy(() => import('@/pages/EyeTestReport/EyeTestReportMain'));
const NearVisionResult = lazy(() => import('@/pages/EyeTestReport/NearVisionResult'));
const PresbyopiaResult = lazy(() => import('@/pages/EyeTestReport/PresbyopiaResult'));
const AMDResult = lazy(() => import('@/pages/EyeTestReport/AMDResult'));
const AMDReportLoading = lazy(() => import('@/pages/EyeTestReport/AMDReportLoading'));
const AMDReportError = lazy(() => import('@/pages/EyeTestReport/AMDReportError'));

// url에 QR 데이터가 있는 경우 로컬스토리지에 저장
handleQRData();

const AppRouter = createBrowserRouter([
  // 빈 Layout - 인증 불필요
  {
    element: <Layout />,
    errorElement: <ErrorPage />,
    children: [
      {
        element: (
          <AuthGuard requireAuth={false}>
            <Landing />
          </AuthGuard>
        ),
        path: 'landing',
      },
      // 인증 관련 페이지 - 인증 불필요
      {
        element: (
          <AuthGuard requireAuth={false}>
            <Login />
          </AuthGuard>
        ),
        path: 'login',
      },
      {
        element: (
          <AuthGuard requireAuth={false}>
            <Signup />
          </AuthGuard>
        ),
        path: 'signup',
      },
    ],
  },
  // Header 만 존재하는 Layout - 인증 필요
  {
    element: (
      <AuthGuard>
        <HeaderLayout />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />,
    children: [
      // 눈 운동 페이지
      {
        path: 'exercise',
        children: [
          {
            path: ':id',
            element: <ExerciseDetail />,
            handle: { name: '눈 운동', path: '/exercise' },
          },
          {
            path: ':id/intro',
            element: <ExerciseIntro />,
            handle: { name: '눈 운동', path: '/exercise' },
          },
          {
            path: ':id/step',
            element: <ExerciseStep />,
            handle: { name: '눈 운동', path: '/exercise' },
          },
          {
            path: ':id/complete',
            element: <ExerciseComplete />,
            handle: { name: '눈 운동', path: '/exercise' },
          },
        ],
      },

      // AMD AI 리포트
      {
        path: 'amd-report/loading',
        element: <AMDReportLoading />,
        handle: { name: 'AMD 예측 리포트' },
      },
      // AI 정밀 진단
      {
        path: 'image-prediction',
        element: <ImagePrediction />,
        handle: { name: 'AI 정밀 진단', path: '/' },
      },
      // AI 정밀 진단 로딩
      {
        path: 'image-prediction/loading',
        element: <ImagePredictionLoading />,
        handle: { name: 'AI 정밀 진단' },
      },
    ],
  },
  // Header 와 Nav가 공존하는 Layout - 인증 필요
  {
    element: (
      <AuthGuard>
        <HeaderNavLayout />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />,
    children: [
      // 홈
      {
        element: <Home />,
        path: '/',
        handle: { name: 'PixmeUp', show: false },
      },
      // 프로필 페이지
      {
        path: 'profile',
        children: [
          {
            path: '',
            element: <Profile />,
            handle: { name: '내 정보', show: false },
          },
          {
            path: 'eyehistory',
            element: <EyeHistory />,
            handle: { name: '눈 검사 기록' },
          },
          {
            path: 'aihistory',
            element: <AIHistory />,
            handle: { name: 'AI 진단 기록' },
          },
        ],
      },
      // 눈 운동 페이지
      {
        path: 'exercise',
        children: [
          {
            path: '',
            element: <Exercise />,
            handle: { name: '눈 운동', show: false },
          },
        ],
      },
      // AI 정밀 진단 결과
      {
        path: 'image-prediction/report/:id',
        errorElement: <ToastErrorBoundary />,
        element: (
          <Suspense fallback={<ImagePredictionSkeleton />}>
            <ImagePredictionReport />
          </Suspense>
        ),
        handle: { name: 'AI 정밀 진단 결과' },
      },
      // AMD AI 리포트 결과
      {
        path: 'amd-report/:id',
        errorElement: <ToastErrorBoundary />,
        element: (
          <Suspense fallback={<AMDResultSkeleton />}>
            <AMDReport />
          </Suspense>
        ),
        handle: { name: 'AMD 예측 리포트' },
      },
      // AMD AI 리포트 결과
      {
        path: 'amd-report/error',
        element: <AMDReportError />,
        handle: { name: 'AMD 예측 리포트' },
      },
      // 눈 검사 결과 리포트
      {
        path: 'report',
        element: <EyeTestReportMain />,
        children: [
          {
            index: true,
            element: <RedirectWithParams to="/report/near-vision" />,
          },
          {
            path: 'near-vision',
            errorElement: <ToastErrorBoundary />,
            element: (
              <Suspense fallback={<ChartPartSkeleton />}>
                <NearVisionResult />
              </Suspense>
            ),
            handle: { name: '내 리포트', show: false },
          },
          {
            path: 'presbyopia',
            errorElement: <ToastErrorBoundary />,
            element: (
              <Suspense fallback={<ChartPartSkeleton />}>
                <PresbyopiaResult />
              </Suspense>
            ),
            handle: { name: '내 리포트', show: false },
          },
          {
            path: 'amd',
            errorElement: <ToastErrorBoundary />,
            element: (
              <Suspense fallback={<ChartPartSkeleton />}>
                <AMDResult />
              </Suspense>
            ),
            handle: { name: '내 리포트', show: false },
          },
        ],
      },
    ],
  },
  {
    path: '/pdf-page',
    element: <PDFPage />,
  },
  // 존재하지 않는 경로에 대한 대체 경로 (404)
  {
    path: '*',
    element: <ErrorPage />,
  },
]);

export default AppRouter;
