import React from 'react';
import { TableRow, TableCell, Avatar, Typography, Button, Box } from '@mui/material';
import { CheckCircleOutlined, CancelOutlined, PendingOutlined } from '@mui/icons-material';
import { useMediaQuery, useTheme } from '@mui/material';

export default function MyReservationItem({ reservation }) {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const getStatusIconAndText = (status) => {
        switch (status) {
            case 'CONFIRMED':
                return {
                    icon: <CheckCircleOutlined style={{ color: 'green' }} />,
                    text: '확정',
                };
            case 'APPLYED':
                return {
                    icon: <PendingOutlined style={{ color: 'orange' }} />,
                    text: '대기',
                };
            case 'CANCELLED':
                return {
                    icon: <CancelOutlined style={{ color: 'red' }} />,
                    text: '취소',
                };
            default:
                return {
                    icon: null,
                    text: '알 수 없음',
                };
        }
    };

    const { icon, text } = getStatusIconAndText(reservation.status);

    // 예약 날짜 데이터
    let reservationDate = new Date(reservation.reservationTime).toLocaleDateString('ko-KR');
    // 문자열의 마지막이 "."로 끝나면 "." 제거
    if (reservationDate.endsWith('.')) {
        reservationDate = reservationDate.slice(0, -1);
    }

    // 예약 시간 데이터
    const reservationTime = new Date(reservation.reservationTime).toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit',
    });

    return (
        <TableRow sx={{ bgcolor: theme.palette.background.default }}>
            {!isMobile && (
                <TableCell sx={{ padding: '4px 8px' }}>
                    <Avatar
                        src={reservation.roomProfileImage}
                        variant="rounded"
                        sx={{ width: '150px', height: '90px' }}
                    />
                </TableCell>
            )}
            <TableCell>
                <Typography variant={isMobile ? 'body2' : 'body1'}>{reservationDate}</Typography>
            </TableCell>
            <TableCell>
                <Typography variant={isMobile ? 'body2' : 'body1'}>{reservationTime}</Typography>
            </TableCell>
            {!isMobile && (
                <TableCell>
                    <Typography variant={'body1'}>{reservation.AgentOfficeName}</Typography>
                </TableCell>
            )}
            <TableCell>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    {icon}
                    <Typography sx={{ ml: 1, whiteSpace: 'nowrap' }} variant={'body1'}>
                        {text}
                    </Typography>
                </Box>
            </TableCell>
            <TableCell>
                <Button
                    variant="contained"
                    disabled={reservation.status !== 'confirmed'}
                    size={isMobile ? 'small' : 'medium'}
                >
                    입장하기
                </Button>
            </TableCell>
        </TableRow>
    );
}
