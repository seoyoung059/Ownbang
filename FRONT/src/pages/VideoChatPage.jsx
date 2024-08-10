import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Video from '../components/video-chat/Video';
import CheckList from '../components/checklist/CheckList';
import { useBoundStore } from '../store/store';
import { Container, Grid, Box, Button, Divider, useMediaQuery, Typography } from '@mui/material';

export default function VideoChatPage() {
    const { enterVideoRoom, leaveVideoRoom, fetchUser, user } = useBoundStore((state) => ({
        enterVideoRoom: state.enterVideoRoom,
        leaveVideoRoom: state.leaveVideoRoom,
        fetchUser: state.fetchUser,
        user: state.user,
    }));

    useEffect(() => {
        const fetchData = async () => {
            await fetchUser();
        };
        fetchData();
    }, []);

    const navigate = useNavigate();
    const [tabValue, setTabValue] = useState(0);
    const isSmallScreen = useMediaQuery('(max-width:600px)');

    const handleChange = (newValue) => {
        setTabValue(newValue);
    };

    const handleLeaveSession = () => {
        navigate('/'); // 추가 경로 설정
    };

    return (
        <Container sx={{ mt: 12 }}>
            {!user.isAgent ? (
                <Grid container spacing={2} direction={isSmallScreen ? 'column' : 'row'}>
                    <Grid item xs={12} md={8}>
                        <Video
                            enterVideoRoom={enterVideoRoom}
                            leaveVideoRoom={leaveVideoRoom}
                            user={user}
                            fetchUser={fetchUser}
                        />
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Box
                            sx={{
                                display: 'flex',
                                justifyContent: 'center',
                                alignItems: 'center',
                                mt: 4,
                                mb: 2,
                                gap: 2,
                            }}
                        >
                            <Button
                                variant={tabValue === 0 ? 'contained' : 'outlined'}
                                onClick={() => handleChange(0)}
                                sx={{ width: '40%' }}
                            >
                                채팅
                            </Button>
                            <Divider orientation="vertical" flexItem />
                            <Button
                                variant={tabValue === 1 ? 'contained' : 'outlined'}
                                onClick={() => handleChange(1)}
                                sx={{ width: '40%' }}
                            >
                                체크리스트
                            </Button>
                        </Box>
                        {tabValue === 0 ? <div>dd</div> : <CheckList canEdit={true} />}
                    </Grid>
                </Grid>
            ) : (
                <Grid container spacing={2} direction="column">
                    <Grid item xs={12}>
                        <Video
                            enterVideoRoom={enterVideoRoom}
                            leaveVideoRoom={leaveVideoRoom}
                            user={user}
                            fetchUser={fetchUser}
                        />
                    </Grid>
                    {/* <Grid item xs={12} sx={{ mt: 12 }}>
            <Typography>채팅 컴퍼넌트</Typography>
          </Grid> */}
                </Grid>
            )}
        </Container>
    );
}
