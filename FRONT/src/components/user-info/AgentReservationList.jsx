import React, { useState, useEffect } from 'react';
import {
    TableContainer,
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    Paper,
    CircularProgress,
    Typography,
} from '@mui/material';
import AgentReservationItem from './AgentReservationItem';
import { useMediaQuery, useTheme } from '@mui/material';
import { useBoundStore } from '../../store/store';

function AgentReservationList() {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const { getAgentReservationList, agentReservations, confirmReservation, denyReservation, enterVideoRoom } =
        useBoundStore((state) => ({
            getAgentReservationList: state.getAgentReservationList,
            agentReservations: state.agentReservations,
            confirmReservation: state.confirmReservation,
            denyReservation: state.denyReservation,
            enterVideoRoom: state.enterVideoRoom,
        }));

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchReservations = async () => {
            setLoading(true);
            setError(null);
            try {
                await getAgentReservationList();
            } catch (error) {
                setError('Failed to fetch reservations. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchReservations();
    }, [getAgentReservationList]);

    if (loading) {
        return <CircularProgress />;
    }

    if (error) {
        return <Typography color="error">{error}</Typography>;
    }

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
                        <TableCell>예약 상태 변경</TableCell>
                        <TableCell>예약자 번호</TableCell>
                        <TableCell>화상 채팅</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody sx={{ bgcolor: theme.palette.background.default }}>
                    {agentReservations.length > 0 ? (
                        agentReservations.map((reservation) => (
                            <AgentReservationItem
                                key={reservation.id}
                                reservation={reservation}
                                confirmReservation={confirmReservation}
                                denyReservation={denyReservation}
                                enterVideoRoom={enterVideoRoom}
                            />
                        ))
                    ) : (
                        <TableRow>
                            <TableCell colSpan={6} align="center" sx={{ bgcolor: theme.palette.background.default }}>
                                <Typography variant="body1">예약이 없습니다.</Typography>
                            </TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default AgentReservationList;
