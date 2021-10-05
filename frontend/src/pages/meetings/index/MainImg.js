/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { defaultPd } from "../../constant";

const MainImg = ({url, alt}) => {
  const imgStyle = css`
    width: 100%;
    height: 420px;
    border-radius: 25px;
    margin: ${defaultPd}px 0;
  `

  return (
    <img src={url} alt={alt} css={imgStyle}/>
  )
}

export default MainImg;