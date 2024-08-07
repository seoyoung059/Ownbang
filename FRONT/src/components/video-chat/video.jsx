import { OpenVidu } from "openvidu-browser";
import React, { Component } from "react";
import UserVideoComponent from "./UserVideoComponent";
import VideoLoading from "./VideoLoading";

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
} from "@mui/material";

import {
  Mic,
  MicOff,
  Videocam,
  VideocamOff,
  ExitToApp,
} from "@mui/icons-material";

class Video extends Component {
  constructor(props) {
    super(props);

    this.state = {
      mySessionId: "16",
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
  }

  async componentDidMount() {
    window.addEventListener("beforeunload", this.onbeforeunload);

    try {
      await this.props.fetchUser();
      this.setState({
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
  }

  onbeforeunload(event) {
    this.leaveSession();
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

  handleMainVideoStream(stream) {
    if (this.state.mainStreamManager !== stream) {
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

  joinSession() {
    this.OV = new OpenVidu();

    this.setState(
      {
        session: this.OV.initSession(),
      },
      () => {
        var mySession = this.state.session;

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
          if (event.stream.streamManager === this.state.mainStreamManager) {
            this.endSession(); // 메인 스트림이 나가면 세션 종료
          }
        });

        mySession.on("exception", (exception) => {
          console.warn(exception);
        });

        this.props.enterVideoRoom(this.state.mySessionId).then((token) => {
          this.setState({ token: token }); // 토큰 상태 저장
          mySession
            .connect(token, { clientData: this.state.myUserName })
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
              var currentVideoDeviceId = publisher.stream
                .getMediaStream()
                .getVideoTracks()[0]
                .getSettings().deviceId;
              var currentVideoDevice = videoDevices.find(
                (device) => device.deviceId === currentVideoDeviceId
              );

              this.setState({
                currentVideoDevice: currentVideoDevice,
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
    } else {
      this.setState({ isLeaveDialogOpen: false });
    }
  }

  async leaveSession() {
    const mySession = this.state.session;

    if (mySession) {
      if (this.state.mainStreamManager === this.state.publisher) {
        await this.props.leaveVideoRoom(
          this.state.mySessionId,
          this.state.token
        );
        mySession.streamManagers.forEach((streamManager) => {
          mySession.forceUnpublish(streamManager);
        });
      }
      mySession.disconnect();
    }

    this.OV = null;
    this.setState({
      session: undefined,
      subscribers: [],
      mySessionId: "SessionA",
      myUserName: "Participant" + Math.floor(Math.random() * 100),
      mainStreamManager: undefined,
      publisher: undefined,
      isLeaveDialogOpen: false,
    });

    window.location.href = "/";
  }

  handleOpenEndSessionDialog() {
    this.setState({ endSessionDialogOpen: true });
  }

  handleCloseEndSessionDialog() {
    this.setState({ endSessionDialogOpen: false });
    window.location.href = "/";
  }

  endSession() {
    console.log("endSession called");
    this.setState({ endSessionDialogOpen: true });
  }

  async switchCamera() {
    try {
      const devices = await this.OV.getDevices();
      var videoDevices = devices.filter(
        (device) => device.kind === "videoinput"
      );

      if (videoDevices && videoDevices.length > 1) {
        var newVideoDevice = videoDevices.filter(
          (device) => device.deviceId !== this.state.currentVideoDevice.deviceId
        );

        if (newVideoDevice.length > 0) {
          var newPublisher = this.OV.initPublisher(undefined, {
            videoSource: newVideoDevice[0].deviceId,
            publishAudio: true,
            publishVideo: true,
            mirror: false,
          });

          await this.state.session.unpublish(this.state.mainStreamManager);
          await this.state.session.publish(newPublisher);
          this.setState({
            currentVideoDevice: newVideoDevice[0],
            mainStreamManager: newPublisher,
            publisher: newPublisher,
          });
        }
      }
    } catch (e) {
      console.error(e);
    }
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
      loading, // 로딩 상태 추가
    } = this.state;

    return (
      <Container component="main" maxWidth="lg">
        <Grid container spacing={2} style={{ marginTop: 16 }}>
          <Grid item xs={12}>
            <Paper style={{ position: "relative", height: "80vh" }}>
              {loading ? ( // 로딩 상태일 때 표시할 내용
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

export default Video;
