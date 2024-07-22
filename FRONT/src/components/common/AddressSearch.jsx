import React from "react";
import Postcode from "@actbase/react-daum-postcode";

export default function AddressSearch({ handleAddress }) {
  return (
    <Postcode
      style={{ width: 320, height: 320, top: 10 }}
      jsOptions={{ animation: true }}
      onSelected={(data) => handleAddress(data.address)}
    />
  );
}
