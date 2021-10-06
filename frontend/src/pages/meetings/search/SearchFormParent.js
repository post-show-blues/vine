/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useState } from 'react';
import { innerShadow } from "../../constant";
import AccountSearch from "./AccountSearch";

const SearchFormParent = () => {
  const [isAccountActive, setIsAccountActive] = useState(false);
  const [isRoomActive, setIsRoomActive] = useState(false);

  const outStyle = css`
    min-height: 602px;
    background-color: #212121;
    box-shadow: ${innerShadow};
    padding: 30px 45px;
    border-radius: 16px;
  `
  const accountButtonStyle = css`
    width: 347px;
    font-size: 35px;
    color: #2F3100;
    display: flex;
    margin: 0 1rem;
    padding: 0 84px;
    font-weight: 700;
    background-color: ${!isAccountActive ? `#908F8F` : `#FFFFFF`}; 
    border-radius: 15px;
    display:flex;
    justify-content: center;
    align-items: center;
  `

  const roomButtonStyle = css`
    width: 347px;
    font-size: 35px;
    color: #2F3100;
    display: flex;
    margin: 0 1rem;
    padding: 0 84px;
    font-weight: 700;
    background-color: ${!isRoomActive ? `#908F8F` : `#FFFFFF`}; 
    border-radius: 15px;
    display:flex;
    justify-content: center;
    align-items: center;
  `

  return (
    <div css={outStyle}>
      <div className="flex justify-center">
        <div css={accountButtonStyle} onClick={() => { setIsAccountActive(true); setIsRoomActive(false) }}>ACCOUNT</div>
        <div css={roomButtonStyle} onClick={() => { setIsAccountActive(false); setIsRoomActive(true) }}>ROOM</div>
      </div>
      {isAccountActive && <AccountSearch />}
    </div>
  )
}

export default SearchFormParent;