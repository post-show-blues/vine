/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import camera from "../../assets/images/camera.svg";
import { innerShadow } from "../constant";

const InputImg = () => {
  const imgInputStyle = css`
    border-radius: 25px;
    background-color: #333333;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 272px;
    margin: 1rem 0;
    box-shadow: ${innerShadow};
  `
  return (
    <div css={imgInputStyle}>
      <img src={camera} alt="사진추가아이콘" css={css`width: 65px; height: 47.6px;`} />
    </div>
  )
}

export default InputImg;