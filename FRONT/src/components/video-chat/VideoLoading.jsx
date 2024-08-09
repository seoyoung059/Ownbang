import React from "react";
import styled, { keyframes } from "styled-components";

// keyframes를 사용하여 애니메이션을 정의합니다.
const pulse = keyframes`
  0% {
    transform: scale(0.8);
    background-color: #b3d4fc;
    box-shadow: 0 0 0 0 rgba(178, 212, 252, 0.7);
  }
  50% {
    transform: scale(1.2);
    background-color: #6793fb;
    box-shadow: 0 0 0 10px rgba(178, 212, 252, 0);
  }
  100% {
    transform: scale(0.8);
    background-color: #b3d4fc;
    box-shadow: 0 0 0 0 rgba(178, 212, 252, 0.7);
  }
`;

// styled-components를 사용하여 스타일을 정의합니다.
const DotsContainer = styled.section`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
`;

const Dot = styled.div`
  height: 20px;
  width: 20px;
  margin-right: ${(props) => (props.isLast ? "0" : "10px")};
  border-radius: 10px;
  background-color: #b3d4fc;
  animation: ${pulse} 1.5s infinite ease-in-out;
  animation-delay: ${(props) => props.delay};
`;

const VideoLoading = () => {
  return (
    <>
      <DotsContainer>
        <Dot delay="-0.3s" />
        <Dot delay="-0.1s" />
        <Dot delay="0.1s" />
        <Dot delay="0.3s" />
        <Dot delay="0.5s" isLast />
      </DotsContainer>
    </>
  );
};

export default VideoLoading;
