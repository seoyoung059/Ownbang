import { OpenVidu } from "openvidu-browser";
import React, { Component } from "react";
import UserVideoComponent from "./UserVideoComponent";
import VideoLoading from "./VideoLoading";
import withNavigation from "./withNavigation";
import {
  Container,
  Grid,
  Paper,
  IconButton,
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from "@mui/material";

import {
  Mic,
  MicOff,
  Videocam,
  VideocamOff,
  ExitToApp,
  SwitchCamera,
} from "@mui/icons-material";

const withNavigate = (Component) => {
  return function WrappedComponent(props) {
    const navigate = useNavigate();
    return <Component {...props} navigate={navigate} />;
  };
};

class Video extends Component {
  constructor(props) {
    super(props);

    this.state = {
      mySessionId: "",
      myUserName: "",
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      isAudioMuted: false,
      isVideoHidden: false,
      isLeaveDialogOpen: false,
      endSessionDialogOpen: false,
      token: undefined,
      loading: true,
      isScreenSmall: window.innerWidth < 1024,
      videoDevices: [],
      selectedVideoDevice: "",
    };

    this.joinSession = this.joinSession.bind(this);
    this.leaveSession = this.leaveSession.bind(this);
    this.switchCamera = this.switchCamera.bind(this);
    this.toggleAudio = this.toggleAudio.bind(this);
    this.toggleVideo = this.toggleVideo.bind(this);
    this.handleChangeSessionId = this.handleChangeSessionId.bind(this);
    this.handleChangeUserName = this.handleChangeUserName.bind(this);
    this.handleMainVideoStream = this.handleMainVideoStream.bind(this);
    this.onbeforeunload = this.onbeforeunload.bind(this);
    this.handleOpenLeaveDialog = this.handleOpenLeaveDialog.bind(this);
    this.handleCloseLeaveDialog = this.handleCloseLeaveDialog.bind(this);
    this.handleCloseEndSessionDialog =
      this.handleCloseEndSessionDialog.bind(this);
    this.handlePopState = this.handlePopState.bind(this);
    this.updateScreenSize = this.updateScreenSize.bind(this);
    this.handleVideoDeviceChange = this.handleVideoDeviceChange.bind(this);
  }

  async componentDidMount() {
    window.addEventListener("beforeunload", this.onbeforeunload);
    window.addEventListener("resize", this.updateScreenSize);
    window.history.pushState(null, "", location.href);
    window.onpopstate = this.handlePopState;

    try {
      await this.props.fetchUser();
      this.setState({
        mySessionId: this.props.reservationId,
        myUserName: this.props.user.isAgent ? "중개인" : "임차인",
      });
      if (!this.state.token) {
        this.joinSession();
      }
    } catch (error) {
      console.error("Failed to fetch user info:", error);
    }
  }

  componentWillUnmount() {
    window.removeEventListener("beforeunload", this.onbeforeunload);
    window.removeEventListener("resize", this.updateScreenSize);
    window.onpopstate = null;

    if (this.state.session) {
      this.state.session.off("sessionDisconnected");
    }
  }

  onbeforeunload(event) {
    this.leaveSession();
  }

  updateScreenSize() {
    this.setState({ isScreenSmall: window.innerWidth < 768 });
  }

  handleChangeSessionId(e) {
    this.setState({
      mySessionId: e.target.value,
    });
  }

  handleChangeUserName(e) {
    this.setState({
      myUserName: e.target.value,
    });
  }

  handlePopState = (event) => {
    event.preventDefault();
    this.setState({ isLeaveDialogOpen: true });
    window.history.pushState(null, "", location.href);
  };

  handleMainVideoStream(stream) {
    if (!this.state.mainStreamManager) {
      this.setState({
        mainStreamManager: stream,
      });
    }
  }

  deleteSubscriber(streamManager) {
    let subscribers = this.state.subscribers;
    let index = subscribers.indexOf(streamManager, 0);
    if (index > -1) {
      subscribers.splice(index, 1);
      this.setState({
        subscribers: subscribers,
      });
    }
  }

  async joinSession() {
    this.OV = new OpenVidu();

    this.setState(
      {
        session: this.OV.initSession(),
      },
      async () => {
        var mySession = this.state.session;

        this.state.session.on("sessionDisconnected", (event) => {
          if (!this.props.user.isAgent) {
            this.handleOpenEndSessionDialog();
          }
        });

        mySession.on("streamCreated", (event) => {
          var subscriber = mySession.subscribe(event.stream, undefined);
          var subscribers = this.state.subscribers;
          subscribers.push(subscriber);

          if (!this.state.mainStreamManager) {
            this.setState({
              mainStreamManager: event.stream.streamManager,
            });
          }

          this.setState({ subscribers: subscribers });
        });

        mySession.on("streamDestroyed", (event) => {
          this.deleteSubscriber(event.stream.streamManager);
          if (
            event.stream.streamManager === this.state.mainStreamManager &&
            !this.props.user.isAgent
          ) {
            this.setState({ endSessionDialogOpen: true });
          }
        });

        mySession.on("exception", (exception) => {
          console.warn(exception);
          if (
            exception.name === "disconnect" ||
            exception.name === "networkDisconnect"
          ) {
            this.handleOpenEndSessionDialog();
          }
        });

        this.props.enterVideoRoom(this.state.mySessionId).then(async (res) => {
          this.setState({ token: res.token });
          if (res.createdAt) {
            localStorage.setItem("createdAt", res.createdAt);
          }
          mySession
            .connect(res.token, { clientData: this.state.myUserName })
            .then(async () => {
              let publisher = await this.OV.initPublisherAsync(undefined, {
                audioSource: undefined,
                videoSource: undefined,
                publishAudio: true,
                publishVideo: true,
                resolution: "1280x960",
                frameRate: 30,
                insertMode: "APPEND",
                mirror: false,
              });

              mySession.publish(publisher);

              var devices = await this.OV.getDevices();
              var videoDevices = devices.filter(
                (device) => device.kind === "videoinput"
              );
              this.setState({
                videoDevices,
                selectedVideoDevice:
                  videoDevices.length > 0 ? videoDevices[0].deviceId : "",
                currentVideoDevice: videoDevices[0],
                publisher: publisher,
                mainStreamManager: this.state.mainStreamManager || publisher,
                loading: false,
              });
            })
            .catch((error) => {
              console.error(
                "There was an error connecting to the session:",
                error.code,
                error.message
              );
            });
        });
      }
    );
  }

  handleOpenLeaveDialog() {
    this.setState({ isLeaveDialogOpen: true });
  }

  handleCloseLeaveDialog(confirm) {
    if (confirm) {
      this.leaveSession();
      this.setState({ isLeaveDialogOpen: false }, () => {
        setTimeout(() => {
          window.history.back();
        }, 0);
      });
    } else {
      this.setState({ isLeaveDialogOpen: false });
    }
  }

  async leaveSession() {
    const { session, token, mySessionId } = this.state;

    if (session) {
      try {
        // WebSocket이 이미 닫혀 있지 않은 경우에만 leaveVideoRoom 요청
        if (session.connection && session.connection.rpcBuilder) {
          const wsState = session.connection.rpcBuilder.websocket.readyState;
          if (wsState === WebSocket.OPEN) {
            await this.props
              .leaveVideoRoom(mySessionId, token)
              .catch((error) => {
                console.error("Error while leaving video room:", error);
              });
          } else {
            console.warn(
              "WebSocket is already closing or closed. Skipping leaveRoom request."
            );
          }
        }

        // 여기에서 removeToken 요청을 전송
        await this.props.leaveVideoRoom(mySessionId, token).catch((error) => {
          console.error("Error while removing token:", error);
        });

        await session.disconnect();
        console.log("Disconnected from the session");
      } catch (error) {
        console.error("Error during session disconnection:", error);
      } finally {
        this.cleanupSessionState();
      }
    } else {
      console.warn("You were not connected to the session");
      this.cleanupSessionState();
    }
  }

  cleanupSessionState = () => {
    localStorage.removeItem("createdAt");

    this.setState(
      {
        session: undefined,
        subscribers: [],
        mySessionId: "",
        myUserName: "",
        mainStreamManager: undefined,
        publisher: undefined,
        isLeaveDialogOpen: false,
      },
      () => {
        const { navigate } = this.props;
        navigate("/", { replace: true });
      }
    );
  };

  handleOpenEndSessionDialog() {
    this.setState({ endSessionDialogOpen: true });
  }

  handleCloseEndSessionDialog() {
    this.setState({ endSessionDialogOpen: false });
    const { navigate } = this.props;
    navigate("/", { replace: true });
  }

  async switchCamera() {
    const { selectedVideoDevice, session, publisher } = this.state;

    if (!selectedVideoDevice || !session) {
      console.error("No selected video device or session.");
      return;
    }

    try {
      // 기존 퍼블리셔의 스트림을 안전하게 중지
      if (publisher) {
        publisher.stream.getTracks().forEach((track) => track.stop());
      }

      // 새 퍼블리셔 생성
      const newPublisher = await this.OV.initPublisherAsync(undefined, {
        videoSource: selectedVideoDevice, // 선택한 비디오 디바이스
        publishAudio: true,
        publishVideo: true,
        mirror: false,
      });

      // 기존 퍼블리셔를 언퍼블리시
      if (publisher) {
        await session.unpublish(publisher);
      }

      // 새 퍼블리셔를 퍼블리시
      await session.publish(newPublisher);

      // 상태 업데이트
      this.setState({
        publisher: newPublisher,
        mainStreamManager: newPublisher,
      });
    } catch (error) {
      console.error("Error switching camera:", error);

      // 오류가 발생하면 기존 스트림으로 돌아가기 위한 복구 로직
      if (publisher) {
        await session.publish(publisher);
      }
    }
  }

  handleVideoDeviceChange(event) {
    console.log(event);
    this.setState({ selectedVideoDevice: event.target.value }, () => {
      this.switchCamera();
    });
  }

  toggleAudio() {
    this.setState(
      (prevState) => ({
        isAudioMuted: !prevState.isAudioMuted,
      }),
      () => {
        this.state.publisher.publishAudio(!this.state.isAudioMuted);
      }
    );
  }

  toggleVideo() {
    this.setState(
      (prevState) => ({
        isVideoHidden: !prevState.isVideoHidden,
      }),
      () => {
        this.state.publisher.publishVideo(!this.state.isVideoHidden);
      }
    );
  }

  render() {
    const {
      mainStreamManager,
      publisher,
      subscribers,
      isLeaveDialogOpen,
      endSessionDialogOpen,
      loading,
      isScreenSmall,
      videoDevices,
      selectedVideoDevice,
    } = this.state;

    const isAgent = this.props.user.isAgent;

    return (
      <Container component="main" maxWidth="lg">
        <Grid container spacing={2} style={{ marginTop: 16 }}>
          <Grid item xs={12}>
            <Paper style={{ position: "relative", height: "80vh" }}>
              {loading ? (
                <Box
                  style={{
                    position: "absolute",
                    top: "50%",
                    left: "50%",
                    transform: "translate(-50%, -50%)",
                    textAlign: "center",
                  }}
                >
                  <VideoLoading />
                </Box>
              ) : (
                <>
                  {mainStreamManager && (
                    <UserVideoComponent
                      streamManager={mainStreamManager}
                      isMain={true}
                    />
                  )}
                  {publisher !== mainStreamManager && (
                    <Box
                      style={{
                        position: "absolute",
                        bottom: 16,
                        right: 16,
                        width: "20%",
                        height: "26%",
                        borderRadius: "8px",
                        overflow: "hidden",
                        border: "2px solid white",
                      }}
                    >
                      <UserVideoComponent
                        streamManager={publisher}
                        isMain={false}
                      />
                    </Box>
                  )}
                  {subscribers[0] && subscribers[0] !== mainStreamManager && (
                    <Box
                      style={{
                        position: "absolute",
                        bottom: 16,
                        right: 16,
                        width: "20%",
                        height: "26%",
                        borderRadius: "8px",
                        overflow: "hidden",
                        border: "2px solid white",
                      }}
                    >
                      <UserVideoComponent
                        streamManager={subscribers[0]}
                        isMain={false}
                      />
                    </Box>
                  )}
                  {mainStreamManager && isAgent && (
                    <Box
                      style={{
                        position: "absolute",
                        top: 16,
                        right: 16,
                      }}
                    >
                      <FormControl
                        variant="outlined"
                        size="small"
                        sx={{ minWidth: 120, backgroundColor: "white" }}
                      >
                        <InputLabel
                          id="camera-select-label"
                          sx={{
                            color: "#ffffff",
                            textShadow: "1px 1px 2px rgba(0, 0, 0, 0.7)",
                          }}
                        >
                          카메라 선택
                        </InputLabel>
                        <Select
                          labelId="camera-select-label"
                          id="camera-select"
                          value={selectedVideoDevice}
                          onChange={this.handleVideoDeviceChange}
                          label="카메라 선택"
                          sx={{
                            backgroundColor: "white",
                            color: "black",
                          }}
                        >
                          {videoDevices.map((device) => (
                            <MenuItem
                              key={device.deviceId}
                              value={device.deviceId}
                              sx={{
                                backgroundColor: "white",
                                color: "black",
                              }}
                            >
                              {device.label || `Camera ${device.deviceId}`}
                            </MenuItem>
                          ))}
                        </Select>
                      </FormControl>
                    </Box>
                  )}
                </>
              )}
              <Box
                sx={{
                  position: "absolute",
                  bottom: -62,
                  left: 16,
                  right: 16,
                  display: "flex",
                  justifyContent: "space-between",
                  gap: 2,
                  borderRadius: "8px",
                  padding: "8px",
                }}
              >
                <Box>
                  <IconButton color="primary" onClick={this.toggleAudio}>
                    {this.state.isAudioMuted ? (
                      <MicOff sx={{ color: "red" }} />
                    ) : (
                      <Mic />
                    )}
                  </IconButton>
                  <IconButton color="primary" onClick={this.toggleVideo}>
                    {this.state.isVideoHidden ? (
                      <VideocamOff sx={{ color: "red" }} />
                    ) : (
                      <Videocam />
                    )}
                  </IconButton>
                </Box>
                <Button onClick={this.handleOpenLeaveDialog}>
                  <ExitToApp sx={{ mr: 1 }} />
                  나가기
                </Button>
              </Box>
            </Paper>
          </Grid>
        </Grid>
        <Dialog
          open={isLeaveDialogOpen}
          onClose={() => this.handleCloseLeaveDialog(false)}
        >
          <DialogTitle>나가기</DialogTitle>
          <DialogContent>
            <DialogContentText>정말 세션을 나가시겠습니까?</DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button
              onClick={() => this.handleCloseLeaveDialog(false)}
              color="primary"
            >
              취소
            </Button>
            <Button
              onClick={() => this.handleCloseLeaveDialog(true)}
              color="primary"
            >
              확인
            </Button>
          </DialogActions>
        </Dialog>

        <Dialog
          open={endSessionDialogOpen}
          onClose={this.handleCloseEndSessionDialog}
        >
          <DialogTitle>세션 종료</DialogTitle>
          <DialogContent>
            <DialogContentText>세션이 종료되었습니다.</DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleCloseEndSessionDialog} color="primary">
              확인
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    );
  }
}

export default withNavigation(Video);
