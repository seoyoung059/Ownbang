import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";
import { useTheme } from "@mui/material";

const MyRealEstate = ({ myRealEstate }) => {
  const theme = useTheme();
  const formatCurrency = (value) => {
    return value ? Number(value).toLocaleString() : "0";
  };

  return (
    <TableContainer
      component={Paper}
      sx={{ width: "80%", margin: "auto", marginTop: 8 }}
    >
      <Table sx={{ minWidth: 500 }}>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell align="right">매물 사진</TableCell>
            <TableCell align="right">매물 주소지</TableCell>
            <TableCell align="right">방 종류</TableCell>
            <TableCell align="right">구조</TableCell>
            <TableCell align="right">거래 종류</TableCell>
            <TableCell align="right">전용 면적</TableCell>
            <TableCell align="right">공급 면적</TableCell>
            <TableCell align="right">층수</TableCell>
            <TableCell align="right">보증금</TableCell>
            <TableCell align="right">월세</TableCell>
            <TableCell align="right">관리비</TableCell>
          </TableRow>
        </TableHead>
        <TableBody sx={{ backgroundColor: theme.palette.background.default }}>
          {myRealEstate.map((row, index) => (
            <TableRow
              key={row.id}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {index + 1}
              </TableCell>
              <TableCell align="right">
                {row.profileImageUrl ? (
                  <img
                    src={row.profileImageUrl}
                    alt="Profile"
                    style={{ width: 50, height: 50 }}
                  />
                ) : (
                  "No Image"
                )}
              </TableCell>
              <TableCell align="right">
                {row.parcel ? row.parcel : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.roomType ? row.roomType : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.structure ? row.structure : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.dealType ? row.dealType : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.exclusiveArea ? row.exclusiveArea : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.supplyArea ? row.supplyArea : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.roomFloor ? row.roomFloor : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.deposit ? formatCurrency(row.deposit) : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.monthlyRent ? formatCurrency(row.monthlyRent) : "N/A"}
              </TableCell>
              <TableCell align="right">
                {row.maintenanceFee
                  ? formatCurrency(row.maintenanceFee)
                  : "N/A"}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default MyRealEstate;
