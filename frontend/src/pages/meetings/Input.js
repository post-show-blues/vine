/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const Input = (props) => {
  const inputStyle = css`
    width: 100%;
    height: ${props.height}px;
    background-color: #333333;
    border-radius: 25px;
    height: ${props.height}px;
    font-size: 1.5rem;
    font-weight: bold;
    label {
      border-radius: 25px;
      background-color: #575757;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      box-shadow: 4 0 10 rgba(0,0,0, 0.25);
      color: #090909;
      min-width: 168px;
    }
    input {
      color: #d6d6d6;
      background-color: #333333;
      border-radius: 1.5rem;
      padding: 0 2rem;
      width: 100%;
      ::-webkit-calendar-picker-indicator {
        filter: invert(1);
      }
    }
  `
  return (
    <div css={inputStyle} className="flex">
      <label css={css`width: 168px;`} className="font-bold">{props.name}</label>
      <input type={props.type} />
    </div>
  )
}

export default Input