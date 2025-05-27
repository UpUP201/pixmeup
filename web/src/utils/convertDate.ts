const convertDate = (date: string) => {

  // 2025-05-07 07:00:00

  const YearMonthDay = date.split("T")[0];

  const SplitDate = YearMonthDay.split("-");

  const Month = SplitDate[1];
  const Day = SplitDate[2];

  return Month + "-" + Day;
}

export default convertDate;
