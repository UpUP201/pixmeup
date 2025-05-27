import { Area, AreaChart, CartesianGrid, XAxis, YAxis } from 'recharts';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from '@/components/ui/chart';
import { useEffect } from 'react';

const chartConfig = {
  desktop: {
    label: '안구 나이',
    color: 'hsl(var(--chart-1))',
  },
} satisfies ChartConfig;

interface chartBar {
  date: string;
  '안구 나이': number;
}

interface Props {
  chartData: chartBar[];
  agePrediction?: number;
  targetDate: string;
}

const PresbyopiaChart = ({ chartData, agePrediction, targetDate }: Props) => {
  useEffect(() => {
    console.log(chartData);
  }, [chartData]);

  if (chartData.length == 0) {
    return (
      <Card className="gap-4 border-0">
        <CardHeader>
          <CardTitle>안구 나이 통계</CardTitle>
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
        <CardTitle>안구 나이 통계</CardTitle>
      </CardHeader>
      <CardContent className="h-[172px]">
        <ChartContainer config={chartConfig}>
          <AreaChart
            accessibilityLayer
            data={chartData}
            margin={{
              left: 20,
              right: 20,
            }}
          >
            <CartesianGrid vertical={false} />
            <XAxis
              dataKey="date"
              tickLine={false}
              axisLine={false}
              tickMargin={8}
              interval={0}
              tick={({ x, y, payload }) => {
                return (
                  <g transform={`translate(${x},${y})`}>
                    <text
                      x={0}
                      y={0}
                      dy={8}
                      textAnchor="middle"
                      style={{ fill: payload.value === targetDate ? '#3F97FF' : '#666' }}
                      fontWeight={payload.value === targetDate ? 'bold' : 'normal'}
                    >
                      {payload.value.slice(0, 5)}
                    </text>
                  </g>
                );
              }}
            />
            <YAxis domain={[0, 100]} hide />
            <ChartTooltip cursor={false} content={<ChartTooltipContent indicator="line" />} />
            <Area
              dataKey="안구 나이"
              type="natural"
              fill="#3F97FF"
              fillOpacity={0.4}
              stroke="#3F97FF"
            />
          </AreaChart>
        </ChartContainer>
      </CardContent>
      <CardFooter>
        <div className="flex w-full items-start gap-2 text-sm">
          <div className="grid gap-2">
            <div className="flex items-center gap-2 leading-none font-medium">
              <span>예상 안구 나이:</span>
              <span className="text-secondory-blue-600">{agePrediction ?? '-'}세</span>
            </div>
          </div>
        </div>
      </CardFooter>
    </Card>
  );
};

export default PresbyopiaChart;
