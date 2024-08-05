import React from 'react';
import { TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Paper } from '@mui/material';
import AgentReservationItem from './AgentReservationItem';
import { useMediaQuery, useTheme } from '@mui/material';

const reservations = [
    {
        id: 1,
        image: 'https://via.placeholder.com/150',
        date: '2024.06.26',
        time: '15:00 ~ 15:30',
        customerName: '김일태',
        status: '확정',
        statusColor: 'success',
        customerNumber: '010-1234-5678',
    },
    {
        id: 2,
        image: 'https://via.placeholder.com/150',
        date: '2024.06.27',
        time: '15:30 ~ 16:00',
        customerName: '오유진',
        status: '취소',
        statusColor: 'error',
        customerNumber: '010-9876-5432',
    },
    {
        id: 3,
        image: 'https://via.placeholder.com/150',
        date: '2024.06.27',
        time: '16:00 ~ 16:30',
        customerName: '서정희',
        status: '대기',
        statusColor: 'warning',
        customerNumber: '010-4567-8912',
    },
];

function AgentReservationList() {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow>
                        {!isMobile && <TableCell>미리보기</TableCell>}
                        <TableCell>예약일</TableCell>
                        <TableCell>예약 시간</TableCell>
                        {!isMobile && <TableCell>예약자명</TableCell>}
                        <TableCell>예약 상태</TableCell>
                        <TableCell>예약자 번호</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody sx={{ bgcolor: theme.palette.common.white }}>
                    {reservations.map((reservation) => (
                        <AgentReservationItem key={reservation.id} reservation={reservation} />
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default AgentReservationList;
