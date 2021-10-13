/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { searchIcon } from "../../../assets/icon/Icon";

const SearchBar = ({ onInputClick }) => {
  const barStyle = css`
    display: flex;
    align-items: center;
    padding: 0 19px;
    height: 61px;
    border-radius: 18px;
    background-color: #626262;
    margin-bottom: 3rem;
  `

  const iconStyle = css`
    width: 36px;
    height: 36px;
  `

  const textStyle = css`
    color: white;
    font-weight: 700;
    font-size: 22px;
    background-color: #626262;
  `

  const dividerStyle = css`
    border-left: 3px solid #c4c4c4;
    height: 48px;
    margin: 0 16px;
  `

  return (
    <div css={barStyle}>
      <img src={searchIcon} alt="검색아이콘" css={iconStyle} />
      <div css={dividerStyle}></div>
      <input type="text" placeholder="SEARCH" css={textStyle} onClick={onInputClick} />
    </div>
  )
}

export default SearchBar;