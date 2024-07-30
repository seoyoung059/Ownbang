import { OpenVidu } from "openvidu-browser";
import axios from "axios";
import React, { Component } from "react";
import UserVideoComponent from "./UserVideoComponent";

import {
  AppBar,
  Toolbar,
  Typography,
  Container,
  Grid,
  Paper,
  Button,
  IconButton,
  Avatar,
  Box,
  TextField,
  Divider,
} from "@mui/material";
import MicIcon from "@mui/icons-material/Mic";
import VideocamIcon from "@mui/icons-material/Videocam";
import ExitToAppIcon from "@mui/icons-material/ExitToApp";

const APPLICATION_SERVER_URL = import.meta.env.VITE_API_URL;

class Video extends Component {
  constructor(props) {
    super(props);

    // These properties are in the state's component in order to re-render the HTML whenever their values change
    this.state = {
      mySessionId: "123",
      myUserName: "Participant" + Math.floor(Math.random() * 100),
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
    };

    this.joinSession = this.joinSession.bind(this);
    this.leaveSession = this.leaveSession.bind(this);
    this.switchCamera = this.switchCamera.bind(this);
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
                mainStreamManager: publisher,
                publisher: publisher,
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

  render() {
    const { mySessionId, myUserName, mainStreamManager, subscribers } =
      this.state;

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
          <Grid container spacing={2} style={{ mt: 16 }}>
            <Grid item xs={12}>
              <Paper style={{ position: "relative" }}>
                {mainStreamManager && (
                  <UserVideoComponent
                    streamManager={mainStreamManager}
                    isMain={true}
                  />
                )}
                {subscribers[0] && (
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
              </Paper>
            </Grid>
          </Grid>
        )}
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
