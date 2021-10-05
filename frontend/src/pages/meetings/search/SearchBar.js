/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { searchIcon } from "../../../assets/icon/Icon";

const SearchBar = () => {
  const barStyle = css`
    display: flex;
    align-items: center;
    padding: 0 19px;
    height: 61px;
  `

  const iconStyle = css`
    width: 36px;
    height: 36px;
  `

  const textStyle = css`
    color: white;
    font-weight: 700;
    font-size: 22px;
  `

  return (
    <div css={barStyle}>
      <img src={searchIcon} alt="검색아이콘" css={iconStyle}/>
      <input type="text" placeholder="SEARCH" />
    </div>
  )
}

export default SearchBar;