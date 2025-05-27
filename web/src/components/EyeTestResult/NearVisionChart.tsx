import { Bar, BarChart, CartesianGrid, XAxis, YAxis } from 'recharts';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from '@/components/ui/chart';

const chartConfig = {
  desktop: {
    label: '시력',
    color: 'hsl(var(--chart-1))',
  },
} satisfies ChartConfig;

interface ChartBar {
  날짜: string;
  date: string;
  시력: number;
}
interface Props {
  type: '왼쪽 눈' | '오른쪽 눈';
  data: ChartBar[];
  targetDate: string;
}

const NearVisionChart = ({ type, data, targetDate }: Props) => {
  if (data.length == 0) {
    return (
      <Card className="gap-4 border-0">
        <CardHeader>
          <CardTitle>{type}</CardTitle>
        </CardHeader>
        <div className="flex flex-col gap-2">
          <span className="text-line-500 text-text-xl text-center font-semibold">
            검사 결과가 없습니다.
          </span>
          <span className="text-line-500 text-text-md text-center font-semibold">
            <strong className="text-primary-600 font-semibold">내눈 키오스크</strong>를 통해서
            검사를 받아주세요.
          </span>
        </div>
      </Card>
    );
  }

  return (
    <Card className="border-0">
      <CardHeader>
        <CardTitle>{type}</CardTitle>
      </CardHeader>
      <CardContent>
        <ChartContainer config={chartConfig}>
          <BarChart accessibilityLayer data={data}>
            <CartesianGrid vertical={false} />
            <XAxis
              dataKey="날짜"
              tickLine={false}
              tickMargin={10}
              axisLine={false}
              tickFormatter={(value) => value.slice(0, 5)}
            />
            <YAxis domain={[0, 1]} hide tickCount={0} />
            <ChartTooltip cursor={false} content={<ChartTooltipContent hideLabel />} />
            <Bar
              dataKey="시력"
              radius={8}
              barSize={32}
              shape={(props: any) => {
                const isCurrentDate = props.payload.date == targetDate;
                const prevfill = isCurrentDate ? '#0B4990' : '#3989E8';
                const isPrediction = props.payload.date.slice(0, 4) == '9999';
                const fill = isPrediction ? '#94BEFF' : prevfill;

                return (
                  <rect
                    x={props.x}
                    y={props.y}
                    width={props.width}
                    height={props.height}
                    fill={fill}
                    radius={8}
                    rx={8}
                    ry={8}
                  />
                );
              }}
            />
          </BarChart>
        </ChartContainer>
      </CardContent>
    </Card>
  );
};

export default NearVisionChart;
