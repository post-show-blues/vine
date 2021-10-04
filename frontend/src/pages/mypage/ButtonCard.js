/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const ButtonCard = ({text, data, color}) => {
  const buttonStyle = css`
    border-radius: 10px;
    background-color: #404040;
    width: 148px;
    height: 54px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    margin-left: 20px;
    font-weight: 700;

    &::before {
      content: '${text}';
      color: white;
      position: absolute;
      top: -27px;
      font-size: 15px;
    }
  `

  const textStyle = css`
    color: ${color};
    font-size: 36px;
  `

  return (
    <div css={buttonStyle}>
      <span css={textStyle}>{data}</span>
    </div>
  )
}

export default ButtonCard;