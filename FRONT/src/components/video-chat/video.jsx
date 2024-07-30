import { OpenVidu } from "openvidu-browser";
import axios from "axios";
import React, { Component } from "react";
import UserVideoComponent from "./UserVideoComponent";

import {
  Container,
  Grid,
  Paper,
  IconButton,
  Box,
  TextField,
  Button,
  Typography,
} from "@mui/material";

import {
  Mic,
  MicOff,
  Videocam,
  VideocamOff,
  ExitToApp,
} from "@mui/icons-material";

const APPLICATION_SERVER_URL = import.meta.env.VITE_API_URL;

class Video extends Component {
  constructor(props) {
    super(props);

    this.state = {
      mySessionId: "123",
      myUserName: "Participant" + Math.floor(Math.random() * 100),
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      isAudioMuted: false,
      isVideoHidden: false,
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
  }

  componentDidMount() {
    window.addEventListener("beforeunload", this.onbeforeunload);
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
    z;
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

          // 첫 번째 스트림을 메인 스트림으로 설정
          if (!this.state.mainStreamManager) {
            this.setState({
              mainStreamManager: event.stream.streamManager,
            });
          }

          this.setState({ subscribers: subscribers });
        });

        mySession.on("streamDestroyed", (event) => {
          this.deleteSubscriber(event.stream.streamManager);
        });

        mySession.on("exception", (exception) => {
          console.warn(exception);
        });

        this.getToken().then((token) => {
          console.log("Received token:", token);
          mySession
            .connect(token, { clientData: this.state.myUserName })
            .then(async () => {
              let publisher = await this.OV.initPublisherAsync(undefined, {
                audioSource: undefined,
                videoSource: undefined,
                publishAudio: true,
                publishVideo: true,
                resolution: "640x480",
                frameRate: 30,
                insertMode: "APPEND",
                mirror: true,
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

  leaveSession() {
    const mySession = this.state.session;

    if (mySession) {
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
    });

    window.location.href = "/";
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
            mirror: true,
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
      mySessionId,
      myUserName,
      mainStreamManager,
      publisher,
      subscribers,
    } = this.state;

    return (
      <Container component="main" maxWidth="lg">
        {this.state.session === undefined ? (
          <Box mt={8} textAlign="center">
            <Typography component="h1" variant="h5">
              Join a video session
            </Typography>
            <Box component="form" mt={3} onSubmit={this.joinSession}>
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="userName"
                label="Participant"
                name="userName"
                value={myUserName}
                onChange={this.handleChangeUserName}
              />
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="sessionId"
                label="Session ID"
                name="sessionId"
                value={mySessionId}
                onChange={this.handleChangeSessionId}
              />
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                sx={{ mt: 3, mb: 2 }}
              >
                Join
              </Button>
            </Box>
          </Box>
        ) : (
          <Grid container spacing={2} style={{ marginTop: 16 }}>
            <Grid item xs={12}>
              <Paper style={{ position: "relative", height: "80vh" }}>
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
                <Box
                  sx={{
                    position: "absolute",
                    bottom: 16,
                    left: 16,
                    display: "flex",
                    gap: 2,
                  }}
                >
                  <IconButton color="primary" onClick={this.toggleAudio}>
                    {this.state.isAudioMuted ? <MicOff /> : <Mic />}
                  </IconButton>
                  <IconButton color="primary" onClick={this.toggleVideo}>
                    {this.state.isVideoHidden ? <VideocamOff /> : <Videocam />}
                  </IconButton>
                </Box>
              </Paper>
            </Grid>
          </Grid>
        )}
        <Button onClick={this.leaveSession}>
          <ExitToApp sx={{ mr: 1 }} />
          나가기
        </Button>
      </Container>
    );
  }

  async getToken() {
    const token = await this.createToken(this.state.mySessionId);
    return token;
  }

  async createToken(mySessionId) {
    const response = await axios.post(
      APPLICATION_SERVER_URL + "/api/webrtcs/get-token",
      { reservationId: mySessionId },
      {
        headers: { "Content-Type": "application/json" },
      }
    );
    return response.data.data.token;
  }
}

export default Video;
